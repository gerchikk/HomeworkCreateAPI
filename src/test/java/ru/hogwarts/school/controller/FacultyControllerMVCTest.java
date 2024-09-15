package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(FacultyController.class)
class FacultyControllerMVCTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private StudentService studentService;
    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void postGet_success() throws Exception {
        Long id = 1L;
        String name = "Гриффндор";
        String color = "Красно-золотой";
        JSONObject jsonFaculty = new JSONObject();
        jsonFaculty.put("id", id);
        jsonFaculty.put("name", name);
        jsonFaculty.put("color", color);
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(jsonFaculty.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void delete_success() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id))
                .andExpect(status().isOk());
        verify(facultyRepository, times(1)).deleteById(id);
    }

    @Test
    void getFaculties_success() throws Exception {
        Faculty faculty = new Faculty(1L, "Гриффиндор", "Красно-золотой");
        String color = "Красно-золотой";
        when(facultyService.getByColorIgnoreCase(anyString())).thenReturn(List.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .param("color", color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Non"))
                .andExpect(jsonPath("$[0].color").value("Red"));
        verify(facultyService).getByColorIgnoreCase(color);
    }

    @Test
    void getByColorOrNameIgnoreCase_success() throws Exception {
        Faculty faculty1 = new Faculty(1L, "Гриффиндор", "Красно-золотой");
        Faculty faculty2 = new Faculty(2L, "Слизерин", "Зеленый");
        String name= "Гриффиндор";
        String color = "Зеленый";
        when(facultyRepository.findByColorIgnoreCase(anyString())).thenReturn(List.of(faculty1));
        when(facultyRepository.findByNameIgnoreCase(anyString())).thenReturn(List.of(faculty2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/by-name-or-color")
                        .param("name", name)
                        .param("color", color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(faculty1.getName()))
                .andExpect(jsonPath("$[1].color").value(faculty2.getColor()));
        verify(facultyRepository, times(1)).findByColorIgnoreCase(color);
        verify(facultyRepository, times(1)).findByNameIgnoreCase(name);
    }

    @Test
    void getStudentsByFacultyId_success() throws Exception {
        Student student1 = new Student(1L, "Гарри Поттер", 11);
        Student student2 = new Student(2L, "Драко Малфой", 11);
        Faculty faculty = new Faculty(1L, "Гриффиндор", "Красно-золотой");
        student1.setFaculty(faculty);
        student2.setFaculty(faculty);
        List<Student> STUDENTS = new ArrayList<>(List.of(student1, student2));
        when(studentRepository.findByFacultyId(anyLong())).thenReturn(STUDENTS);
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/students-by-faculty-id")
                        .param("id", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].faculty.id").value(student1.getFaculty().getId()))
                .andExpect(jsonPath("$[1].faculty.name").value(student2.getFaculty().getName()));
    }
}