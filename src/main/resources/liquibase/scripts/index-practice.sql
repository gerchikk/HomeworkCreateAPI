-- liquibase formatted sql

-- changeset gturumtaev:1
CREATE INDEX student_name_index ON student (name);

-- changeset gturumtaev:2
CREATE INDEX faculty_nc_index ON faculty (name, color);
