package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @GetMapping("{id}")
    public Faculty get(@PathVariable("id") Long id) {
        return facultyService.get(id);
    }
    @PostMapping
    public Faculty add(@RequestBody Faculty faculty) {
        return facultyService.add(faculty);
    }
    @PutMapping("{id}")
    public Faculty update(@PathVariable("id") Long id,
                          @RequestParam String name,
                          @RequestParam String color) {
        return facultyService.update(id, name, color);
    }
    @DeleteMapping("{id}")
    void delete(@PathVariable("id") Long id) {
        facultyService.delete(id);
    }
    @GetMapping
    public List<Faculty> getFaculties(@RequestParam (required = false) String color) {
        if (color != null && !color.isBlank()) {
            return facultyService.getByColorIgnoreCase(color);
        }
        return facultyService.getAllFaculties();
    }
    @GetMapping("/by-name-or-color")
    public Set<Faculty> getByColorOrNameIgnoreCase(@RequestParam String param) {
        return facultyService.getByColorOrNameIgnoreCase(param);
    }

    @GetMapping("/students-by-faculty-id")
    public List<Student> getStudentsByFacultyId(@RequestParam Long id) {
        return facultyService.getStudentsByFacultyId(id);
    }
}
