package com.request.manager.service;

import com.request.manager.domain.HistoryEntry;
import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.repository.HistoryEntryRepository;
import com.request.manager.repository.HistoryRepository;
import com.request.manager.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RequestServiceImplTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryEntryRepository historyEntryRepository;

    private final Request request = Request.builder()
            .title("requestTestTitle")
            .description("requestTestContent")
            .build();

    @Test
    public void shouldFindById() {
        //Given
        requestRepository.save(request);
        Long id = request.getId();

        //When
        Request gotRequest = requestService.findById(id);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(id, gotRequest.getId());

        //CleanUp
        requestRepository.deleteById(id);
    }

    @Test
    public void shouldCreateRequest() {
        //Given + When
        Request gotRequest = requestService.createRequest(request);
        Long requestId = gotRequest.getId();
        Long historyId = historyService.findByRequestId(requestId).getId();
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(Status.CREATED, gotRequest.getStatus());

        //CleanUp
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyId);
    }

    @Test
    public void shouldVerifyRequest() {
        //Given
        Request createdRequest = requestService.createRequest(request);

        //When
        Request gotRequest = requestService.verifyRequest(createdRequest);
        Long requestId = gotRequest.getId();
        Long historyId = historyService.findByRequestId(requestId).getId();
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(Status.VERIFIED, gotRequest.getStatus());

        //CleanUp
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyId);
    }

    @Test
    public void shouldAcceptRequest() {
        //Given
        Request createdRequest = requestService.createRequest(request);
        Request verifiedRequest = requestService.verifyRequest(createdRequest);

        //When
        Request gotRequest = requestService.acceptRequest(verifiedRequest);
        Long requestId = gotRequest.getId();
        Long historyId = historyService.findByRequestId(requestId).getId();
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(Status.ACCEPTED, gotRequest.getStatus());

        //CleanUp
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyId);
    }

    @Test
    public void shouldRejectVerifiedRequest() {
        //Given
        Request createdRequest = requestService.createRequest(request);
        Request verifiedRequest = requestService.verifyRequest(createdRequest);

        //When
        Request gotRequest = requestService.rejectRequest(verifiedRequest);
        Long requestId = gotRequest.getId();
        Long historyId = historyService.findByRequestId(requestId).getId();
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(Status.REJECTED, gotRequest.getStatus());

        //CleanUp
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyId);
    }

    @Test
    public void shouldRejectAcceptedRequest() {
        //Given
        Request createdRequest = requestService.createRequest(request);
        Request verifiedRequest = requestService.verifyRequest(createdRequest);
        Request acceptedRequest = requestService.acceptRequest(verifiedRequest);

        //When
        Request gotRequest = requestService.rejectRequest(acceptedRequest);
        Long requestId = gotRequest.getId();
        Long historyId = historyService.findByRequestId(requestId).getId();
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(Status.REJECTED, gotRequest.getStatus());

        //CleanUp
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyId);
    }

    @Test
    public void shouldPublishRequest() {
        //Given
        Request createdRequest = requestService.createRequest(request);
        Request verifiedRequest = requestService.verifyRequest(createdRequest);
        Request acceptedRequest = requestService.acceptRequest(verifiedRequest);

        //When
        Request gotRequest = requestService.publishRequest(acceptedRequest);
        Long requestId = gotRequest.getId();
        Long historyId = historyService.findByRequestId(requestId).getId();
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(Status.PUBLISHED, gotRequest.getStatus());

        //CleanUp
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyId);
    }

    @Test
    public void shouldDeleteRequest() {
        //Given
        Request createdRequest = requestService.createRequest(request);

        //When
        Request gotRequest = requestService.deleteRequest(createdRequest);
        Long requestId = gotRequest.getId();
        Long historyId = historyService.findByRequestId(requestId).getId();
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);

        //Then
        assertEquals(request.getTitle(), gotRequest.getTitle());
        assertEquals(request.getDescription(), gotRequest.getDescription());
        assertEquals(Status.DELETED, gotRequest.getStatus());

        //CleanUp
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyId);
    }
}