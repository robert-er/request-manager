package com.request.manager.repository;

import com.request.manager.domain.HistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface HistoryEntryRepository extends JpaRepository<HistoryEntry, Long> {

    @Override
    <S extends HistoryEntry> S save(S historyEntry);

    @Override
    Optional<HistoryEntry> findById(Long id);

    @Override
    void deleteById(Long id);
}
