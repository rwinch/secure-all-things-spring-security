package com.example.dogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@SpringBootApplication
public class DogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogsApplication.class, args);
    }

}


@Controller
@ResponseBody
class HelloController {

    @GetMapping("/me")
    Map<String, String> hello(Principal principal) {
        return Map.of("name", principal.getName());
    }

}

@Controller
@ResponseBody
class DogController {

    private final DogRepository repository;

    DogController(DogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/dogs")
    Collection<Dog> dogs() {
        return this.repository.findAll();
    }

}

interface DogRepository extends ListCrudRepository<Dog, Integer> {
}

record Dog(@Id int id, String name, String description, String owner) {
}