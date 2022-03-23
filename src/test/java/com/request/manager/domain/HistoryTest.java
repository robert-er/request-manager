package com.request.manager.domain;

import com.request.manager.repository.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HistoryTest {

    private final Request request = Request.builder()
            .title("testTitle")
            .description("testContent")
            .build();

    private final History history = History.builder()
            .request(request).build();

    @Autowired
    private HistoryRepository historyRepository;

    @Test
    public void shouldSaveHistory() {
        //Given + When
        historyRepository.save(history);
        Long historyId = history.getId();

        //Then
        assertTrue(historyRepository.findById(historyId).isPresent());

        //CleanUp
        historyRepository.deleteById(historyId);
    }

    @Test
    public void shouldFindByRequestId() {
        //Given
        historyRepository.save(history);
        Long historyId = history.getId();

        //When
        Optional<History> gotHistory = historyRepository.findByRequestId(request.getId());

        //Then
        assertTrue(gotHistory.isPresent());

        //CleanUp
        historyRepository.deleteById(historyId);
    }
}