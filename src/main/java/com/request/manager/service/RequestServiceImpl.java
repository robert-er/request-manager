package com.request.manager.service;

import com.request.manager.domain.History;
import com.request.manager.domain.HistoryEntry;
import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.exception.NotFoundException;
import com.request.manager.exception.NotValidException;
import com.request.manager.exception.StatusException;
import com.request.manager.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final HistoryService historyService;

    @Override
    public Request findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id: %s does not exist", id)));
    }

    @Override
    public Request createRequest(Request request) {
        Status status = Status.CREATED;
        validateRequest(request);

        History history = createHistory(request);

        request.setStatus(status);

        HistoryEntry historyEntry = prepareHistoryEntry(history, null, request);

        Request outgoingRequest = saveRequest(request);
        historyService.addEntryToHistory(outgoingRequest.getId(), historyEntry);

        return outgoingRequest;
    }

    @Override
    public Request verifyRequest(Request request) {
        Status incomingStatus = request.getStatus();
        Status outgoingStatus = Status.VERIFIED;
        if (incomingStatus != null && Status.CREATED.equals(incomingStatus)) {
            request.setStatus(outgoingStatus);

            Request outgoingRequest = saveRequest(request);

            HistoryEntry historyEntry = prepareHistoryEntry(historyService.findByRequestId(outgoingRequest.getId()),
                    incomingStatus, outgoingRequest);
            historyService.addEntryToHistory(outgoingRequest.getId(), historyEntry);

            return outgoingRequest;
        } else {
            throw new StatusException(String.format("Verify failed. Can verify only request with status CREATED." +
                    " Actual status: %s", incomingStatus));
        }
    }

    @Override
    public Request acceptRequest(Request request) {
        Status incomingStatus = request.getStatus();
        Status outgoingStatus = Status.ACCEPTED;
        if (request.getStatus() != null && Status.VERIFIED.equals(incomingStatus)) {
            request.setStatus(outgoingStatus);

            Request outgoingRequest = saveRequest(request);

            HistoryEntry historyEntry = prepareHistoryEntry(historyService.findByRequestId(outgoingRequest.getId()),
                    incomingStatus, outgoingRequest);
            historyService.addEntryToHistory(outgoingRequest.getId(), historyEntry);

            return outgoingRequest;
        } else {
            throw new StatusException(String.format("Accept failed. Can accept only request with status VERIFIED." +
                    " Actual status: %s", incomingStatus));
        }
    }

    @Override
    public Request rejectRequest(Request request) {
        Status incomingStatus = request.getStatus();
        Status outgoingStatus = Status.REJECTED;
        if (incomingStatus != null &&
                (Status.VERIFIED.equals(incomingStatus) || Status.ACCEPTED.equals(incomingStatus))) {
            request.setStatus(outgoingStatus);

            Request outgoingRequest = saveRequest(request);

            HistoryEntry historyEntry = prepareHistoryEntry(historyService.findByRequestId(outgoingRequest.getId()),
                    incomingStatus, outgoingRequest);
            historyService.addEntryToHistory(outgoingRequest.getId(), historyEntry);

            return outgoingRequest;
        } else {
            throw new StatusException(String.format("Reject failed. Can reject only request with status VERIFIED" +
                    " or ACCEPTED. Actual status: %s", incomingStatus));
        }
    }

    @Override
    public Request publishRequest(Request request) {
        Status incomingStatus = request.getStatus();
        Status outgoingStatus = Status.PUBLISHED;
        if (incomingStatus != null && Status.ACCEPTED.equals(incomingStatus)) {
            request.setStatus(outgoingStatus);
            request.setUuid(UUID.randomUUID());

            Request outgoingRequest = saveRequest(request);

            HistoryEntry historyEntry = prepareHistoryEntry(historyService.findByRequestId(outgoingRequest.getId()),
                    incomingStatus, outgoingRequest);
            historyService.addEntryToHistory(outgoingRequest.getId(), historyEntry);

            return outgoingRequest;
        } else {
            throw new StatusException(String.format("Publish failed. Can publish only request with status ACCEPTED." +
                    " Actual status: %s", incomingStatus));
        }
    }

    private Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public Request deleteRequest(Request request) {
        Status incomingStatus = request.getStatus();
        Status outgoingStatus = Status.DELETED;
        if (incomingStatus == null || Status.CREATED.equals(incomingStatus)) {
            request.setStatus(outgoingStatus);

            Request outgoingRequest = saveRequest(request);

            HistoryEntry historyEntry = prepareHistoryEntry(historyService.findByRequestId(outgoingRequest.getId()),
                    incomingStatus, outgoingRequest);
            historyService.addEntryToHistory(outgoingRequest.getId(), historyEntry);

            return outgoingRequest;
        } else {
            throw new StatusException(String.format("Delete failed. Can delete only request with status CREATED." +
                    " Actual status: %s", incomingStatus));
        }
    }

    private void validateRequest(Request request) {
        if (request.getTitle() == null || request.getDescription() == null) {
            throw new NotValidException(String.format("Create request: title and description required. " +
                    "Provided title: %s, description: %s", request.getTitle(), request.getDescription()));
        }
    }

    private HistoryEntry prepareHistoryEntry(History history, Status incomingStatus, Request outgoingRequest) {
        HistoryEntry historyEntry = HistoryEntry.builder()
                .history(history)
                .date(LocalDateTime.now())
                .details(outgoingRequest.getDetails())
                .incomingStatus(incomingStatus)
                .outgoingStatus(outgoingRequest.getStatus())
                .build();

        return historyEntry;
    }

    private History createHistory(Request request) {
        History history = History.builder()
                .request(request)
                .build();
        historyService.save(history);
        return history;
    }
}
