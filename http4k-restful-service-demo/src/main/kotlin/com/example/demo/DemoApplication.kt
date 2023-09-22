package com.example.demo

import com.example.demo.controller.UserController
import com.example.demo.service.DefaultUserServiceImpl
import com.example.demo.service.UserService
import com.google.inject.AbstractModule
import com.google.inject.Guice
import org.http4k.contract.ContractRoute
import org.http4k.contract.ContractRoutingHttpHandler
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.contract.ui.swagger.swaggerUiWebjar
import org.http4k.core.*
import org.http4k.filter.CachingFilters
import org.http4k.format.Jackson
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

class MainGuiceModule : AbstractModule() {
    override fun configure() {
        bind(UserService::class.java).to(DefaultUserServiceImpl::class.java)
    }
}

fun createContractHandler(routes: List<ContractRoute>, descriptionPath: String): ContractRoutingHttpHandler {
    return contract {
        this.routes += routes
        renderer = OpenApi3(
                ApiInfo("User API", "v1.0"),
                Jackson,
                servers = listOf(ApiServer(Uri.of("http://localhost:8080/"), "local server"))
        )
        this.descriptionPath = descriptionPath
    }
}

val timingFilter = Filter { next: HttpHandler ->
    { req: Request ->
        val start = System.currentTimeMillis()
        val resp: Response = next(req)
        val timeElapsed = System.currentTimeMillis() - start
        println("[timing filter] request to ${req.uri} took ${timeElapsed}ms")
        resp
    }
}

fun main() {
    // guice
    val injector = Guice.createInjector(MainGuiceModule())
    val userController = injector.getInstance(UserController::class.java)

    // app
    val app: HttpHandler = routes(
            createContractHandler(userController.routes, "/openapi.json"),
            "/swagger" bind swaggerUiWebjar {
                url = "/openapi.json"
            }
    )

    // start
    val filteredApp: HttpHandler = CachingFilters.Response.NoCache().then(timingFilter).then(app)
    filteredApp.asServer(SunHttp(8080)).start().block()
}