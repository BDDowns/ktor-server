import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        port = 8080,
        module = Application::mainModule
    ).start(wait = true)
}

fun Application.mainModule() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/status") {
            call.respond(mapOf("status" to "OK"))
        }
    }
}