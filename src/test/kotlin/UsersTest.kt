import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import user.UsersTable

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersTest {
    @BeforeAll
    fun setup() {
        DB.connect()
        transaction {
            SchemaUtils.create(UsersTable)
        }
    }

    @AfterAll
    fun cleanup() {
        DB.connect()
        transaction {
            SchemaUtils.drop(UsersTable)
        }
    }

    @Test
    fun `POST creates a new user`() = testApplication {
        application {
            mainModule()
        }
        val response = client.post("/users") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(listOf(
                "name" to "thecoolest",
                "email" to "the@coolest.com"
            ).formUrlEncode())
        }
        Assertions.assertEquals(HttpStatusCode.Created, response.status)
    }

    @Nested
    inner class `With user in DB` {
        lateinit var id: EntityID<Int>
        @BeforeEach
        fun setup() {
            DB.connect()
            id = transaction {
                UsersTable.deleteAll()
                UsersTable.insertAndGetId {
                    it[name] = NAME
                    it[email] = EMAIL
                }
            }
        }

        @AfterEach
        fun tearDown() {
            DB.connect()
            transaction {
                UsersTable.deleteAll()
            }
        }
        @Test
        fun `GET with ID fetches a single user`() = testApplication {
            application {
                mainModule()
            }
            val response = client.get("/users/$id")
            Assertions.assertEquals("""{"id":$id,"name":"$NAME","email":"$EMAIL"}""", response.bodyAsText())
        }

        @Test
        fun `GET without ID fetches all users`() = testApplication {
            application {
                mainModule()
            }
            val response = client.get("/users")
            Assertions.assertEquals("""[{"id":$id,"name":"$NAME","email":"$EMAIL"}]""", response.bodyAsText())
        }
    }

    companion object {
        const val NAME = "hollabakkacha"
        const val EMAIL = "numba1mimmik@shout.com"
    }
}