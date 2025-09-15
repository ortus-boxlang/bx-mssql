# BoxLang MSSQL Module - AI Agent Instructions

## Project Overview
This is a BoxLang module that provides Microsoft SQL Server JDBC driver support. It's part of the BoxLang ecosystem and follows BoxLang's module architecture patterns.

## Key Architecture Patterns

### Module Registration (Java Service Loader)
- **Main Driver**: `src/main/java/ortus/boxlang/modules/mssql/MicrosoftSQLDriver.java` extends `GenericJDBCDriver`
- **Service Registration**: Handled by `gradle-serviceloader` plugin in `build.gradle` - automatically generates `META-INF/services/ortus.boxlang.runtime.jdbc.drivers.IJDBCDriver`
- **Module Config**: `src/main/bx/ModuleConfig.bx` defines module properties and lifecycle

### Connection URL Building Logic
The driver supports complex MSSQL connection scenarios:
- **Standard**: `jdbc:sqlserver://host:port;databaseName=db;params`
- **Named Instance (dynamic port)**: `jdbc:sqlserver://host\instance;databaseName=db;params`
- **Named Instance (static port)**: `jdbc:sqlserver://host\instance:port;databaseName=db;params`

Critical logic in `MicrosoftSQLDriver.buildConnectionURL()`:
- Instance names append `\\instanceName` to host
- Port handling differs based on `hasInstanceName` boolean
- Default custom params include `trustServerCertificate=true`

## Development Workflows

### Local Development
```bash
# Start test database
docker compose up -d
# Build and test (must download BoxLang first)
./gradlew downloadBoxLang shadowJar test
```

### Testing Strategy
- **Unit Tests**: `src/test/java/ortus/boxlang/modules/mssql/MicrosoftSQLDriverTest.java` - comprehensive URL building scenarios
- **Integration Tests**: `BaseIntegrationTest.java` + `IntegrationTest.java` - test actual database connections
- **CI Database**: GitHub Actions uses services pattern (not docker-compose) with init scripts from `docker/init/*.sql`

### Docker Setup for Tests
- **Local**: Uses `docker-compose.yml` with custom `init-and-run.sh` script
- **CI**: Uses GitHub Actions services with manual script execution from `docker/init/` folder
- **Test Database**: `boxlang_test` with sample tables, users, procedures, views

## Project-Specific Conventions

### Version Management
- `development` branch auto-appends `-snapshot` to versions in `build.gradle`
- BoxLang dependency resolved from local build first, then downloaded jars

### Test Configuration
- Test datasource config in `src/test/resources/boxlang.json`
- Uses `testDataSource` with localhost:1433, sa/BoxLang123!, boxlang_test database
- Integration tests use `KeyDictionary.moduleName` for registration

### Build Artifacts
- **Shadow JAR**: Main distribution artifact with dependencies
- **Module Structure**: `build/module/` contains BoxLang module layout
- **Service Registration**: Auto-generated `META-INF/services` files for BoxLang discovery

## Critical Integration Points

### BoxLang Runtime Integration
- Module must register via `IJDBCDriver` service interface
- Uses BoxLang's `DatasourceConfig` and `StringCaster` utilities
- Integrates with BoxLang's datasource service and connection pooling (HikariCP)

### Database Testing Dependencies
- Requires MSSQL Server 2022+ (uses both `/opt/mssql-tools/bin/sqlcmd` and `/opt/mssql-tools18/bin/sqlcmd`)
- Test scripts create comprehensive schema: users, products, categories tables + stored procedures + views
- CI environment needs specific health checks and initialization timing

## Common Development Tasks

### Adding New Connection Parameters
1. Add to `DEFAULT_CUSTOM_PARAMS` in `MicrosoftSQLDriver.java`
2. Update URL building logic if needed
3. Add test cases in `MicrosoftSQLDriverTest.java`
4. Document in `readme.md` custom parameters section

### Debugging Connection Issues
- Check `buildConnectionURL()` output format
- Verify custom parameters are properly added via `customParamsToQueryString()`
- Test both unit tests (URL generation) and integration tests (actual connections)

### CI/Testing Issues
- CI uses GitHub Actions services, not docker-compose
- Database initialization runs scripts from `docker/init/` manually in CI
- Local development uses `docker-compose.yml` with automatic initialization