package com.bmt.HolaMundo.Impl;

import com.bmt.HolaMundo.Service.RecaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service 
public class RecaptchaServiceImp implements RecaptchaService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecaptchaServiceImp.class);
    
    @Value("${recaptcha.secret.key}")
    private String recaptchaSecret;
    
    @Value("${recaptcha.site.key}")
    private String recaptchaSiteKey;
    
    @Value("${recaptcha.url}")
    private String recaptchaUrl;
    
    private final RestTemplate restTemplate;
    
    public RecaptchaServiceImp() {
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public boolean validateRecaptcha(String captchaResponse) {
        // Validación básica del captcha response
        if (captchaResponse == null || captchaResponse.trim().isEmpty()) {
            logger.warn("reCAPTCHA v2 response está vacío");
            return false;
        }
        
        // Para v2, el response debe tener un formato específico
        if (captchaResponse.length() < 20) {
            logger.warn("reCAPTCHA v2 response parece inválido (muy corto)");
            return false;
        }
        
        try {
            // Preparar los headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            // Preparar el body (form-urlencoded)
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("secret", recaptchaSecret);
            body.add("response", captchaResponse);
            
            // Para v2, también podemos enviar la IP del usuario (opcional)
            // body.add("remoteip", remoteIp);
            
            HttpEntity<MultiValueMap<String, String>> request = 
                new HttpEntity<>(body, headers);
            
            logger.debug("Enviando validación de reCAPTCHA v2 a Google...");
            
            // Enviar la solicitud
            RecaptchaV2Response apiResponse = restTemplate.postForObject(
                recaptchaUrl, 
                request, 
                RecaptchaV2Response.class
            );
            
            if (apiResponse == null) {
                logger.error("No se recibió respuesta de la API de reCAPTCHA v2");
                return false;
            }
            
            boolean isValid = Boolean.TRUE.equals(apiResponse.success);
            
            // Log para depuración
            if (isValid) {
                logger.debug("reCAPTCHA v2 validado exitosamente. Hostname: {}", 
                    apiResponse.hostname);
            } else {
                if (apiResponse.errorCodes != null && apiResponse.errorCodes.length > 0) {
                    logger.warn("reCAPTCHA v2 falló. Errores: {}", 
                        String.join(", ", apiResponse.errorCodes));
                } else {
                    logger.warn("reCAPTCHA v2 falló sin códigos de error específicos");
                }
            }
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("Error validando reCAPTCHA v2: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public String getSiteKey() {
        // Validar que la site key esté configurada
        if (recaptchaSiteKey == null || recaptchaSiteKey.trim().isEmpty()) {
            logger.error("reCAPTCHA v2 site key no configurada en application.properties");
            throw new IllegalStateException("reCAPTCHA site key no configurada");
        }
        
        // Para v2, devolvemos la site key normal
        return recaptchaSiteKey;
    }
    
    // Método para verificar si está configurado (útil para logs)
    public boolean isConfigured() {
        boolean hasSecret = recaptchaSecret != null && !recaptchaSecret.trim().isEmpty();
        boolean hasSiteKey = recaptchaSiteKey != null && !recaptchaSiteKey.trim().isEmpty();
        
        if (!hasSecret || !hasSiteKey) {
            logger.warn("reCAPTCHA v2 no está completamente configurado. " +
                       "Secret: {}, SiteKey: {}", 
                       hasSecret ? "OK" : "FALTANTE", 
                       hasSiteKey ? "OK" : "FALTANTE");
            return false;
        }
        
        return true;
    }
    
    // Clase específica para la respuesta de reCAPTCHA v2
    private static class RecaptchaV2Response {
        @JsonProperty("success")
        private Boolean success;
        
        @JsonProperty("challenge_ts")
        private String challengeTs; // timestamp del desafío
        
        @JsonProperty("hostname")
        private String hostname; // hostname del sitio
        
        @JsonProperty("error-codes")
        private String[] errorCodes;
        
        // Getters (no necesitamos setters para el mapeo JSON)
        public Boolean getSuccess() {
            return success;
        }
        
        public String getChallengeTs() {
            return challengeTs;
        }
        
        public String getHostname() {
            return hostname;
        }
        
        public String[] getErrorCodes() {
            return errorCodes;
        }
    }
}