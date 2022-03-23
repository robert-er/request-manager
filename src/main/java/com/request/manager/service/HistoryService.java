package com.request.manager.service;

import com.request.manager.domain.History;
import com.request.manager.domain.HistoryEntry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryService {

    History createHistory(History history);
    void addEntryToHistory(Long requestId, HistoryEntry historyEntry);
    List<HistoryEntry> getHistory(Long requestId);
    History save(History history);
    History findByRequestId(Long requestId);

}
