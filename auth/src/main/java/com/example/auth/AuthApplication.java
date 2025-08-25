package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;

import javax.sql.DataSource;

import static org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer.authorizationServer;

@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .with(authorizationServer(), as -> as.oidc(Customizer.withDefaults()))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .webAuthn(w -> w
                    .allowedOrigins("http://localhost:8080")
                    .rpId("localhost")
                    .rpName("Bootiful")
                )
                .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                .oneTimeTokenLogin(ott -> ott.tokenGenerationSuccessHandler((OneTimeTokenGenerationSuccessHandler) (request, response, oneTimeToken) -> {
                    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                    response.getWriter().println("you've got console mail!");
                    var url = "http://localhost:8080/login/ott?token=" + oneTimeToken.getTokenValue();
                    System.out.println(url);
                }))
                .build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        var u = new JdbcUserDetailsManager(dataSource);
        u.setEnableUpdatePassword(true);
        return u;
    }

/*    @Bean
    InMemoryUserDetailsManager userDetailsManager(PasswordEncoder pw) {
        return new InMemoryUserDetailsManager(
                User.withUsername("rob").roles("USER", "ADMIN").password(pw.encode("pw")).build(),
                User.withUsername("josh").roles("USER").password(pw.encode("pw")).build()
        );
    }*/

}

