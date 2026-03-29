package com.e_commerce.OAuth2_ecommerce.auth;

import com.e_commerce.OAuth2_ecommerce.dto.LoginDto;
import com.e_commerce.OAuth2_ecommerce.dto.LoginResponseDto;
import com.e_commerce.OAuth2_ecommerce.dto.RegistrationDto;
import jakarta.persistence.GeneratedValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginDto loginDto,
            HttpServletResponse response) {
        LoginResponseDto loginResponse = authService.validate(loginDto, response);
        return ResponseEntity.ok(loginResponse);
    }
    @PostMapping("/register")
    public String register(@RequestBody RegistrationDto registrationDto){
        return authService.register(registrationDto);
    }

    @PostMapping("/refresh")
    public LoginResponseDto refresh(HttpServletRequest request,
                                    HttpServletResponse response) {

        return authService.refreshToken(request, response);
    }
    @GetMapping("/welcome")
    public String wel(){
        return "welcome";
    }
}
