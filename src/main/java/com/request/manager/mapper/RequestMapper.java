package com.request.manager.mapper;

import com.request.manager.domain.Request;
import com.request.manager.dto.IncomingRequestDto;
import com.request.manager.dto.OutgoingRequestDto;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public Request mapIncomingRequestDtoToRequest(IncomingRequestDto incomingRequestDto) {
        return Request.builder()
                .title(incomingRequestDto.getTitle())
                .description(incomingRequestDto.getDescription())
                .build();
    }

    public OutgoingRequestDto mapRequestToOutgoingRequestDto(Request request) {
        return OutgoingRequestDto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .uuid(request.getUuid())
                .status(request.getStatus())
                .details(request.getDetails())
                .build();
    }
}
