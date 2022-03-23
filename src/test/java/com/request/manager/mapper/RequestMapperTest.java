package com.request.manager.mapper;

import com.request.manager.domain.Request;
import com.request.manager.dto.IncomingRequestDto;
import com.request.manager.dto.OutgoingRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequestMapperTest {

    private final RequestMapper requestMapper = new RequestMapper();

    @Test
    public void shouldMapIncomingRequestDtoToRequest() {
        //Given
        IncomingRequestDto incomingRequestDto = new IncomingRequestDto("testTitleDto", "testContentDto");

        //When
        Request request = requestMapper.mapIncomingRequestDtoToRequest(incomingRequestDto);

        //Then
        assertEquals(incomingRequestDto.getTitle(), request.getTitle());
        assertEquals(incomingRequestDto.getDescription(), request.getDescription());
    }

    @Test
    public void shouldMapRequestToOutgoingRequestDto() {
        //Given
        Request request = new Request("testTitleRequest", "testContentRequest");

        //When
        OutgoingRequestDto outgoingRequestDto = requestMapper.mapRequestToOutgoingRequestDto(request);

        //Then
        assertEquals(request.getTitle(), outgoingRequestDto.getTitle());
        assertEquals(request.getDescription(), outgoingRequestDto.getDescription());
    }
}