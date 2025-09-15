package ortus.boxlang.modules.mssql;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Struct;

public class MicrosoftSQLDriverTest {

	@Test
	@DisplayName( "Test getName()" )
	public void testGetName() {
		MicrosoftSQLDriver	driver			= new MicrosoftSQLDriver();
		Key					expectedName	= new Key( "MSSQL" );
		assertThat( driver.getName() ).isEqualTo( expectedName );
	}

	@Test
	@DisplayName( "Test getType()" )
	public void testGetType() {
		MicrosoftSQLDriver	driver			= new MicrosoftSQLDriver();
		DatabaseDriverType	expectedType	= DatabaseDriverType.MSSQL;
		assertThat( driver.getType() ).isEqualTo( expectedType );
	}

	@Test
	@DisplayName( "Test buildConnectionURL()" )
	public void testBuildConnectionURL() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "mydb" );

		String expectedURL = "jdbc:sqlserver://localhost:1433;databaseName=mydb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@DisplayName( "Throw an exception if the database is not found" )
	@Test
	public void testBuildConnectionURLNoDatabase() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();

		assertThrows( IllegalArgumentException.class, () -> {
			driver.buildConnectionURL( config );
		} );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with custom host and port" )
	public void testBuildConnectionURLWithCustomHostAndPort() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "host", "sql.example.com" );
		config.properties.put( "port", "1434" );

		String expectedURL = "jdbc:sqlserver://sql.example.com:1434;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with instance name and no port (dynamic port)" )
	public void testBuildConnectionURLWithInstanceNameDynamicPort() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "instanceName", "SQLEXPRESS" );

		String expectedURL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with instance name and static port" )
	public void testBuildConnectionURLWithInstanceNameStaticPort() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "instanceName", "SQLEXPRESS" );
		config.properties.put( "port", "1435" );

		String expectedURL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1435;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with custom host and instance name" )
	public void testBuildConnectionURLWithCustomHostAndInstanceName() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "host", "sql-server.domain.com" );
		config.properties.put( "instanceName", "PRODUCTION" );

		String expectedURL = "jdbc:sqlserver://sql-server.domain.com\\PRODUCTION;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with custom host, instance name, and static port" )
	public void testBuildConnectionURLWithCustomHostInstanceNameAndPort() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "host", "sql-cluster.company.com" );
		config.properties.put( "instanceName", "NODE1" );
		config.properties.put( "port", "2433" );

		String expectedURL = "jdbc:sqlserver://sql-cluster.company.com\\NODE1:2433;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with no port specified (dynamic port discovery)" )
	public void testBuildConnectionURLWithNoDynamicPort() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "host", "sql.example.com" );
		config.properties.put( "port", "" );

		String expectedURL = "jdbc:sqlserver://sql.example.com:1433;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with port set to 0 (uses default port)" )
	public void testBuildConnectionURLWithZeroPort() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "port", "0" );

		String expectedURL = "jdbc:sqlserver://localhost:1433;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with empty host (uses default localhost)" )
	public void testBuildConnectionURLWithEmptyHost() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "host", "" );

		String expectedURL = "jdbc:sqlserver://localhost:1433;databaseName=testdb;trustServerCertificate=true";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@Test
	@DisplayName( "Test buildConnectionURL() with additional custom parameters" )
	public void testBuildConnectionURLWithCustomParameters() {
		MicrosoftSQLDriver	driver	= new MicrosoftSQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "mssql" );
		config.properties.put( "database", "testdb" );
		config.properties.put( "custom",
		    Struct.of(
		        "encrypt", "true",
		        "loginTimeout", "30",
		        "applicationName", "BoxLangApp"
		    )
		);

		String connectionURL = driver.buildConnectionURL( config );

		// Verify the URL contains our parameters
		assertThat( connectionURL ).startsWith( "jdbc:sqlserver://localhost:1433;databaseName=testdb;" );
		assertThat( connectionURL ).contains( "trustServerCertificate=true" );
		assertThat( connectionURL ).contains( "encrypt=true" );
		assertThat( connectionURL ).contains( "loginTimeout=30" );
		assertThat( connectionURL ).contains( "applicationName=BoxLangApp" );
	}

}
