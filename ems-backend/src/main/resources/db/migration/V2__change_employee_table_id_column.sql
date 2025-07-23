-- Drop existing primary key if you want to redefine it
ALTER TABLE employees DROP PRIMARY KEY;

-- Now you can alter the id column or add a new primary key as needed
ALTER TABLE employees MODIFY COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY;