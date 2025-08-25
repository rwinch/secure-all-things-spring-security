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

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }


    //
//    @Bean
//    RestClient restClient(
//            RestClient.Builder builder,
//            OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager
//    ) {
//        var ochri = new OAuth2ClientHttpRequestInterceptor(oAuth2AuthorizedClientManager);
//        return builder
//                .requestInterceptor(ochri)
//                .build();
//    }
//
    @Bean
    OAuth2RestClientHttpServiceGroupConfigurer securityConfigurer(
            OAuth2AuthorizedClientManager manager) {
        return OAuth2RestClientHttpServiceGroupConfigurer.from(manager);
    }
}

record Me(String name) {
}

@HttpServiceClient
interface DogsClient {

    @ClientRegistrationId("spring")
    @GetExchange("http://localhost:8081/me")
    Me me();

    @ClientRegistrationId("spring")
    @GetExchange("http://localhost:8081/dogs")
    Collection<Dog> dogs();
}

@Controller
@ResponseBody
class ClientController {

//    private final RestClient http;

    private final Consumer<Map<String, Object>> spring = clientRegistrationId("spring");

    private final DogsClient dogsClient;

    ClientController(/*RestClient http,*/ DogsClient dogsClient) {
//        this.http = http;
        this.dogsClient = dogsClient;
    }

    @GetMapping("/me")
    Me me() {
        return this.dogsClient.me();
     /*   return this.http
                .get()
                .uri("http://localhost:8081/me")
                .attributes(this.spring)
                .retrieve()
                .body(String.class);*/


    }

    @GetMapping("/dogs")
    Collection<Dog> dogs() {
        return this.dogsClient.dogs();

        /*this.http
                .get()//
                .uri("http://localhost:8081/dogs")//
                .attributes(this.spring) //
                .retrieve() //
                .body(new ParameterizedTypeReference<Collection<Dog>>() {
                });*/
    }


}

record Dog(int id, String name, String owner, String description) {
}