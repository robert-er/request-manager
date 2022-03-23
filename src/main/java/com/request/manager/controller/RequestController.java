package com.request.manager.controller;

import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.dto.*;
import com.request.manager.exception.DetailsException;
import com.request.manager.mapper.RequestMapper;
import com.request.manager.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/request")
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @GetMapping("get/{id}")
    public OutgoingRequestDto getRequest(@PathVariable Long id) {
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.findById(id));
    }

    @PostMapping
    public Request createRequest(@RequestBody IncomingRequestDto incomingRequestDto) {
        return requestService.createRequest(requestMapper.mapIncomingRequestDtoToRequest(incomingRequestDto));
    }

    @PutMapping("verify/{id}")
    public OutgoingRequestDto verifyRequest(@PathVariable Long id,
                                            @RequestBody Optional<OptionalDescriptionDto> optionalDescriptionDto) {
        Request request = requestService.findById(id);

        if (optionalDescriptionDto.isPresent() &&
                !request.getDescription().equals(optionalDescriptionDto.get().getDescription().get())) {
            request = changeDescriptionIfNeeded(id, optionalDescriptionDto.get().getDescription());
        }
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.verifyRequest(request));
    }

    @PutMapping("accept/{id}")
    public OutgoingRequestDto acceptRequest(@PathVariable Long id,
                                            @RequestBody Optional<OptionalDescriptionDto> optionalDescriptionDto) {
        Request request = requestService.findById(id);

        if (optionalDescriptionDto.isPresent() &&
                !request.getDescription().equals(optionalDescriptionDto.get().getDescription().get())) {
            request = changeDescriptionIfNeeded(id, optionalDescriptionDto.get().getDescription());
        }
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.acceptRequest(request));
    }

    @PutMapping("reject/{id}")
    public OutgoingRequestDto rejectRequest(@PathVariable Long id,
                                            @RequestBody DetailsOptDescriptionDto detailsOptDescriptionDtoDto) {
        Request request = requestService.findById(id);

        if (Status.VERIFIED.equals(request.getStatus())) {
            if (detailsOptDescriptionDtoDto.hasDescription() &&
                    !request.getDescription().equals(detailsOptDescriptionDtoDto.getDescription().get())) {
                request = changeDescriptionIfNeeded(id, detailsOptDescriptionDtoDto.getDescription());
            }

            String details = detailsOptDescriptionDtoDto.getDetails();
            if (details != null) {
                request.setDetails(details);
            } else {
                throw new DetailsException("Reject request: details required.");
            }
        }

        if (Status.ACCEPTED.equals(request.getStatus())) {
            String details = detailsOptDescriptionDtoDto.getDetails();
            if (details != null) {
                request.setDetails(details);
            } else {
                throw new DetailsException("Reject request: details required.");
            }
        }

        return requestMapper.mapRequestToOutgoingRequestDto(requestService.rejectRequest(request));
    }

    @PutMapping("publish/{id}")
    public OutgoingRequestDto publishRequest(@PathVariable Long id) {
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.publishRequest(requestService.findById(id)));
    }

    @PutMapping("delete/{id}")
    public OutgoingRequestDto deleteRequest(@PathVariable Long id,
                                            @RequestBody DetailsOptDescriptionDto detailsOptDescriptionDto) {
        Request request = requestService.findById(id);

        if (detailsOptDescriptionDto.hasDescription() &&
                !request.getDescription().equals(detailsOptDescriptionDto.getDescription().get())) {
            request = changeDescriptionIfNeeded(id, detailsOptDescriptionDto.getDescription());
        }

        String details = detailsOptDescriptionDto.getDetails();
        if (details != null) {
            request.setDetails(details);
        } else {
            throw new DetailsException("Delete request: details required.");
        }

        return requestMapper.mapRequestToOutgoingRequestDto(requestService.deleteRequest(request));
    }

    private Request changeDescriptionIfNeeded(Long id, Optional<String> descriptionDto) {
        Request request = requestService.findById(id);
        descriptionDto.ifPresent(request::setDescription);
        return request;
    }
}
