package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.security.oauth2.client.web.client.support.OAuth2RestClientHttpServiceGroupConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.registry.HttpServiceClient;

import java.security.Principal;
import java.util.Collection;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

 /*   @Bean
    RestClient restClient(OAuth2AuthorizedClientManager auth2AuthorizedClientManager,
                          RestClient.Builder builder) {
        return builder
                .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(auth2AuthorizedClientManager))
                .build();
    }*/

    @Bean
    OAuth2RestClientHttpServiceGroupConfigurer restClientHttpServiceGroupConfigurer(
            OAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
        return OAuth2RestClientHttpServiceGroupConfigurer.from(auth2AuthorizedClientManager);
    }
}

record Dog(int id, String owner, String name, String description) {
}

@Controller
@ResponseBody
class DogsClientController {

    private final DogsClient dogsClient;

    DogsClientController(DogsClient dogsClient) {
        this.dogsClient = dogsClient;
    }

    @GetMapping("/dogs-client")
    Collection<Dog> dogs(Principal principal) {
        System.out.println("looking up dogs for username [" + principal.getName() + ']');
        return dogsClient.dogs();
    }

}

@HttpServiceClient
interface DogsClient {

    @ClientRegistrationId("spring")
    @GetExchange("http://localhost:8081/dogs")
    Collection<Dog> dogs();
}

/*
@Component
class DogsClient {

    private final RestClient restClient;

    DogsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    Collection<Dog> dogs() {
        return this.restClient
                .get()
                .uri("http://localhost:8081/dogs")
                .attributes(clientRegistrationId("spring"))
                .retrieve()
                .body(new ParameterizedTypeReference<Collection<Dog>>() {
                });
    }
}

 */