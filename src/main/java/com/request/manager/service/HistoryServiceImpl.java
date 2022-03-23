package com.request.manager.service;

import com.request.manager.domain.History;
import com.request.manager.domain.HistoryEntry;
import com.request.manager.exception.NotFoundException;
import com.request.manager.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Override
    public History createHistory(History history) {
        return historyRepository.findByRequestId(history.getRequest().getId()).orElseGet(() -> save(history));
    }

    @Override
    public void addEntryToHistory(Long requestId, HistoryEntry historyEntry) throws NotFoundException {
        History history = historyRepository.findByRequestId(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("No history for request id: %s", requestId)));
        history.getEntries().add(historyEntry);
        save(history);
    }

    @Override
    public List<HistoryEntry> getHistory(Long requestId) throws NotFoundException {
        return historyRepository.findByRequestId(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("No history for request id: %s", requestId)))
                .getEntries();
    }

    @Override
    public History save(History history) {
        return historyRepository.save(history);
    }

    @Override
    public History findByRequestId(Long requestId) {
        return historyRepository.findByRequestId(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("No history for request id: %s", requestId)));
    }
}
