package com.e_commerce.OAuth2_ecommerce.repo;

import com.e_commerce.OAuth2_ecommerce.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User > findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
