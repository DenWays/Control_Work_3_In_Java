package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Person {
    private String name;
    private LocalDate dateOfBirth;
    private String email;

    public Person() {
        this("noname", LocalDate.parse("01.01.1000", DateTimeFormatter.ofPattern("dd.MM.yyyy")), "example@gmail.com");
    }

    public Person(String name, LocalDate dateOfBirth, String email) {
        if(!email.matches("^[A-Za-z0-9._%+-]+@[a-z]+.[a-z]{2,4}$")) {
            throw new IllegalArgumentException("Некорректный email");
        }
        if(dateOfBirth.isAfter(LocalDate.now()) || dateOfBirth.isBefore(LocalDate.now().minusYears(105))
                || !dateOfBirth.toString().matches("^[0-9]{4}.[0-9]{2}.[0-9]{2}$")) {
            throw new IllegalArgumentException("Некорректная дата рождения");
        }

        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if(dateOfBirth.isAfter(LocalDate.now()) || dateOfBirth.isBefore(LocalDate.now().minusYears(105))
                || !dateOfBirth.toString().matches("^[0-9]{4}.[0-9]{2}.[0-9]{2}$")) {
            throw new IllegalArgumentException("Некорректная дата рождения");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(!email.matches("^[A-Za-z0-9._%+-]+@[a-z]+.[a-z]{2,4}$")) {
            throw new IllegalArgumentException("Некорректный email");
        }
        this.email = email;
    }

    public int getAge() {
        dateOfBirth.
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
}
