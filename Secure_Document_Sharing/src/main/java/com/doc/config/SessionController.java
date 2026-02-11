package com.doc.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.doc.entity.User;
import com.doc.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class SessionController {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

   
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private SecurityConfig config;

    

    @GetMapping("/postLogin")
    public String postLogin(HttpSession session, Authentication authentication) {

        String username = authentication.getName();
        logger.info("Processing postLogin for username: {}", username);
    
        
        // check if username belongs to a user
        logger.debug("Checking User table for username: {}", username);
        Optional<User> user = userRepo.findByAuth_Username(username);

        if (user.isPresent()) {
            logger.info("Login successful as USER: {}", username);
            session.setAttribute("user", user.get());
            logger.debug("User object stored in session.");
            return "redirect:/dashboard";
        }

        logger.warn("No User or Maid found for username: {}", username);
        return "redirect:/";
    }
    
}
