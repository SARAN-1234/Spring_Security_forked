package com.e_commerce.OAuth2_ecommerce.auth.refreshToken;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Welcome {
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to secured site";
    }
}
