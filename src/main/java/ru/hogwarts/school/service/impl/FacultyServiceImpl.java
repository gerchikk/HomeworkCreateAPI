package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private final StudentService studentService;

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyServiceImpl(FacultyRepository facultyRepository, StudentService studentService) {
        this.facultyRepository = facultyRepository;
        this.studentService = studentService;
    }
    @Override
    public Faculty add(Faculty faculty) {
        logger.debug("Was invoked method for add a faculty");
        Faculty newFaculty = new Faculty(faculty);
        newFaculty = facultyRepository.save(newFaculty);
        return newFaculty;
    }

    @Override
    public Faculty get(long id) {
        logger.debug("Was invoked method for get the faculty");
        return facultyRepository.findById(id).get();
    }

    @Override
    public Faculty update(long id, String name, String color) {
        logger.debug("Was invoked method for update the faculty");
        Faculty facultyForUpdate = facultyRepository.findById(id).get();
        facultyForUpdate.setName(name);
        facultyForUpdate.setColor(color);
        return facultyRepository.save(facultyForUpdate);
    }

    @Override
    public void delete(long id) {
        logger.debug("Was invoked method to delete the faculty");
        facultyRepository.deleteById(id);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        logger.debug("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    @Override
    public List<Faculty> getByColorIgnoreCase(String color) {
        logger.debug("Was invoked method for get faculties by color");
        return facultyRepository.findByColorIgnoreCase(color);
    }

    @Override
    public Set<Faculty> getByColorOrNameIgnoreCase(String param) {
        logger.debug("Was invoked method for get faculties by color or name");
        Set<Faculty> result = new HashSet<>();
        result.addAll(facultyRepository.findByColorIgnoreCase(param));
        result.addAll(facultyRepository.findByNameIgnoreCase(param));
        return result;
    }

    @Override
    public List<Student> getStudentsByFacultyId(long id) {
        logger.debug("Was invoked method for get the students by faculty id");
        return studentService.getByFacultyId(id);
    }

    @Override
    public String longestFacultyName() {
        logger.info("Was invoked method for longest faculty name");
        return facultyRepository.findAll()
                .stream()
                .max((f1, f2) -> f1.getName().length() - f2.getName().length())
                .toString();
    }

    @Override
    public Integer sumExample() {
        logger.info("Was invoked method for calculation sum");
        Integer sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        return sum;
    }


}
