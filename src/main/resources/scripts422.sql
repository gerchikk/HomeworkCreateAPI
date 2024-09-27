-- Описание структуры: у каждого человека есть машина. Причем несколько человек могут пользоваться одной машиной.
-- У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет).
-- У каждой машины есть марка, модель и стоимость. Также не забудьте добавить таблицам первичные ключи и связать их.

CREATE TABLE People(
id SERIAL PRIMARY KEY,
name TEXT,
age SERIAL,
driversLicense BOOLEAN,
car_id SERIAL REFERENCES cars (id)
);

CREATE TABLE Cars(
id SERIAL PRIMARY KEY,
brand TEXT,
model TEXT,
cost NUMERIC (10, 2)
);

SELECT people.name, people.driversLicense, cars.brand, cars.model, cars.cost
FROM people
INNER JOIN cars ON people.car_id = cars.id


