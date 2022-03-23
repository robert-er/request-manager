package com.request.manager.service;

import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.exception.NotFoundException;
import com.request.manager.exception.NotValidException;
import com.request.manager.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public Request createRequest(Request request) {
        validateRequest(request);
        request.setStatus(Status.CREATED);
        return saveRequest(request);
    }

    @Override
    public Request findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id: %s does not exist", id)));
    }

    @Override
    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public Request verifyRequest(Request request) {
        request.setStatus(Status.VERIFIED);
        return saveRequest(request);
    }

    @Override
    public Request acceptRequest(Request request) {
        request.setStatus(Status.ACCEPTED);
        return saveRequest(request);
    }

    @Override
    public Request rejectRequest(Request request) {
        request.setStatus(Status.REJECTED);
        return saveRequest(request);
    }

    @Override
    public Request publishRequest(Request request) {
        request.setStatus(Status.PUBLISHED);
        request.setUuid(UUID.randomUUID());
        return saveRequest(request);
    }

    private Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    private void validateRequest(Request request) {
        if (request.getTitle() == null || request.getDescription() == null) {
            throw new NotValidException("In order to register a new request You have to provide " +
                    "at least title and description");
        }
    }
}
