package com.request.manager.mapper;

import com.request.manager.domain.Request;
import com.request.manager.dto.IncomingRequestDto;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public Request mapIncomingRequestDtoToRequest(IncomingRequestDto incomingRequestDto) {
        return Request.builder()
                .title(incomingRequestDto.getTitle())
                .description(incomingRequestDto.getDescription())
                .build();
    }
}
