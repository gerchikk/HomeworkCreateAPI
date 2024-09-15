package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(StudentController.class)
class StudentControllerMVCTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void postGet_success() throws Exception {
        Long id = 1L;
        String name = "Гарри Поттер";
        int age = 11;
        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void delete_success() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/student/{id}", id))
                .andExpect(status().isOk());
        verify(studentRepository,times(1)).deleteById(id);
    }

    @Test
    void findStudents_success() throws Exception {
        int age = 11;

        Student student1 = new Student(1L, "Гарри Поттер", 11);
        Student student2 = new Student(2L, "Драко Малфой", 11);;
        when(studentRepository.findByAge(age)).thenReturn(List.of(student1, student2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age")
                        .param("age", String.valueOf(age)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
        verify(studentRepository, times(1)).findByAge(age);
    }

    @Test
    void getFacultyByStudentId_success() throws Exception {
        Student student1 = new Student(1L, "Гарри Поттер", 11);
        Faculty faculty1 = new Faculty(1L,"Гриффиндор", "Красно-золотой");
        student1.setFaculty(faculty1);
        when(studentRepository.findByFacultyId(any(Long.class))).thenReturn(List.of(student1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/faculty-by-student-id")
                        .param("id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(faculty1.getName()))
                .andExpect(jsonPath("$.color").value(faculty1.getColor()));
    }
}