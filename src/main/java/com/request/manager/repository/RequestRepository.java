package com.request.manager.repository;

import com.request.manager.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Override
    <S extends Request> S save(S request);

    @Override
    Optional<Request> findById(Long id);

    @Override
    void deleteById(Long id);
}
