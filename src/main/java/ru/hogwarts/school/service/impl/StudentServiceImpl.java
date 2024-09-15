package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.LastFiveStudent;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
        Student newStudent = new Student(student);
        newStudent = studentRepository.save(newStudent);
        return newStudent;
    }
    @Override
    public Student get(long id) {
        return studentRepository.findById(id).get();
    }
    @Override
    public Student update(long id, String name, int age) {
        Student studentForUpdate = studentRepository.findById(id).get();
        studentForUpdate.setName(name);
        studentForUpdate.setAge(age);
        return studentRepository.save(studentForUpdate);
    }
    @Override
    public void delete(long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    @Override
    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getFacultyByStudentId(long id) {
        return studentRepository.findById(id).get().getFaculty();
    }

    @Override
    public List<Student> getByFacultyId(long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }

    @Override
    public Integer getCountStudents() {
        return studentRepository.getCountStudents();
    }

    @Override
    public Double getAverageAgeStudents() {
        return studentRepository.getAverageAgeStudents();
    }

    @Override
    public List<LastFiveStudent> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents();
    }
}
