package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static Logger logger;

    public static List<Person> getPersons() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> persons = new ArrayList<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(new FileReader("persons.json"));
            for (JsonNode person : jsonNode) {
                LocalDate dateOfBirth = LocalDate.parse(person.get("dateOfBirth").asText(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                persons.add(new Person(person.get("name").asText(), dateOfBirth, person.get("email").asText()));
            }
            logger.log(Level.INFO, "JSON файл успешно считан");
            return persons;
        }
        catch (IllegalArgumentException | IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    public static List<Person> getFilteredPerson(List<Person> persons) {
        return persons.stream().filter(person -> person.getAge() > 18).toList();
    }

    public static void writePersonsToJSON(List<Person> filteredPersons) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode;
        ArrayNode arrayNode = objectMapper.createArrayNode();

        for (Person person : filteredPersons) {
            jsonNode = objectMapper.createObjectNode();
            jsonNode.put("name", person.getName());
            jsonNode.put("email", person.getEmail());
            arrayNode.add(jsonNode);
        }

        objectMapper.writeValue(new FileWriter("filteredPersons.json"), arrayNode);
        for (int i = 0; i < filteredPersons.size(); i++) {
            logger.log(Level.INFO, filteredPersons.get(i).getName() + " " + filteredPersons.get(i).getEmail());
        }
    }

    public static Double getAverageAge(List<Person> persons) {
        return persons.stream().mapToInt(Person::getAge).average().getAsDouble();
    }

    public static List<Person> isLeapDateOfBirth(List<Person> persons) {
        Predicate<Person> isLeapYear = person -> (((person.getDateOfBirth().getYear()) % 4 == 0)
                && ((person.getDateOfBirth().getYear()) % 100 != 0)) || ((person.getDateOfBirth().getYear()) % 400 == 0);
        return persons.stream().filter(isLeapYear).toList();
    }

    public static List<Person> groupByAge(List<Person> persons, Predicate<Person> filter) {
        return persons.stream().filter(filter).toList();
    }

    public static List<Integer> longSort() {
        List<Integer> listRandom = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000000; i++) {
            listRandom.add(random.nextInt(-10000000, 10000000));
        }
        listRandom.sort(Integer::compareTo);
        return listRandom;
    }

    public static void main(String[] args) throws IOException {
        logger = LoggerConfig.createLogger(Main.class);

        List<Person> persons = getPersons();
        Consumer<List<Person>> loggingListPersons = personsToLog -> personsToLog
                .forEach(person -> logger.log(Level.INFO, person.getName() + " "
                        + person.getDateOfBirth() + " " + person.getEmail()));


        // 3. Используя Stream API, отфильтруйте людей старше 18 лет и соберите их имена и email в новый список.
        // Отфильтрованные данные записывайте также в JSON-файл. Логируйте имена пользователей, которые прошли фильтр.
        List<Person> filteredPersons = getFilteredPerson(persons);
        writePersonsToJSON(filteredPersons);


        // 4. Определить средний возраст людей.
        double averageAge = getAverageAge(filteredPersons);
        logger.log(Level.INFO, Double.toString(averageAge));


        // 5. Выведите список людей, которые родились в високосный год.
        List<Person> filteredYearPersons = isLeapDateOfBirth(persons);
        loggingListPersons.accept(filteredYearPersons);


        // 6. Используя Stream API, сгруппируйте людей по возрастным группам (ребенок, молодежь, пожилой).
        List<Person> children = groupByAge(persons, person -> person.getAge() < 16);
        List<Person> youth = groupByAge(persons, person -> person.getAge() >= 16);
        List<Person> elderly = groupByAge(persons, person -> person.getAge() >= 50);

        loggingListPersons.accept(children);
        loggingListPersons.accept(youth);
        loggingListPersons.accept(elderly);


        // 9. Реализуйте метод, который выполняет длительную операцию (например, сортировку большого массива).
        // Измерьте время выполнения и залогируйте его с помощью логгера.
        long startTime = System.currentTimeMillis();
        List<Integer> list = longSort();
        long endTime = System.currentTimeMillis();
        logger.log(Level.INFO, ((endTime - startTime) / 1000) + " секунд");
    }
}