package com.request.manager.repository;

import com.request.manager.domain.HistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface HistoryEntryRepository extends JpaRepository<HistoryEntry, Long> {
}
