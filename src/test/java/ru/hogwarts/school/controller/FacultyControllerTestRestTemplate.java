package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Faculty;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRestTemplate {
    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void get_success() {
        Faculty facultyForAdd = new Faculty(1L, "Грифиндор", "Красно-золотой");
        Faculty postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForAdd, Faculty.class);
        Faculty actualFacultyOpt = this.restTemplate.getForObject("http://localhost:"
                + port + "/faculty" + "?id=" + postedFaculty.getId(), Faculty.class);
        assertEquals(postedFaculty, actualFacultyOpt);
    }

    @Test
    void add_success() {
        Faculty facultyForAdd = new Faculty(1L, "Грифиндор", "Красно-золотой");
        Faculty expectedFaculty = new Faculty(1L, "Грифиндор", "Красно-золотой");
        assertEquals(expectedFaculty, this.restTemplate.postForObject("http://localhost:"
                + port + "/faculty", facultyForAdd, Faculty.class));
    }

    @Test
    void update_success() {
        Faculty facultyForUpdate = new Faculty(1L, "Грифиндор", "Красно-золотой");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Faculty> httpEntity = new HttpEntity<>(facultyForUpdate, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, httpEntity, Faculty.class);

        assertEquals(200, response.getStatusCodeValue());

        Faculty updateFaculty = response.getBody();
        assertEquals("Грифиндор", updateFaculty.getName());
        assertEquals("Красно-золотой", updateFaculty.getColor());
    }

    @Test
    void delete_success() {
        Faculty facultyForDelete = new Faculty(1L, "Грифиндор", "Красно-золотой");
        Faculty postedFaculty = this.restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", facultyForDelete, Faculty.class);
        this.restTemplate.delete(
                "http://localhost:" + port + "/faculty" + "?id=" + postedFaculty.getId());
        Optional<Faculty> facultyOpt = this.restTemplate.getForObject(
                "http://localhost:" + port + "/faculty" + "?id=" + postedFaculty.getId(), Optional.class);
        assertTrue(facultyOpt.isEmpty());
    }

    @Test
    void getByColorOrNameIgnoreCase_success() {
        String name = "Грифиндор";
        String color = "Красно-золотой";
        HttpEntity<Void> httpEntity = new HttpEntity<>(null);
        ResponseEntity<Faculty[]> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/by-name-or-color?name=" + name + "&color=" + color,
                HttpMethod.GET, httpEntity, Faculty[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getStudentsByFacultyId_success() {
        Long facultyId = 1L;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                        "http://localhost:" + port + "/faculty/students-by-faculty-id")
                .queryParam("id", facultyId);
        ResponseEntity<Faculty> response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, null, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}