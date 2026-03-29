package com.e_commerce.OAuth2_ecommerce.auth.refreshToken;

import com.e_commerce.OAuth2_ecommerce.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
   private String token;




   private LocalDate expirationTime;
   @OneToOne
   @JoinColumn(name = "user_id", unique = true)
   private User user;

    public RefreshToken(long id, String token, User user, LocalDate expirationTime) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expirationTime = expirationTime;
    }

    public RefreshToken (){

   }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(LocalDate expirationTime) {
        this.expirationTime = expirationTime;
    }

}
