package com.e_commerce.OAuth2_ecommerce.auth.refreshToken;

import com.e_commerce.OAuth2_ecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
