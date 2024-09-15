package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.entity.LastFiveStudent;
import ru.hogwarts.school.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int min, int max);

    List<Student> findByFacultyId(long facultyId);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getCountStudents();

    @Query(value = "SELECT AVG(age) as averageAge FROM student", nativeQuery = true)
    Double getAverageAgeStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<LastFiveStudent> getLastFiveStudents();

}
