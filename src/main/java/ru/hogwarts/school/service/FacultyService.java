package ru.hogwarts.school.service;

import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;

import java.util.List;
import java.util.Set;

public interface FacultyService {
    Faculty add(Faculty faculty);

    Faculty get(long id);

    Faculty update(long id, String name, String color);

    void delete(long id);

    List<Faculty> getAllFaculties();

    List<Faculty> getByColorIgnoreCase(String color);

    Set<Faculty> getByColorOrNameIgnoreCase(String param);

    List<Student> getStudentsByFacultyId(long id);


}
