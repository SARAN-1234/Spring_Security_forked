package com.e_commerce.OAuth2_ecommerce.dto;

import com.e_commerce.OAuth2_ecommerce.auth.refreshToken.RefreshToken;
public class LoginResponseDto {

    private String message;

    public LoginResponseDto() {}

    public LoginResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}