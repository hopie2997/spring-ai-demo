package com.hopie.springaidemo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description

@Configuration
class FunctionConfiguration {

    @Bean
    @Description("This function is used to get the MFV employees information.")
    fun companyEmployee(): java.util.function.Function<CompanyService.Request, CompanyService.Response> {
        return CompanyService()
    }
}

class CompanyService : java.util.function.Function<CompanyService.Request, CompanyService.Response> {

    private val employees = listOf(
        Employee("001", "John"),
        Employee("002", "Doe"),
        Employee("003", "Jane"),
        Employee("004", "Smith"),
    )

    data class Request(
        val query: String
    )

    data class Response(
        val employees: List<Employee>
    )

    data class Employee(
        val employeeCode: String,
        val employeeName: String,
    )

    override fun apply(request: Request): Response {
        return Response(employees)
    }

}
