package com.request.manager.service;

import com.request.manager.domain.Request;
import org.springframework.stereotype.Service;

@Service
public interface RequestService {

    Request createRequest(Request request);
    Request findById(Long id);
    void deleteRequest(Long id);
    Request verifyRequest(Request request);
    Request acceptRequest(Request request);
    Request rejectRequest(Request request);
    Request publishRequest(Request request);
}
