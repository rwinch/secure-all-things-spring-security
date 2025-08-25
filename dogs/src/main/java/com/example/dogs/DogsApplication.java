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

@SpringBootApplication
public class DogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogsApplication.class, args);
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
    Collection<Dog> dogs(Principal principal) {
        System.out.println("looking up dogs for " + principal.getName() + ".");
        return this.repository.findByOwner(principal.getName());
    }
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {

    Collection<Dog> findByOwner(String owner);
}

record Dog(@Id int id, String name, String owner, String description) {
}
