package com.request.manager.controller;

import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.dto.*;
import com.request.manager.exception.DetailsException;
import com.request.manager.mapper.RequestMapper;
import com.request.manager.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/request")
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;
    private final SessionFactory sessionFactory;

    private static final int PAGINATION = 2;

    @GetMapping("get/{id}")
    public OutgoingRequestDto getRequest(@PathVariable Long id) {
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.findById(id));
    }

    @GetMapping("page/{page}")
    public List<OutgoingRequestDto> getPageRequest(@PathVariable int page,
                                                   @RequestParam String title) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("From Request");
        query.setFirstResult(page * PAGINATION);
        query.setMaxResults(PAGINATION);
        List<Request> requests = query.list();
        List<OutgoingRequestDto> requestDtos= new ArrayList<>();
        for (Request request : requests) {
            requestDtos.add(requestMapper.mapRequestToOutgoingRequestDto(request));
        }
        return requestDtos;
    }

    @GetMapping("page/{page}/bytitle")
    public List<OutgoingRequestDto> getPageRequestByTitle(@PathVariable int page, @RequestParam String title) {
        Session session = sessionFactory.openSession();
        String hql = "From Request r where title = '" + title + "' order by r.title";
        Query query = session.createQuery(hql);
        query.setFirstResult(page * PAGINATION);
        query.setMaxResults(PAGINATION);
        List<Request> requests = query.list();
        List<OutgoingRequestDto> requestDtos= new ArrayList<>();
        for (Request request : requests) {
            requestDtos.add(requestMapper.mapRequestToOutgoingRequestDto(request));
        }
        return requestDtos;
    }

    @GetMapping("page/{page}/bystatus")
    public List<OutgoingRequestDto> getPageRequestByStatus(@PathVariable int page, @RequestParam String status) {
        Session session = sessionFactory.openSession();
        String hql = "From Request r where status = '" + status + "' order by r.status";
        Query query = session.createQuery(hql);
        query.setFirstResult(page * PAGINATION);
        query.setMaxResults(PAGINATION);
        List<Request> requests = query.list();
        List<OutgoingRequestDto> requestDtos= new ArrayList<>();
        for (Request request : requests) {
            requestDtos.add(requestMapper.mapRequestToOutgoingRequestDto(request));
        }
        return requestDtos;
    }

    @PostMapping
    public Request createRequest(@RequestBody IncomingRequestDto incomingRequestDto) {
        return requestService.createRequest(requestMapper.mapIncomingRequestDtoToRequest(incomingRequestDto));
    }

    @PutMapping("verify/{id}")
    public OutgoingRequestDto verifyRequest(@PathVariable Long id,
                                            @RequestBody Optional<OptionalDescriptionDto> optionalDescriptionDto) {
        Request request = requestService.findById(id);

        request = updateDescription(request, optionalDescriptionDto.get().getDescription());
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.verifyRequest(request));
    }



    @PutMapping("accept/{id}")
    public OutgoingRequestDto acceptRequest(@PathVariable Long id,
                                            @RequestBody Optional<OptionalDescriptionDto> optionalDescriptionDto) {
        Request request = requestService.findById(id);

        Optional<String> description = Optional.empty();

        if (optionalDescriptionDto.isPresent()) {
            description = optionalDescriptionDto.get().getDescription();
        }
        request = updateDescription(request, description);
        return requestMapper.mapRequestToOutgoingRequestDto(requestService.acceptRequest(request));
    }

    @PutMapping("reject/{id}")
    public OutgoingRequestDto rejectRequest(@PathVariable Long id,
                                            @RequestBody DetailsOptDescriptionDto detailsOptDescriptionDtoDto) {
        Request request = requestService.findById(id);

        if (Status.VERIFIED.equals(request.getStatus())) {
            request = updateDescription(request, detailsOptDescriptionDtoDto.getDescription());

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

        request = updateDescription(request, detailsOptDescriptionDto.getDescription());

        String details = detailsOptDescriptionDto.getDetails();
        if (details != null) {
            request.setDetails(details);
        } else {
            throw new DetailsException("Delete request: details required.");
        }

        return requestMapper.mapRequestToOutgoingRequestDto(requestService.deleteRequest(request));
    }

    private Request updateDescription(Request request, Optional<String> description) {
        if (description != null && description.isPresent() &&
                !request.getDescription().equals(description.get())) {
            request.setDescription(description.get());
        }
        return request;
    }
}
