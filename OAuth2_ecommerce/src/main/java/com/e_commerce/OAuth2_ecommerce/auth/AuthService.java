package com.e_commerce.OAuth2_ecommerce.auth;

import com.e_commerce.OAuth2_ecommerce.auth.refreshToken.RefreshToken;
import com.e_commerce.OAuth2_ecommerce.auth.refreshToken.RefreshTokenService;
import com.e_commerce.OAuth2_ecommerce.dto.LoginDto;
import com.e_commerce.OAuth2_ecommerce.dto.LoginResponseDto;
import com.e_commerce.OAuth2_ecommerce.dto.RegistrationDto;
import com.e_commerce.OAuth2_ecommerce.repo.UserRepo;
import com.e_commerce.OAuth2_ecommerce.user.Role;
import com.e_commerce.OAuth2_ecommerce.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtService jwtService;
    public LoginResponseDto validate(LoginDto loginDto, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {

            String accesstoken = jwtService.getToken(loginDto.getEmail());

            String refreshtoken = refreshTokenService
                    .createRefreshToken(loginDto.getEmail())
                    .getToken();

            Cookie accessCookie = new Cookie("accessToken", accesstoken);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(false); // only HTTPS
            accessCookie.setPath("/");
            accessCookie.setMaxAge(15 * 60); // 15 minutes

            Cookie refreshCookie = new Cookie("refreshToken", refreshtoken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);

            return new LoginResponseDto("Login Successful");
        }

        throw new RuntimeException("Invalid credentials");
    }
    public String register(RegistrationDto registrationDto){
        if(userRepo.existsByEmail(registrationDto.getEmail())){
            return "User Email Already Registered";
        }
        if (userRepo.existsByUsername(registrationDto.getUsername())){
            return "User Name Already Taken";
        }

        User user=new User();
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPhoneNumber(registrationDto.getPh_no());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
        return "User Saved Succussfully";
    }

    public LoginResponseDto refreshToken(HttpServletRequest request,
                                         HttpServletResponse response) {

        String refreshTokenRequest = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenRequest = cookie.getValue();
                }
            }
        }

        if (refreshTokenRequest == null) {
            throw new RuntimeException("Refresh token not found");
        }

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(refreshTokenRequest);

        String email = refreshToken.getUser().getEmail();

        String newAccessToken = jwtService.getToken(email);

        Cookie accessCookie = new Cookie("accessToken", newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);

        response.addCookie(accessCookie);

        return new LoginResponseDto("Access token refreshed");
    }
}
