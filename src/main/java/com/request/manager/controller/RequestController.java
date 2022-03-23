package com.request.manager.controller;

import com.request.manager.domain.Request;
import com.request.manager.dto.IncomingRequestDto;
import com.request.manager.dto.OutgoingRequestDto;
import com.request.manager.mapper.RequestMapper;
import com.request.manager.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("verify/{id}")
    public OutgoingRequestDto verifyRequest(@PathVariable Long id) {
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.verifyRequest(requestService.findById(id)));
    }

    @PutMapping("accept/{id}")
    public OutgoingRequestDto acceptRequest(@PathVariable Long id) {
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.acceptRequest(requestService.findById(id)));
    }

    @PutMapping("reject/{id}")
    public OutgoingRequestDto rejectRequest(@PathVariable Long id) {
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.rejectRequest(requestService.findById(id)));
    }

    @PutMapping("publish/{id}")
    public OutgoingRequestDto publishRequest(@PathVariable Long id) {
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.publishRequest(requestService.findById(id)));
    }

    @DeleteMapping("{id}")
    public void deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
    }
}
