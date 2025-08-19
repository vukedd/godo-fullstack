package com.app.godo.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    public void sendRegistrationRequestSuccessEmail(String username, String address);
}
