package online.courseal.courseal_android.data.api.coursemanagement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.auth.data.AuthWrapperError
import online.courseal.courseal_android.data.api.auth.CoursealAuthService
import online.courseal.courseal_android.data.api.coursemanagement.data.CourseApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.CourseApiResponse
import online.courseal.courseal_android.data.api.coursemanagement.data.CourseListData
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateCourseApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateCourseApiRequest
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateCourseApiResponse
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateLessonApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateLessonApiRequest
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateLessonApiResponse
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateTaskApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateTaskApiRequest
import online.courseal.courseal_android.data.api.coursemanagement.data.CreateTaskApiResponse
import online.courseal.courseal_android.data.api.coursemanagement.data.DeleteCourseApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.DeleteLessonApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.DeleteTaskApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.LessonListApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.LessonListData
import online.courseal.courseal_android.data.api.coursemanagement.data.StructureApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.StructureData
import online.courseal.courseal_android.data.api.coursemanagement.data.TaskListApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.TaskListData
import online.courseal.courseal_android.data.api.coursemanagement.data.UpdateCourseApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.UpdateCourseApiRequest
import online.courseal.courseal_android.data.api.coursemanagement.data.UpdateLessonApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.UpdateLessonApiRequest
import online.courseal.courseal_android.data.api.coursemanagement.data.UpdateStructureApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.UpdateTaskApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.UpdateTaskApiRequest
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLesson
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject

