package user

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.users() {
    route("/users") {
        get("/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val resultRow = transaction {
                UsersTable.select {
                    UsersTable.id.eq(id)
                }.firstOrNull()
            }
            if (resultRow == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond( User.fromResultRow(resultRow) )
            }
        }
        get {
            val users = transaction {
                UsersTable
                    .selectAll()
                    .map { User.fromResultRow(it) }.toList()
            }
            call.respond(users)
        }

        post {
            val parameters = call.receiveParameters()
            println(parameters.toString())
            val name = requireNotNull(parameters["name"]). also { it.validateName() }
            val email = requireNotNull(parameters["email"]). also { it.validateEmail() }
            transaction {
                UsersTable.insert { user ->
                    user[UsersTable.name] = name
                    user[UsersTable.email] = email
                }
            }
            call.respond(HttpStatusCode.Created)
        }
    }
}