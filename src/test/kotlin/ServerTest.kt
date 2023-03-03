import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ServerTest {
    @Test
    fun testStatus() = testApplication {
        application {
            mainModule()
        }
        val response = client.get("/status")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("{\"status\":\"OK\"}", response.bodyAsText())
    }
}