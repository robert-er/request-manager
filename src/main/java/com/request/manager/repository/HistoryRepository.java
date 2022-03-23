package com.request.manager.repository;

import com.request.manager.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface HistoryRepository extends JpaRepository<History, Long> {
}
