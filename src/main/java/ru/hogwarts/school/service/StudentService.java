package ru.hogwarts.school.service;

import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;

import java.util.List;

public interface StudentService {
    Student add(String name, int age);

    Student get(long id);

    Student update(long id, String name, int age);

    void delete(long id);

    List<Student> getAllStudents();

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int min, int max);
    List<Student> getByFacultyId(long id);

    Faculty getFacultyByStudentId(long id);
}
