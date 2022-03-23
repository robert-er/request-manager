package com.request.manager.service;

import com.request.manager.domain.Request;
import org.springframework.stereotype.Service;

@Service
public interface RequestService {

    Request findById(Long id);
    Request createRequest(Request request);
    Request verifyRequest(Request request);
    Request acceptRequest(Request request);
    Request rejectRequest(Request request);
    Request publishRequest(Request request);
    Request deleteRequest(Request request);
}
