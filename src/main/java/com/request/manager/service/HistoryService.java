package com.request.manager.service;

import com.request.manager.domain.History;
import com.request.manager.domain.HistoryEntry;

import java.util.List;

public interface HistoryService {

    History createHistory(History history);
    void addEntryToHistory(Long userId, HistoryEntry historyEntry);
    List<HistoryEntry> getHistory(Long userId);
}
