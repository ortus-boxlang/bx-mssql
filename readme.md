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

## Examples

See [BoxLang's Defining Datasources](https://boxlang.ortusbooks.com/boxlang-language/syntax/queries#defining-datasources) documentation for full examples on where and how to construct a datasource connection pool.

Here's an example of a BoxLang datasource definition using this Microsoft SQL Server driver:

```js
this.datasources["mssqlDB"] = {
	driver: "mssql",
	host: "localhost",
	port: "1433",
	database: "myDB",
	username: "sa",
	password: "123456Password",
};
```

or you can use [environment variable substitution](https://boxlang.ortusbooks.com/getting-started/configuration#environment-variable-substitution) to populate the datasource definition:

```js
this.datasources["mssqlDB"] = {
	driver: "mssql",
	host: "${env.MSSQL_HOST:localhost}",
	port: "${env.MSSQL_PORT:1433}",
	database: "${env.MSSQL_DATABASE:myDB}",
	username: "${env.MSSQL_USERNAME:sa}",
	password: "123456Password",
};
```

## Ortus Sponsors

BoxLang is a professional open-source project and it is completely funded by the [community](https://patreon.com/ortussolutions) and [Ortus Solutions, Corp](https://www.ortussolutions.com). Ortus Patreons get many benefits like a cfcasts account, a FORGEBOX Pro account and so much more. If you are interested in becoming a sponsor, please visit our patronage page: [https://patreon.com/ortussolutions](https://patreon.com/ortussolutions)

### THE DAILY BREAD

> "I am the way, and the truth, and the life; no one comes to the Father, but by me (JESUS)" Jn 14:1-12
