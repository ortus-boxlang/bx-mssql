-- Create test database for BoxLang MSSQL module integration tests
USE master;

GO
	-- Create the test database
	IF NOT EXISTS (
		SELECT
			name
		FROM
			sys.databases
		WHERE
			name = N'boxlang_test'
	) BEGIN CREATE DATABASE boxlang_test;

END
GO
	-- Switch to the test database
	USE boxlang_test;

GO
	-- Create a users table for testing
	CREATE TABLE users (
		id INT IDENTITY(1, 1) PRIMARY KEY,
		username NVARCHAR(50) NOT NULL UNIQUE,
		email NVARCHAR(100) NOT NULL,
		first_name NVARCHAR(50),
		last_name NVARCHAR(50),
		age INT,
		is_active BIT DEFAULT 1,
		created_date DATETIME2 DEFAULT GETDATE(),
		updated_date DATETIME2 DEFAULT GETDATE()
	);

GO
	-- Create a products table for testing
	CREATE TABLE products (
		id INT IDENTITY(1, 1) PRIMARY KEY,
		name NVARCHAR(100) NOT NULL,
		description NTEXT,
		price DECIMAL(10, 2),
		category_id INT,
		in_stock BIT DEFAULT 1,
		created_date DATETIME2 DEFAULT GETDATE()
	);

GO
	-- Create a categories table for testing
	CREATE TABLE categories (
		id INT IDENTITY(1, 1) PRIMARY KEY,
		name NVARCHAR(50) NOT NULL,
		description NVARCHAR(255)
	);

GO
	-- Add foreign key constraint
ALTER TABLE
	products
ADD
	CONSTRAINT FK_products_categories FOREIGN KEY (category_id) REFERENCES categories(id);

GO
	-- Insert sample data for testing
INSERT INTO
	categories (name, description)
VALUES
	('Electronics', 'Electronic devices and gadgets'),
	('Books', 'Books and publications'),
	('Clothing', 'Clothing and accessories');

GO
INSERT INTO
	users (username, email, first_name, last_name, age)
VALUES
	(
		'john_doe',
		'john.doe@example.com',
		'John',
		'Doe',
		30
	),
	(
		'jane_smith',
		'jane.smith@example.com',
		'Jane',
		'Smith',
		25
	),
	(
		'bob_wilson',
		'bob.wilson@example.com',
		'Bob',
		'Wilson',
		35
	);

GO
INSERT INTO
	products (name, description, price, category_id)
VALUES
	(
		'Laptop',
		'High-performance laptop computer',
		999.99,
		1
	),
	(
		'Programming Book',
		'Learn advanced programming concepts',
		49.99,
		2
	),
	(
		'T-Shirt',
		'Comfortable cotton t-shirt',
		19.99,
		3
	),
	(
		'Smartphone',
		'Latest model smartphone',
		699.99,
		1
	);

GO
	-- Create a stored procedure for testing
	CREATE PROCEDURE GetUsersByAge @MinAge INT = 0,
	@MaxAge INT = 150 AS BEGIN
SELECT
	id,
	username,
	email,
	first_name,
	last_name,
	age,
	is_active,
	created_date
FROM
	users
WHERE
	age BETWEEN @MinAge
	AND @MaxAge
ORDER BY
	age;

END
GO
	-- Create a view for testing
	CREATE VIEW ActiveUsersView AS
SELECT
	u.id,
	u.username,
	u.email,
	u.first_name + ' ' + u.last_name AS full_name,
	u.age,
	u.created_date
FROM
	users u
WHERE
	u.is_active = 1;

GO
	-- Create indexes for better performance in tests
	CREATE INDEX IX_users_username ON users(username);

CREATE INDEX IX_users_email ON users(email);

CREATE INDEX IX_products_category ON products(category_id);

GO
	PRINT 'BoxLang test database and sample data created successfully!';