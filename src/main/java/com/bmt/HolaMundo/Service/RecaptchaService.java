package com.bmt.HolaMundo.Service;

public interface RecaptchaService {
    boolean validateRecaptcha(String captchaResponse);
    String getSiteKey();
}