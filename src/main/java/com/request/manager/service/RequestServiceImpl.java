package com.request.manager.service;

import com.request.manager.domain.Request;
import com.request.manager.domain.Status;
import com.request.manager.exception.NotValidException;
import com.request.manager.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public Request createRequest(Request request) {
        validateRequest(request);
        request.setStatus(Status.CREATED);
        return requestRepository.save(request);
    }

    @Override
    public void deleteRequest(Long id) {

    }

    @Override
    public Request verifyRequest(Request request) {
        return null;
    }

    @Override
    public Request acceptRequest(Request request) {
        return null;
    }

    @Override
    public Request rejectRequest(Request request) {
        return null;
    }

    @Override
    public Request publishRequest(Request request) {
        return null;
    }

    private void validateRequest(Request request) {
        if (request.getTitle() == null || request.getDescription() == null) {
            throw new NotValidException("In order to register a new request You have to provide " +
                    "at least title and description");
        }
    }
}
