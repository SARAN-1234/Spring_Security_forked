package com.e_commerce.OAuth2_ecommerce.auth.refreshToken;

import com.e_commerce.OAuth2_ecommerce.repo.UserRepo;
import com.e_commerce.OAuth2_ecommerce.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private UserRepo userRepo;
    @Transactional
    public RefreshToken createRefreshToken(String email) {

       User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // remove old refresh token
        refreshTokenRepo.deleteByUser(user);
        refreshTokenRepo.flush();

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpirationTime(LocalDate.now().plusDays(7));

        return refreshTokenRepo.save(refreshToken);
    }
    @Transactional
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpirationTime().isBefore(LocalDate.now())) {
            refreshTokenRepo.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }
        return refreshToken;
    }
}
