package com.test.quawi.retrofit

import com.test.quawi.models.LoginModel
import com.test.quawi.models.LoginResponseModel
import com.test.quawi.models.ProjectNameModel
import com.test.quawi.models.ProjectsModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @Headers(
        "Content-Type: application/json",
        "Client-Timezone-Offset: 60",
        "Client-Language: ru-RU",
        "Client-Company: udimiteam",
        "Client-Device: android"
    )

    @POST("auth/login")
    fun login(@Body body: LoginModel): Call<LoginResponseModel>

    @Multipart
    @POST("projects-manage/update?")
    fun changeProjectName(
        @Header("Authorization") token: String,
        @Query("id") projectId: String,
        @Part name: MultipartBody.Part
    ): Call<ProjectNameModel>

    @GET("projects-manage/index")
    fun getProjects(@Header("Authorization") token: String): Call<ProjectsModel>
}