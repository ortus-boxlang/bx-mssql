-- Create a dedicated test user for BoxLang integration tests
USE master;

GO
	-- Create login for test user
	IF NOT EXISTS (
		SELECT
			name
		FROM
			sys.server_principals
		WHERE
			name = 'boxlang_test_user'
	) BEGIN CREATE LOGIN boxlang_test_user WITH PASSWORD = 'TestUser123!';

END
GO
	-- Switch to test database
	USE boxlang_test;

GO
	-- Create database user
	IF NOT EXISTS (
		SELECT
			name
		FROM
			sys.database_principals
		WHERE
			name = 'boxlang_test_user'
	) BEGIN CREATE USER boxlang_test_user FOR LOGIN boxlang_test_user;

END
GO
	-- Grant permissions to test user
	ALTER ROLE db_datareader
ADD
	MEMBER boxlang_test_user;

ALTER ROLE db_datawriter
ADD
	MEMBER boxlang_test_user;

ALTER ROLE db_ddladmin
ADD
	MEMBER boxlang_test_user;

-- Grant execute permissions for stored procedures
GRANT EXECUTE ON GetUsersByAge TO boxlang_test_user;

GO
	PRINT 'Test user created with appropriate permissions!';