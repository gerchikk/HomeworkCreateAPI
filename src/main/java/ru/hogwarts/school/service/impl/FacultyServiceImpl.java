package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentService studentService;

    public FacultyServiceImpl(FacultyRepository facultyRepository, StudentService studentService) {
        this.facultyRepository = facultyRepository;
        this.studentService = studentService;
    }
    @Override
    public Faculty add(Faculty faculty) {
        Faculty newFaculty = new Faculty(faculty);
        newFaculty = facultyRepository.save(newFaculty);
        return newFaculty;
    }
    @Override
    public Faculty get(long id) {
        return facultyRepository.findById(id).get();
    }
    @Override
    public Faculty update(long id, String name, String color) {
        Faculty facultyForUpdate = facultyRepository.findById(id).get();
        facultyForUpdate.setName(name);
        facultyForUpdate.setColor(color);
        return facultyRepository.save(facultyForUpdate);
    }
    @Override
    public void delete(long id) {
        facultyRepository.deleteById(id);
    }
    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public List<Faculty> getByColorIgnoreCase(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }
    @Override
    public Set<Faculty> getByColorOrNameIgnoreCase(String param) {
        Set<Faculty> result = new HashSet<>();
        result.addAll(facultyRepository.findByColorIgnoreCase(param));
        result.addAll(facultyRepository.findByNameIgnoreCase(param));
        return result;
    }
    @Override
    public List<Student> getStudentsByFacultyId(long id) {
        return studentService.getByFacultyId(id);
    }
}
