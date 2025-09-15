# ⚡︎ BoxLang Module: Microsoft SQL Server JDBC Driver

```
|:------------------------------------------------------:|
| ⚡︎ B o x L a n g ⚡︎
| Dynamic : Modular : Productive
|:------------------------------------------------------:|
```

<blockquote>
	Copyright Since 2023 by Ortus Solutions, Corp
	<br>
	<a href="https://www.boxlang.io">www.boxlang.io</a> |
	<a href="https://www.ortussolutions.com">www.ortussolutions.com</a>
</blockquote>

<p>&nbsp;</p>

This module provides a BoxLang JDBC driver for Microsoft SQL Server. This module is part of the BoxLang project.

## Installation

Install this module using CommandBox:

```bash
install bx-mssql
```

Or add it to your `box.json` dependencies:

```json
{
    "dependencies": {
        "bx-mssql": "^1.0.0"
    }
}
```

## Configuration

This driver supports various connection configurations for Microsoft SQL Server, including standard connections, named instances, and custom JDBC parameters.

### Basic Configuration

See [BoxLang's Defining Datasources](https://boxlang.ortusbooks.com/boxlang-language/syntax/queries#defining-datasources) documentation for full examples on where and how to construct a datasource connection pool.

Here's a basic example of a BoxLang datasource definition using this Microsoft SQL Server driver:

```js
this.datasources["mssqlDB"] = {
	driver: "mssql",
	host: "localhost",
	port: "1433",
	database: "myDB",
	username: "sa",
	password: "123456Password"
};
```

### Environment Variable Configuration

You can use [environment variable substitution](https://boxlang.ortusbooks.com/getting-started/configuration#environment-variable-substitution) to populate the datasource definition:

```js
this.datasources["mssqlDB"] = {
	driver: "mssql",
	host: "${env.MSSQL_HOST:localhost}",
	port: "${env.MSSQL_PORT:1433}",
	database: "${env.MSSQL_DATABASE:myDB}",
	username: "${env.MSSQL_USERNAME:sa}",
	password: "${env.MSSQL_PASSWORD:123456Password}"
};
```

## Connection Types

### Standard Connection with Static Port

```js
this.datasources["standardConnection"] = {
	driver: "mssql",
	host: "sql-server.company.com",
	port: "1433",
	database: "ProductionDB",
	username: "appuser",
	password: "SecurePassword123!"
};
```

**Generated URL**: `jdbc:sqlserver://sql-server.company.com:1433;databaseName=ProductionDB;trustServerCertificate=true`

### Named Instance Connection (Dynamic Port)

For SQL Server named instances, omit the port to use dynamic port discovery:

```js
this.datasources["instanceConnection"] = {
	driver: "mssql",
	host: "sql-server.company.com",
	instanceName: "SQLEXPRESS",
	database: "TestDB",
	username: "sa",
	password: "Password123!"
};
```

**Generated URL**: `jdbc:sqlserver://sql-server.company.com\SQLEXPRESS;databaseName=TestDB;trustServerCertificate=true`

### Named Instance with Static Port

You can also specify both an instance name and a static port:

```js
this.datasources["instanceWithPort"] = {
	driver: "mssql",
	host: "sql-cluster.domain.com",
	instanceName: "NODE1",
	port: "2433",
	database: "ClusterDB",
	username: "clusteruser",
	password: "ClusterPass123!"
};
```

**Generated URL**: `jdbc:sqlserver://sql-cluster.domain.com\NODE1:2433;databaseName=ClusterDB;trustServerCertificate=true`

## Custom JDBC Parameters

Use the `custom` key to pass additional JDBC connection parameters:

### SSL/TLS Configuration

```js
this.datasources["secureConnection"] = {
	driver: "mssql",
	host: "secure-sql.company.com",
	port: "1433",
	database: "SecureDB",
	username: "secureuser",
	password: "SecurePassword123!",
	custom: {
		encrypt: "true",
		trustServerCertificate: "false",
		hostNameInCertificate: "*.company.com",
		trustStore: "/path/to/truststore.jks",
		trustStorePassword: "truststorepass"
	}
};
```

### Connection Timeout and Performance

```js
this.datasources["performanceOptimized"] = {
	driver: "mssql",
	host: "fast-sql.company.com",
	port: "1433",
	database: "FastDB",
	username: "fastuser",
	password: "FastPassword123!",
	custom: {
		loginTimeout: "30",
		socketTimeout: "60000",
		packetSize: "8192",
		sendStringParametersAsUnicode: "false",
		selectMethod: "cursor",
		responseBuffering: "adaptive"
	}
};
```

### Application Identification

```js
this.datasources["identifiedConnection"] = {
	driver: "mssql",
	host: "monitored-sql.company.com",
	port: "1433",
	database: "MonitoredDB",
	username: "appuser",
	password: "AppPassword123!",
	custom: {
		applicationName: "MyBoxLangApp",
		workstationID: "WebServer01",
		applicationIntent: "ReadWrite"
	}
};
```

### Integrated Security (Windows Authentication)

```js
this.datasources["windowsAuth"] = {
	driver: "mssql",
	host: "domain-sql.company.com",
	port: "1433",
	database: "DomainDB",
	custom: {
		integratedSecurity: "true",
		authenticationScheme: "JavaKerberos"
	}
	// Note: username/password not needed with integrated security
};
```

### Always Encrypted

```js
this.datasources["encryptedConnection"] = {
	driver: "mssql",
	host: "encrypted-sql.company.com",
	port: "1433",
	database: "EncryptedDB",
	username: "encryptuser",
	password: "EncryptPassword123!",
	custom: {
		columnEncryptionSetting: "Enabled",
		keyStoreAuthentication: "JavaKeyStorePassword",
		keyStoreLocation: "/path/to/keystore.jks",
		keyStoreSecret: "keystorepassword"
	}
};
```

## Connection Pool Configuration

You can configure HikariCP connection pool settings:

```js
this.datasources["pooledConnection"] = {
	driver: "mssql",
	host: "pooled-sql.company.com",
	port: "1433",
	database: "PooledDB",
	username: "pooluser",
	password: "PoolPassword123!",

	// Connection pool settings
	maxConnections: 50,
	minConnections: 5,
	maxConnectionLifetime: 1800000, // 30 minutes
	idleTimeout: 600000, // 10 minutes
	connectionTimeout: 30000, // 30 seconds
	leakDetectionThreshold: 60000, // 1 minute

	custom: {
		applicationName: "PooledApp",
		loginTimeout: "15"
	}
};
```

## Docker Development Setup

This module includes Docker Compose configuration for local development:

```bash
# Start MSSQL Server with test database
docker compose up -d

# Connect to the test database
docker compose exec mssql /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P BoxLang123! -d boxlang_test -C
```

The Docker setup includes:

- SQL Server 2022 Developer Edition
- Pre-configured test database (`boxlang_test`)
- Sample tables with test data
- Test user with appropriate permissions

### Test Database Configuration

```js
this.datasources["testDB"] = {
	driver: "mssql",
	host: "localhost",
	port: "1433",
	database: "boxlang_test",
	username: "sa",
	password: "BoxLang123!"
};

// Or use the test user
this.datasources["testUserDB"] = {
	driver: "mssql",
	host: "localhost",
	port: "1433",
	database: "boxlang_test",
	username: "boxlang_test_user",
	password: "TestUser123!"
};
```

## Common JDBC URL Patterns

This driver generates JDBC URLs in the following formats:

| Configuration | Generated URL |
|---------------|---------------|
| Standard connection | `jdbc:sqlserver://host:port;databaseName=db;params` |
| Named instance (dynamic) | `jdbc:sqlserver://host\instance;databaseName=db;params` |
| Named instance (static) | `jdbc:sqlserver://host\instance:port;databaseName=db;params` |
| No port specified | `jdbc:sqlserver://host;databaseName=db;params` |

## Advanced Configuration Examples

### Multi-Subnet Failover

```js
this.datasources["failoverConnection"] = {
	driver: "mssql",
	host: "primary-sql.company.com,secondary-sql.company.com",
	port: "1433",
	database: "FailoverDB",
	username: "failoveruser",
	password: "FailoverPassword123!",
	custom: {
		multiSubnetFailover: "true",
		applicationIntent: "ReadWrite",
		failoverPartner: "secondary-sql.company.com"
	}
};
```

### Read-Only Replica

```js
this.datasources["readOnlyConnection"] = {
	driver: "mssql",
	host: "readonly-sql.company.com",
	port: "1433",
	database: "ReportingDB",
	username: "reportuser",
	password: "ReportPassword123!",
	custom: {
		applicationIntent: "ReadOnly",
		readOnlyApplicationIntent: "true"
	}
};
```

### Azure SQL Database

```js
this.datasources["azureConnection"] = {
	driver: "mssql",
	host: "myserver.database.windows.net",
	port: "1433",
	database: "myazuredb",
	username: "azureuser@myserver",
	password: "AzurePassword123!",
	custom: {
		encrypt: "true",
		trustServerCertificate: "false",
		hostNameInCertificate: "*.database.windows.net",
		loginTimeout: "30"
	}
};
```

## Troubleshooting

### Common Connection Issues

1. **Connection Timeout**: Increase `loginTimeout` in custom parameters
2. **SSL/TLS Issues**: Set `trustServerCertificate: "true"` for development
3. **Named Instance Discovery**: Ensure SQL Server Browser service is running
4. **Port Blocking**: Check firewall settings for port 1433 or dynamic ports
5. **Authentication**: Verify username/password or integrated security configuration

### Enable Connection Logging

```js
this.datasources["debugConnection"] = {
	driver: "mssql",
	// ... other settings
	custom: {
		logLevel: "DEBUG",
		logFile: "/path/to/mssql-debug.log"
	}
};
```

## Ortus Sponsors

BoxLang is a professional open-source project and it is completely funded by the [community](https://patreon.com/ortussolutions) and [Ortus Solutions, Corp](https://www.ortussolutions.com). Ortus Patreons get many benefits like a cfcasts account, a FORGEBOX Pro account and so much more. If you are interested in becoming a sponsor, please visit our patronage page: [https://patreon.com/ortussolutions](https://patreon.com/ortussolutions)

### THE DAILY BREAD

> "I am the way, and the truth, and the life; no one comes to the Father, but by me (JESUS)" Jn 14:1-12
