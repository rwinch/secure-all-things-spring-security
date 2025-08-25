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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.Map;

import static org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer.authorizationServer;

@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .with(authorizationServer(), as -> as.oidc(Customizer.withDefaults()))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .webAuthn(wa -> wa
                        .rpName("bootiful")
                        .rpId("localhost")
                        .allowedOrigins("http://localhost:8080")
                )
                .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                .oneTimeTokenLogin(configurer -> configurer
                        .tokenGenerationSuccessHandler((request, response, oneTimeToken) -> {
                            response.getWriter().println("you've got console mail!");
                            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                            System.out.println("go to http://localhost:8080/login/ott?token=" + oneTimeToken.getTokenValue());
                        }))
                .build();
    }

    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        var u = new JdbcUserDetailsManager(dataSource);
        u.setEnableUpdatePassword(true);
        return u;
    }

    /*   @Bean
       InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder pw) {
           return new InMemoryUserDetailsManager(
                   User.withUsername("josh").roles("USER").password(pw.encode("pw")).build(),
                   User.withUsername("rob").roles("USER", "ADMIN").password(pw.encode("pw")).build()
           );
       }

   */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

@Controller
@ResponseBody
class HelloController {

    @GetMapping("/")
    Map<String, String> me(Principal principal) {
        return Map.of("name", principal.getName());
    }
}