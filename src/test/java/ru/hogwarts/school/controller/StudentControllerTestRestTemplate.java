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
import ru.hogwarts.school.entity.Student;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void get_success() {
        Student studentForAdd = new Student(1L, "Гарри Поттер", 11);
        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForAdd, Student.class);
        Student actualStudentOpt = this.restTemplate.getForObject("http://localhost:"
                + port + "/student" + "?id=" + postedStudent.getId(), Student.class);
        assertEquals(postedStudent, actualStudentOpt);
    }

    @Test
    void add_success() {
        Student studentForAdd = new Student(1L,"Гарри Поттер", 11);
        Student expectedStudent = new Student(1L,"Гарри Поттер", 11);
        assertEquals(expectedStudent, this.restTemplate.postForObject("http://localhost:"
                + port + "/student", studentForAdd, Student.class));
    }

    @Test
    void update_success() {
        Student studentForUpdate = new Student(1L,"Гарри Поттер", 11);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Student> httpEntity = new HttpEntity<>(studentForUpdate, headers);

        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.PUT, httpEntity, Student.class);

        assertEquals(200, response.getStatusCodeValue());

        Student updateStudent = response.getBody();
        assertEquals("Гарри Поттер", updateStudent.getName());
        assertEquals(11, updateStudent.getAge());
    }

    @Test
    void delete_success() {
        Student studentForDelete = new Student(1L,"Гарри Поттер", 11);
        Student postedStudent = this.restTemplate.postForObject(
                "http://localhost:" + port + "/student", studentForDelete, Student.class);
        this.restTemplate.delete(
                "http://localhost:" + port + "/student" + "?id=" + postedStudent.getId());
        Optional<Student> studentOpt = this.restTemplate.getForObject(
                "http://localhost:" + port + "/student" + "?id=" + postedStudent.getId(), Optional.class);
        assertTrue(studentOpt.isEmpty());
    }

    @Test
    void findStudents_success() {
        int age = 11;
        HttpEntity<Void> httpEntity = new HttpEntity<>(null);
        ResponseEntity<Student[]> response = restTemplate.exchange("http://localhost:" + port + "/student?age=" + age,
                HttpMethod.GET, httpEntity, Student[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Student[] students = response.getBody();
        assertNotNull(students);
    }

    @Test
    void getFacultyByStudentId_success() {
        Long studentId = 1L;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                "http://localhost:" + port + "/student/faculty-by-student-id")
                .queryParam("id", studentId);
        ResponseEntity<Faculty> response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, null, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}