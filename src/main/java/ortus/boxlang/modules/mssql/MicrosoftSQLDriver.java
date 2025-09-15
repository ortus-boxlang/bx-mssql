/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.modules.mssql;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.jdbc.drivers.GenericJDBCDriver;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;

/**
 * The Microsoft SQL JDBC Driver
 * https://learn.microsoft.com/en-us/sql/connect/jdbc/building-the-connection-url?view=sql-server-ver16
 */
public class MicrosoftSQLDriver extends GenericJDBCDriver {

	protected static final String	DEFAULT_CLASSNAME			= "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	protected static final String	DEFAULT_PORT				= "1433";
	protected static final String	DEFAULT_HOST				= "localhost";
	protected static final String	DEFAULT_DELIMITER			= ";";
	protected static final String	DEFAULT_URI_DELIMITER		= ";";
	protected static final IStruct	DEFAULT_HIKARI_PROPERTIES	= Struct.of();
	protected static final IStruct	DEFAULT_CUSTOM_PARAMS		= Struct.of(
	    "trustServerCertificate", "true"
	);

	/**
	 * Constructor
	 */
	public MicrosoftSQLDriver() {
		super();
		this.name					= new Key( "MSSQL" );
		this.type					= DatabaseDriverType.MSSQL;
		this.driverClassName		= DEFAULT_CLASSNAME;
		this.defaultDelimiter		= DEFAULT_DELIMITER;
		this.defaultURIDelimiter	= DEFAULT_URI_DELIMITER;
		this.defaultCustomParams	= DEFAULT_CUSTOM_PARAMS;
		this.defaultProperties		= DEFAULT_HIKARI_PROPERTIES;
	}

	@Override
	public String buildConnectionURL( DatasourceConfig config ) {
		// Validate the host
		String host = StringCaster.cast( config.properties.getOrDefault( "host", DEFAULT_HOST ) );
		if ( host.isEmpty() ) {
			host = DEFAULT_HOST;
		}

		// Instance Name : Optional
		boolean	hasInstanceName	= false;
		String	instanceName	= StringCaster.cast( config.properties.getOrDefault( "instanceName", "" ) );
		if ( !instanceName.isEmpty() ) {
			host			= host + "\\" + instanceName;
			hasInstanceName	= true;
		}

		// Port
		String port = null;
		// Case 1: Check if instance name is used, else default to normal port detection or default port
		if ( hasInstanceName ) {
			var maybePort = StringCaster.cast( config.properties.getOrDefault( "port", "" ) );
			if ( !maybePort.isEmpty() ) {
				port = maybePort;
			}
		} else {
			port = StringCaster.cast( config.properties.getOrDefault( "port", DEFAULT_PORT ) );
			if ( port.isEmpty() || port.equals( "0" ) ) {
				port = DEFAULT_PORT;
			}
		}

		// Validate the database
		String database = StringCaster.cast( config.properties.getOrDefault( "database", "" ) );
		if ( database.isEmpty() ) {
			throw new IllegalArgumentException( "The database property is required for the Microsoft JDBC Driver" );
		}

		// Build the URL: Two cases, with or without port
		if ( port != null && !port.isEmpty() ) {
			return String.format(
			    "jdbc:sqlserver://%s:%s;databaseName=%s;%s",
			    host,
			    port,
			    database,
			    customParamsToQueryString( config )
			);
		}
		// No port
		return String.format(
		    "jdbc:sqlserver://%s;databaseName=%s;%s",
		    host,
		    database,
		    customParamsToQueryString( config )
		);
	}

}
