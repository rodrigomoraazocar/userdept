-- Creación de la tabla tb_department
CREATE TABLE tb_department (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);


-- Creación de la tabla tb_user
CREATE TABLE tb_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    department_id INTEGER REFERENCES tb_department(id)
);

-- Inserción de datos en tb_department
INSERT INTO tb_department(name) VALUES ('Gestão');
INSERT INTO tb_department(name) VALUES ('Informática');

-- Inserción de datos en tb_user


INSERT INTO tb_user(department_id, name, email) VALUES (1, 'Maria', 'maria@gmail.com');
INSERT INTO tb_user(department_id, name, email) VALUES (1, 'Bob', 'bob@gmail.com');
INSERT INTO tb_user(department_id, name, email) VALUES (2, 'Alex', 'alex@gmail.com');
INSERT INTO tb_user(department_id, name, email) VALUES (2, 'Ana', 'ana@gmail.com');

