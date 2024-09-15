-- Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах
-- (достаточно получить только имя и возраст студента) школы Хогвартс вместе с названиями факультетов.
SELECT student.name, student.age, faculty.name
FROM student
RIGHT JOIN faculty ON student.faculty_id = faculty.id;
-- Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.
SELECT student.name, student.age, avatar.file_path
FROM student
RIGHT JOIN avatar ON student.id = avatar.student_id;
