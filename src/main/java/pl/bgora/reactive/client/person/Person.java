package pl.bgora.reactive.client.person;

import lombok.Data;

import java.util.List;

@Data
public class Person {

    private String id;

    private String firstName;

    private String lastName;

    List<Vacation> vacations;
}
