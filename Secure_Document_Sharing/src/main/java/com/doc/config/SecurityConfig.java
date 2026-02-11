package com.doc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig 
{
	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Value("${Remember_Me_Key}")
	private String RememberMeKey;
	
	

	@Bean
    PersistentTokenRepository persistentTokenRepo() {
        JdbcTokenRepositoryImpl tokenImpl = new JdbcTokenRepositoryImpl();
        tokenImpl.setDataSource(datasource);
        
        return tokenImpl;
    }

	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
	{
		
		http.csrf(csrf -> csrf.disable())
        .sessionManagement(session -> {
            session.sessionFixation().none();
        });
		
		
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(requests -> requests
				.requestMatchers("/","/swagger-ui","/userOperations/register",
						"/login","/register",
						"/get-username", "/Secure_Document_Sharing/uploads/**", "/uploads/**", 
						"/fetchDocumentDetails", "/docaccess/{token}", "/docaccess/verify",
						"/api/preview/{token}", "/download/{token}","/fetchSecureDoc",
						"/securityPolicy","/terms","/privacyPolicy","/adminDashboard",
						"/manageUser","/manageDocument","/manageSecurityLogs","/registerUserCount",
						"/uploadedDocument","/fetchActiveShares","/onlineUsers",
						
						
						
						
						
						"/css/**", "/js/**", 
                        "/images/**",  "/webjars/**", 
                        "/favicon.ico", "/*.png", 
                        "/*.jpg", "/*.jpeg", 
                        "/*.gif", "/*.svg",
                        "/*.pdf","/*.doc","/*.docx").permitAll()
				.anyRequest().authenticated()
				)
		
		.headers(headers -> headers
	            .frameOptions(frame -> frame.sameOrigin())  // allow iframe
	        )
		
		.formLogin(form -> {
            
            form.loginPage("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/postLogin", true)
            .failureUrl("/login?error=true")
            .permitAll();
        })
		
		.sessionManagement(session -> session
	            .maximumSessions(-1)  
	            .sessionRegistry(sessionRegistry())
	        )
		
		 .logout(logout -> logout
		            .logoutUrl("/logout")
		            .logoutSuccessUrl("/?logout=true")
		            .permitAll()
		        )
		
		
		.rememberMe(remember -> {
            
            remember
                .key(RememberMeKey)
                .tokenRepository(persistentTokenRepo())
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .userDetailsService(customUserDetailsService)
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("remember-me-cookie")
                .useSecureCookie(false); // Required for localhost
           
        });
		
		
		return http.build();
	}
	
	
	@Bean
	AuthenticationManager authManager(HttpSecurity http) throws Exception
	{
		 AuthenticationManager authManager =
	                http.getSharedObject(AuthenticationManagerBuilder.class)
	                        .userDetailsService(customUserDetailsService)
	                        .passwordEncoder(encoder())
	                        .and()
	                        .build();
		 
		 return authManager;
	}
	
	
	@Bean
	BCryptPasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	
	// sessionRegistry Implementation
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}



	@Bean
	public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
	    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}

	
	

}
