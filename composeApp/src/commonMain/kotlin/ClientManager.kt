import androidx.compose.runtime.snapshots.SnapshotStateList
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import models.*
import org.jetbrains.compose.resources.Resource
import java.time.LocalDateTime

object ClientManager {
    private val client = HttpClient{
      defaultRequest {
          url("http://10.243.113.142:8080")
      }
    }

    suspend fun auth(login: String, password: String) : Employee
        = Json.decodeFromString<Employee>(
            client.submitForm(
            "/auth",
            formParameters = parameters {
                append("login", login)
                append("password", password)
            }
        ).bodyAsText()
        )

    suspend fun getDepartmentName(id: Int): String
        = client.get("/department/${id}").bodyAsText()

    suspend fun getEmployeeCompleting(id: Int): Map<String, Completing>
        = Json.decodeFromString<Map<String, Completing>>(
            client.get("/completings/$id").bodyAsText()
        )

    suspend fun changeCompletingStatus(completing: Completing, newStatus: Statuses){
        client.submitForm(
            url = "/completings/edit",
            formParameters = parameters{
                append("id", completing.id.toString())
                append("employeeId", completing.employeeId.toString())
                append("taskId", completing.taskId.toString())
                append("status", newStatus.text)
            }

        )
    }

    suspend fun editingTask(editedTask: Task): String
    = client
        .submitForm(
            url = "/tasks/edit",
            formParameters = parameters{
                append("id", editedTask.id.toString())
                append("name", editedTask.name)
                append("description", editedTask.description)
                append("amount", editedTask.amount.toString())
                append("employeeId", editedTask.employeeId.toString())
                append("creatingDate", editedTask.creatingDate.toString())
                append("endingDate", editedTask.endingDate.toString())

            }
        )
        .bodyAsText()

    suspend fun createTask(
        name: String,
        description: String,
        employeeId: Int,
        amount: Double,
        creatingDate: LocalDateTime,
        endingDate: LocalDateTime
    ): String
            = client
        .submitForm(
            url = "/tasks/create",
            formParameters = parameters{
                append("name", name)
                append("description", description)
                append("amount", amount.toString())
                append("employeeId", employeeId.toString())
                append("creatingDate", creatingDate.toString())
                append("endingDate", endingDate.toString())
            }
        )
        .bodyAsText()

    suspend fun deletingTask(id: Int): String
            = client
        .submitForm(
            url = "/tasks/delete",
            formParameters = parameters{
                append("id", id.toString())
            }
        )
        .bodyAsText()

    suspend fun getAllDepartmentTask(id: Int): Map<String, Task> =
        Json.decodeFromString<Map<String, Task>>(
            client.get("tasks/all/$id").bodyAsText()
        )

    suspend fun getAuditionDepartmentTask(id: Int): Map<String, Completing> =
        Json.decodeFromString<Map<String, Completing>>(
            client.submitForm(
                "/completing/audition",
                formParameters = parameters {
                    append(
                        "departmentId", id.toString()
                    )
                }
                ).bodyAsText()
        )

    suspend fun createCompleting(id: Int, taskId: Int){
        client.submitForm(
            "/completing/add",
            formParameters = parameters{
                append("taskId", taskId.toString())
                append("employeeId", id.toString())
            }
        )
    }

    suspend fun changeMoney(
        id: Int,
        value: Double
    ){
        client.get("/employee/${id}/changemoney/${value}")
    }

    suspend fun getEmployee(id: Int) : Employee
        = Json.decodeFromString<Employee>(
            client.get("/employee/$id").bodyAsText()
        )

    suspend fun departmentResults(isLastMonth: Boolean)
        = Json.decodeFromString<List<String>>(
            client.get("/departments/results/${isLastMonth}").bodyAsText()
        )

    suspend fun giveSalary() : String
        = client.get("/salary").bodyAsText()

    suspend fun getAllEmployees() : List<Employee>
        = Json.decodeFromString<List<Employee>>(client.get("/employees").bodyAsText())

    suspend fun getAllDepartments() : List<Department>
            = Json.decodeFromString<List<Department>>(client.get("/departments").bodyAsText())


    suspend fun deleteEmployee(id: Int){
        client.get("/employee/delete/${id}")
    }

    suspend fun editEmployee(
        id: Int,
        surname: String,
        name: String,
        fathername: String,
        login: String,
        password: String,
        accessLevel: String,
        bonusMoney: Double,
        departmentId: Int
    ) =
        client.submitForm(
            "/employee/edit",
            formParameters = parameters{
                append("id", id.toString())
                append("surname", surname)
                append("name", name)
                append("fathername", fathername)
                append("login", login)
                append("password", password)
                append("accessLevel", accessLevel)
                append("bonusMoney", bonusMoney.toString())
                append("departmentId", departmentId.toString())
            }
        ).bodyAsText()

    suspend fun addEmployee(
        surname: String,
        name: String,
        fathername: String,
        login: String,
        password: String,
        accessLevel: String,
        bonusMoney: Double,
        departmentId: Int
    ) =
        client.submitForm(
            "/employee/add",
            formParameters = parameters{
                append("surname", surname)
                append("name", name)
                append("fathername", fathername)
                append("login", login)
                append("password", password)
                append("accessLevel", accessLevel)
                append("bonusMoney", bonusMoney.toString())
                append("departmentId", departmentId.toString())
            }
        ).bodyAsText()

    suspend fun getPenalties(
        filter: String
    ) : List<Penalty>
        =
        if (filter.isBlank())
            Json.decodeFromString<List<Penalty>>(
                client.get(
                    "/penalty/"
                ).bodyAsText()
            )
        else
        Json.decodeFromString<List<Penalty>>(
            client.get(
                "/penalty/$filter"
            ).bodyAsText()
        )

    suspend fun newPenalty(
        name: String,
        description: String,
        amount: Double
    )
    = client.submitForm(
            url = "/penalties/new",
            formParameters = parameters {
                append("name", name)
                append("desc", description)
                append("amount", amount.toString())
            }
        ).bodyAsText()

    suspend fun getAllEmployeesPenaltings(id : Int)
        = Json.decodeFromString<Map<String, Penalting>>(
            client.get("/penaltings/all/$id").bodyAsText()
        )

    suspend fun getAllDepEmployees(id : Int) = Json.decodeFromString<List<Employee>>(
        client.get("/employee/all/$id").bodyAsText()
    )

    suspend fun getEmployeesPenaltings(id: Int)
    = Json.decodeFromString<Map<String, Penalting>>(
        client.get("/penaltings/personal/$id").bodyAsText()
    )

    suspend fun addNewPenalting(
        employeeId: Int,
        penaltyId: Int
    ){
        client.submitForm(
         url = "/penalting",
            formParameters = parameters {
                append("employeeId", employeeId.toString())
                append("penaltyId", penaltyId.toString())
            }
        )
    }

}