package com.request.manager.domain;

import com.request.manager.repository.HistoryEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HistoryEntryTest {

    private final Request request = Request.builder()
            .title("testTitle")
            .description("testContent")
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
    private HistoryEntryRepository historyEntryRepository;

    @Test
    public void shouldSaveHistoryEntry() {
        //Given + When
        historyEntryRepository.save(historyEntry);
        Long historyEntryId = historyEntry.getId();

        //Then
        assertTrue(historyEntryRepository.findById(historyEntryId).isPresent());

        //CleanUp
        historyEntryRepository.deleteById(historyEntryId);
    }
}