class CoursealCourseManagementService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao,
    private val authService: CoursealAuthService
) {
    private suspend fun courseManagementUrl(): String = "${serverDao.getCurrentServerUrl()}/api/course-management"

    suspend fun createCourse(courseName: String, courseDescription: String): ApiResult<CreateCourseApiResponse, CreateCourseApiError> = authService.authWrap(CreateCourseApiError.UNKNOWN) {
        val response = httpClient.post(courseManagementUrl()) {
            contentType(ContentType.Application.Json)
            setBody(
                CreateCourseApiRequest(
                    courseName = courseName,
                    courseDescription = courseDescription
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.CANT_CREATE_COURSES -> ApiResult.Error(AuthWrapperError.InnerError(CreateCourseApiError.CANT_CREATE_COURSES))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CreateCourseApiError.UNKNOWN))
        }
    }

    suspend fun coursesList(): ApiResult<List<CourseListData>, Unit> = authService.authWrap(Unit) {
        val response = httpClient.get(courseManagementUrl())

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            else -> ApiResult.Error(AuthWrapperError.InnerError(Unit))
        }
    }

    suspend fun courseInfo(courseId: Int): ApiResult<CourseApiResponse, CourseApiError> = authService.authWrap(
        CourseApiError.UNKNOWN) {
        val response = httpClient.get(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.COURSE_NOT_FOUND -> ApiResult.Error(AuthWrapperError.InnerError(CourseApiError.COURSE_NOT_FOUND))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CourseApiError.UNKNOWN))
        }
    }

    suspend fun updateCourseInfo(courseId: Int, courseName: String, courseDescription: String): ApiResult<Unit, UpdateCourseApiError> = authService.authWrap(UpdateCourseApiError.UNKNOWN) {
        val response = httpClient.put(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId")
            }
            contentType(ContentType.Application.Json)
            setBody(
                UpdateCourseApiRequest(
                    courseName = courseName,
                    courseDescription = courseDescription
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(UpdateCourseApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(UpdateCourseApiError.UNKNOWN))
        }
    }

    suspend fun deleteCourse(courseId: Int): ApiResult<Unit, DeleteCourseApiError> = authService.authWrap(DeleteCourseApiError.UNKNOWN) {
        val response = httpClient.delete(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(DeleteCourseApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(DeleteCourseApiError.UNKNOWN))
        }
    }

    suspend fun createTask(courseId: Int, taskName: String, task: CoursealTask): ApiResult<CreateTaskApiResponse, CreateTaskApiError> = authService.authWrap(
        CreateTaskApiError.UNKNOWN) {
        val response = httpClient.post(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "task")
            }
            contentType(ContentType.Application.Json)
            setBody(
                CreateTaskApiRequest(
                    taskName = taskName,
                    task = task
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(CreateTaskApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CreateTaskApiError.UNKNOWN))
        }
    }

    suspend fun tasksList(courseId: Int): ApiResult<List<TaskListData>, TaskListApiError> = authService.authWrap(
        TaskListApiError.UNKNOWN) {
        val response = httpClient.get(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "task")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(TaskListApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(TaskListApiError.UNKNOWN))
        }
    }

    suspend fun updateTask(courseId: Int, taskId: Int, taskName: String, task: CoursealTask): ApiResult<Unit, UpdateTaskApiError> = authService.authWrap(
        UpdateTaskApiError.UNKNOWN) {
        val response = httpClient.put(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "task", "$taskId")
            }
            contentType(ContentType.Application.Json)
            setBody(
                UpdateTaskApiRequest(
                    taskName = taskName,
                    task = task
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(UpdateTaskApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(UpdateTaskApiError.UNKNOWN))
        }
    }

    suspend fun deleteTask(courseId: Int, taskId: Int): ApiResult<Unit, DeleteTaskApiError> = authService.authWrap(DeleteTaskApiError.UNKNOWN) {
        val response = httpClient.delete(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "task", "$taskId")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(DeleteTaskApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(DeleteTaskApiError.UNKNOWN))
        }
    }

    suspend fun createLesson(courseId: Int, lessonName: String, progressNeeded: Int, lesson: CoursealLesson):
            ApiResult<CreateLessonApiResponse, CreateLessonApiError> = authService.authWrap(CreateLessonApiError.UNKNOWN) {
        val response = httpClient.post(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "lesson")
            }
            contentType(ContentType.Application.Json)
            setBody(
                CreateLessonApiRequest(
                    lessonName = lessonName,
                    lessonProgressNeeded = progressNeeded,
                    lesson = lesson
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(CreateLessonApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(CreateLessonApiError.UNKNOWN))
        }
    }

    suspend fun lessonsList(courseId: Int): ApiResult<List<LessonListData>, LessonListApiError> = authService.authWrap(LessonListApiError.UNKNOWN) {
        val response = httpClient.get(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "lesson")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(LessonListApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(LessonListApiError.UNKNOWN))
        }
    }

    suspend fun updateLesson(courseId: Int, lessonId: Int, lessonName: String, progressNeeded: Int, lesson: CoursealLesson):
            ApiResult<Unit, UpdateLessonApiError> = authService.authWrap(UpdateLessonApiError.UNKNOWN) {
        val response = httpClient.put(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "lesson", "$lessonId")
            }
            contentType(ContentType.Application.Json)
            setBody(
                UpdateLessonApiRequest(
                    lessonName = lessonName,
                    lessonProgressNeeded = progressNeeded,
                    lesson = lesson
                )
            )
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(UpdateLessonApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(UpdateLessonApiError.UNKNOWN))
        }
    }

    suspend fun deleteLesson(courseId: Int, lessonId: Int): ApiResult<Unit, DeleteLessonApiError> = authService.authWrap(DeleteLessonApiError.UNKNOWN) {
        val response = httpClient.delete(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "lesson", "$lessonId")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(DeleteLessonApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(DeleteLessonApiError.UNKNOWN))
        }
    }

    suspend fun getStructure(courseId: Int): ApiResult<List<List<StructureData>>, StructureApiError> = authService.authWrap(StructureApiError.UNKNOWN) {
        val response = httpClient.get(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "structure")
            }
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(response.body())
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(StructureApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(StructureApiError.UNKNOWN))
        }
    }

    suspend fun updateStructure(courseId: Int, structure: List<List<StructureData>>): ApiResult<Unit, UpdateStructureApiError> = authService.authWrap(UpdateStructureApiError.UNKNOWN) {
        val response = httpClient.put(courseManagementUrl()) {
            url {
                appendPathSegments("$courseId", "structure")
            }
            contentType(ContentType.Application.Json)
            setBody(structure)
        }

        return@authWrap if (response.status.value in 200..299)
            ApiResult.Success(Unit)
        else when (response.body<ErrorResponse>().error) {
            ErrorResponseType.JWT_INVALID -> ApiResult.Error(AuthWrapperError.JWTInvalid())
            ErrorResponseType.NO_PERMISSIONS -> ApiResult.Error(AuthWrapperError.InnerError(UpdateStructureApiError.NO_PERMISSIONS))
            else -> ApiResult.Error(AuthWrapperError.InnerError(UpdateStructureApiError.UNKNOWN))
        }
    }
}