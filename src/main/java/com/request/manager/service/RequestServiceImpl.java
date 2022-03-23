package com.request.manager.service;

import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.exception.NotFoundException;
import com.request.manager.exception.NotValidException;
import com.request.manager.exception.StatusException;
import com.request.manager.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public Request findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id: %s does not exist", id)));
    }

    @Override
    public Request createRequest(Request request) {
        validateRequest(request);
        request.setStatus(Status.CREATED);
        return saveRequest(request);
    }

    @Override
    public Request verifyRequest(Request request) {
        Status status = request.getStatus();
        if (status != null && Status.CREATED.equals(status)) {
            request.setStatus(Status.VERIFIED);
            return saveRequest(request);
        } else {
            throw new StatusException(String.format("Verify failed. Can verify only request with status CREATED." +
                    " Actual status: %s", status));
        }
    }

    @Override
    public Request acceptRequest(Request request) {
        Status status = request.getStatus();
        if (request.getStatus() != null && Status.VERIFIED.equals(status)) {
            request.setStatus(Status.ACCEPTED);
            return saveRequest(request);
        } else {
            throw new StatusException(String.format("Accept failed. Can accept only request with status VERIFIED." +
                    " Actual status: %s", status));
        }
    }

    @Override
    public Request rejectRequest(Request request) {
        Status status = request.getStatus();
        if (status != null &&
                (Status.VERIFIED.equals(status) || Status.ACCEPTED.equals(status))) {
            request.setStatus(Status.REJECTED);
            return saveRequest(request);
        } else {
            throw new StatusException(String.format("Reject failed. Can reject only request with status VERIFIED" +
                    " or ACCEPTED. Actual status: %s", status));
        }
    }

    @Override
    public Request publishRequest(Request request) {
        Status status = request.getStatus();
        if (status != null && Status.ACCEPTED.equals(status)) {
            request.setStatus(Status.PUBLISHED);
            request.setUuid(UUID.randomUUID());
            return saveRequest(request);
        } else {
            throw new StatusException(String.format("Publish failed. Can publish only request with status ACCEPTED." +
                    " Actual status: %s", status));
        }
    }

    private Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public Request deleteRequest(Request request) {
        Status status = request.getStatus();
        if (status == null || Status.CREATED.equals(status)) {
            request.setStatus(Status.DELETED);
            return saveRequest(request);
        } else {
            throw new StatusException(String.format("Delete failed. Can delete only request with status CREATED." +
                    " Actual status: %s", status));
        }
    }

    private void validateRequest(Request request) {
        if (request.getTitle() == null || request.getDescription() == null) {
            throw new NotValidException(String.format("Create request: title and description required. " +
                    "Provided title: %s, description: %s", request.getTitle(), request.getDescription()));
        }
    }
}
