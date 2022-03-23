package com.request.manager.controller;

import com.request.manager.domain.Request;
import com.request.manager.dto.IncomingRequestDto;
import com.request.manager.mapper.RequestMapper;
import com.request.manager.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/request")
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public Request createRequest(@RequestBody IncomingRequestDto incomingRequestDto) {
        return requestService.createRequest(requestMapper.mapIncomingRequestDtoToRequest(incomingRequestDto));
    }
}
