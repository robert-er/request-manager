package com.request.manager.domain;

import com.request.manager.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RequestTest {

    private final Request request = Request.builder()
            .title("requestTestTitle")
            .description("requestTestContent")
            .build();

    @Autowired
    private RequestRepository requestRepository;

    @Test
    public void shouldSaveRequest() {
        //Given + When
        requestRepository.save(request);
        String title = request.getTitle();
        Long id = request.getId();

        //Then
        assertTrue(requestRepository.findById(id).isPresent());

        //CleanUp
        requestRepository.deleteById(id);
    }

    @Test
    public void shouldGetTitle() {
        //Given
        String title = "Test";
        request.setTitle(title);
        requestRepository.save(request);
        Long id = request.getId();

        //When
        String gotTitle = request.getTitle();

        //Then
        assertEquals(title, gotTitle);

        //CleanUp
        requestRepository.deleteById(id);
    }

    @Test
    public void shouldGetDescription() {
        //Given
        String description = "Test";
        request.setDescription(description);
        requestRepository.save(request);
        Long id = request.getId();

        //When
        String gotDescription = request.getDescription();

        //Then
        assertEquals(description, gotDescription);

        //CleanUp
        requestRepository.deleteById(id);
    }
}