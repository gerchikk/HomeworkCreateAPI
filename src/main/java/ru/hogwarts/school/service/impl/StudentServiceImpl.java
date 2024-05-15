package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.NotFoundStudentException;
import ru.hogwarts.school.exception.NotSupportedClassException;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Student add(Student student) {
        logger.info("method add is run");
        logger.debug("Student input: " + student);

        Student createStudent = null;

        try {
            createStudent = studentRepository.save(student);
        } catch (RuntimeException e) {
            logger.error("Not can upload new student. Reason: " + e.getMessage());
            throw new NotSupportedClassException("JSON not valid");
        }

        logger.debug("Student " + createStudent + " upload to db");
        return createStudent;
    }

    @Override
    public Student get(long id) {
        logger.info("method get is run");
        Student finnStudent = studentRepository.findById(id).orElseThrow(() -> {
            logger.warn("method get cannot find student with id " + id);
            return new NotFoundStudentException("Нет такого студента с id " + id);
        });
        logger.debug("findStudent = " + finnStudent);
        return finnStudent;
    }

    @Override
    public Student update(Student student) {
        logger.info("method update is run");

        Student updatedStudent = studentRepository.findById(student.getId()).orElseThrow(() -> {
            logger.warn("method update cannot find student " + student);
            return new NotFoundStudentException("Нет такого студента : " + student);
        });
        logger.debug("updatedStudent = " + updatedStudent);
        return studentRepository.save(student);
    }

    @Override
    public void delete(long id) {
        logger.info("method delete is run");

        Student student = studentRepository.findById(id).orElse(null);

        logger.debug("Method Remove find: " + student);

        if (student != null) {
            studentRepository.deleteById(id);
            logger.debug("student deleted");
        } else {
            logger.warn("Student for delete not found with id " + id);
            throw new NotFoundStudentException("Student for delete not found");
        }
    }

    @Override
    public List<Student> getAllStudents() {
        logger.info("method getAllStudent is run");
        List<Student> all = studentRepository.findAll();
        logger.debug("Collection<Student> = " + all);
        return all;
    }

    @Override
    public List<Student> findByAge(int age) {
        logger.info("method findByAge is run");
        List<Student> studentByAge = studentRepository.findByAge(age);
        logger.debug("Collection<Student> =" + studentByAge);
        return studentByAge;
    }

    @Override
    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("method findByAgeBetween is run");
        List<Student> byAgeBetween = studentRepository.findByAgeBetween(min, max);
        logger.debug("Collection<Student> =" + byAgeBetween);
        return byAgeBetween;
    }

    @Override
    public Faculty getFacultyByStudentId(long id) {
        logger.info("method getFacultyByStudentId is run");
        return studentRepository.findById(id).get().getFaculty();
    }

    @Override
    public List<Student> getByFacultyId(long facultyId) {
        logger.info("method getByFacultyId is run");
        return studentRepository.findByFacultyId(facultyId);
    }

    @Override
    public Integer getCountStudents() {
        logger.info("method getCountStudents is run");
        Integer allCountStudents = studentRepository.getCountStudents();
        logger.debug("Count students is  " + allCountStudents);
        return allCountStudents;
    }

    @Override
    public Double getAVGAgeStudents() {
        logger.info("method getAVGAgeStudent is run");
        Double avgAgeStudent = studentRepository.getAverageAgeStudents();
        logger.debug("AVGAgeStudent students is  " + avgAgeStudent);
        return avgAgeStudent;
    }

    @Override
    public List<Student> getLastFiveStudents() {
        logger.info("method getLastFiveStudent is run");
        List<Student> fiveLastStudent = studentRepository.getLastFiveStudents();
        logger.debug("Collection<Student> =" + fiveLastStudent);
        return fiveLastStudent;
    }

    @Override
    public Collection<String> studentsNameStartWithA() {
        logger.info("method getStudentNameStartWithA is run");
        List<String> all = studentRepository.findAll().stream()
                .filter(s->s.getName().startsWith("A"))
                .map(s->s.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
        logger.debug("Collection<String> = {}", all);
        return all;
    }

    @Override
    public Double getAVGAgeStudentsWithStream() {
        logger.info("method getAVGAgeStudentsWithStream is run");
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @Override
    public void getAllStudents2() {
        getNamesStudents(0);
        getNamesStudents(1);

        new Thread(() -> {
            getNamesStudents(2);
            getNamesStudents(3);
        }).start();

        new Thread(() -> {
            getNamesStudents(4);
            getNamesStudents(5);
        }).start();

    }
    @Override
    public synchronized void getAllStudents3() {
        getNamesStudents(0);
        getNamesStudents(1);

        new Thread(() -> {
            getNamesStudents(2);
            getNamesStudents(3);
        }).start();

        new Thread(() -> {
            getNamesStudents(4);
            getNamesStudents(5);
        }).start();

    }

    @Override
    public void getNamesStudents(int number) {
        List<Student> students = studentRepository.findAll();
        System.out.println(students.get(number).getName());
    }
}
