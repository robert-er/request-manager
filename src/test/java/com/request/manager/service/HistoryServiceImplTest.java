package com.request.manager.service;

import com.request.manager.domain.History;
import com.request.manager.domain.HistoryEntry;
import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.repository.HistoryEntryRepository;
import com.request.manager.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HistoryServiceImplTest {

    private final Request request = Request.builder()
            .title("historyServiceTestTitle")
            .description("historyServiceTestContent")
            .build();

    private final History history = History.builder()
            .request(request).build();

    private final HistoryEntry historyEntry = HistoryEntry.builder()
            .history(history)
            .date(LocalDateTime.now())
            .incomingStatus(Status.CREATED)
            .outgoingStatus(Status.VERIFIED)
            .details("testDetail")
            .build();

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryEntryRepository historyEntryRepository;

    @Autowired
    private RequestService requestService;

    @Test
    public void shouldCreateHistory() {
        //Given
        Request createdRequest = requestService.createRequest(request);
        Long requestId = createdRequest.getId();
        History history = historyRepository.findByRequestId(requestId).get();

        //When
        History gotHistory = historyService.createHistory(history);
        Long id = gotHistory.getId();

        //Then
        assertEquals(history.getRequest(), gotHistory.getRequest());
        assertEquals(history.getId(), id);

        //CleanUp
        historyRepository.deleteById(id);
    }

    @Test
    public void shouldAddEntryToHistory() {
        //Given
        Request createdRequest = requestService.createRequest(request);
        Long requestId = createdRequest.getId();

        //When
        historyService.addEntryToHistory(requestId, historyEntry);

        //Then
        assertFalse(historyService.getHistory(requestId).isEmpty());

        //CleanUp
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyRepository.findByRequestId(requestId).get().getId());
    }

    @Test
    public void shouldGetHistory() {
        //Given
        Request createdRequest = requestService.createRequest(request);
        Long requestId = createdRequest.getId();
        historyService.addEntryToHistory(requestId, historyEntry);

        //When
        List<HistoryEntry> gotHistoryEntries = historyService.getHistory(requestId);

        //Then
        assertFalse(gotHistoryEntries.isEmpty());

        //CleanUp
        List<HistoryEntry> historyEntries = historyService.getHistory(requestId);
        for (HistoryEntry historyEntry : historyEntries) {
            historyEntryRepository.deleteById(historyEntry.getId());
        }
        historyRepository.deleteById(historyRepository.findByRequestId(requestId).get().getId());
    }
}