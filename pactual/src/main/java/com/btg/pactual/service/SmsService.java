package com.btg.pactual.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendSms(String to, String message) {
        System.out.println("Enviando SMS a " + to + ": " + message);
    }
}
