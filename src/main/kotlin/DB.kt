import org.jetbrains.exposed.sql.Database

object DB {
    private val host = System.getenv("DB_HOST") ?: "localhost"
    private val port = System.getenv("DB_PORT")?.toIntOrNull() ?: 5432
    private val dbName = System.getenv("DB_NAME") ?: "ktor_db"
    private val dbUser = System.getenv("DB_USER") ?: "ktor_admin"
    private val dbPassword = System.getenv("DB_PASSWORD") ?: "ktor"

    fun connect() =
        Database.connect(
            "jdbc:postgresql://$host:$port/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword,
        )
}