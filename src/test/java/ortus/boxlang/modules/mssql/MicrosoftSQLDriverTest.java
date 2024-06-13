package ortus.boxlang.modules.mssql;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.scopes.Key;

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

}
