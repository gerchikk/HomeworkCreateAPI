package ru.hogwarts.school.service;

import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    Student add(Student student);

    Student get(long id);

    Student update(Student student);

    void delete(long id);

    List<Student> getAllStudents();

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int min, int max);
    List<Student> getByFacultyId(long id);

    Faculty getFacultyByStudentId(long id);

    Integer getCountStudents();

    Double getAVGAgeStudents();

    List<Student> getLastFiveStudents();

    Collection<String> studentsNameStartWithA();

    Double getAVGAgeStudentsWithStream();

    void getAllStudents2();

    void getAllStudents3();

    void getNamesStudents(int number);
}
