package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.LastFiveStudent;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
        logger.info("Was invoked method for add student");
        Student newStudent = new Student(student);
        newStudent = studentRepository.save(newStudent);
        return newStudent;
    }
    @Override
    public Student get(long id) {
        logger.info("Was invoked method for get student");
        return studentRepository.findById(id).get();
    }
    @Override
    public Student update(long id, String name, int age) {
        logger.info("Was invoked method for update student");
        Student studentForUpdate = studentRepository.findById(id).get();
        studentForUpdate.setName(name);
        studentForUpdate.setAge(age);
        return studentRepository.save(studentForUpdate);
    }
    @Override
    public void delete(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findByAge(int age) {
        logger.info("Was invoked method for age students");
        return studentRepository.findByAge(age);
    }

    @Override
    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for between min max age student");
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getFacultyByStudentId(long id) {
        logger.info("Was invoked method for faculty by student id");
        return studentRepository.findById(id).get().getFaculty();
    }

    @Override
    public List<Student> getByFacultyId(long facultyId) {
        logger.info("Was invoked method for get by faculty id students");
        return studentRepository.findByFacultyId(facultyId);
    }

    @Override
    public Integer getCountStudents() {
        logger.info("Was invoked method for count students");
        return studentRepository.getCountStudents();
    }

    @Override
    public Double getAverageAgeStudents() {
        logger.info("Was invoked method for average age students");
        return studentRepository.getAverageAgeStudents();
    }

    @Override
    public List<LastFiveStudent> getLastFiveStudents() {
        logger.info("Was invoked method for last five students");
        return studentRepository.getLastFiveStudents();
    }

    @Override
    public Collection<String> studentsNameStartWithA() {
        logger.info("Was invoked method for students name start with A");
        return studentRepository
                .findAll()
                .stream()
                .filter(s->s.getName().startsWith("A"))
                .map(s->s.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageAgeStudents2() {
        logger.info("Was invoked method getAvgAgeStudents2");
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @Override
    public void getAllStudents2() {
        System.out.println("_______________");
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
        System.out.println("_______________");
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
