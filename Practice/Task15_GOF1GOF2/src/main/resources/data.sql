set schema public;

drop table IF EXISTS FIBONACCI;

create table EMPLOYEE
(
    id            INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name          VARCHAR(100)                   NOT NULL,
    salary        DOUBLE                         NOT NULL,
    department_id VARCHAR(100)                   NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS UNIQUE_PERSON ON EMPLOYEE (name);

create table SALARY_PAYMENTS
(
    id          INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    employee_id INT                            NOT NULL,
    date        DATE
--  foreign key (employee_id) references EMPLOYEE(id)
);

-- "select emp.id as emp_id, emp.name as amp_name, sum(salary) as salary from employee emp left join" +
--                     "salary_payments sp on emp.id = sp.employee_id where emp.department_id = ? and" +
--                     " sp.date >= ? and sp.date <= ? group by emp.id, emp.name"

-- create table PERSON (
--                         id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
--                         name VARCHAR(100) NOT NULL,
--                         age INT NOT NULL
-- );
--
-- CREATE UNIQUE INDEX IF NOt EXISTS UNIQUE_PERSON ON PERSON(name);
--
-- create table ANIMAL (
--                         id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
--                         name VARCHAR(100) NOT NULL,
--                         type VARCHAR(100) NOT NULL,
--                         PERSON_ID INT
-- --    foreign key (PERSON_ID) references PERSON(id)
-- );
--
-- CREATE UNIQUE INDEX IF NOt EXISTS UNIQUE_ANIMAL ON ANIMAL(name, type);