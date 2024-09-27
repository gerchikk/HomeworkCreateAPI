/* 1 */select *
       from student s
       where s.age between 12 and 18;
/* 2 */select s.name
       from student s;
/* 3 */select *
       from student
       where name like '%й%';
/* 4 */select *
       from student s
       where s.age < s.id;
/* 5 */select *
       from student s
       order by s.age;


