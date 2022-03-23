package com.request.manager.repository;

import com.request.manager.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface HistoryRepository extends JpaRepository<History, Long> {

    @Override
    <S extends History> S save(S history);

    Optional<History> findByRequestId(Long id);
}
