package com.test.quawi.retrofit

import com.test.quawi.models.LoginModel
import com.test.quawi.models.LoginResponseModel
import com.test.quawi.models.ProjectNameModel
import com.test.quawi.models.ProjectsModel
import com.test.quawi.utils.API_URL
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

interface RequestResult<T> {
    fun onSuccess(response: Response<T>)
    fun onError(t: Throwable)
}

class Requests {
    private lateinit var apiService: APIService

    fun initRetrofitClient() {
        apiService = Client.getClient(API_URL).create(APIService::class.java)
    }

    fun changeProjectName(
        projectId: String,
        name: MultipartBody.Part,
        token: String,
        result: RequestResult<ProjectNameModel>
    ) {
        apiService.changeProjectName("Bearer $token", projectId, name)
            .enqueue(object : retrofit2.Callback<ProjectNameModel> {
                override fun onFailure(call: Call<ProjectNameModel>, t: Throwable) {
                    result.onError(t)
                }

                override fun onResponse(
                    call: Call<ProjectNameModel>,
                    response: Response<ProjectNameModel>
                ) {
                    result.onSuccess(response)
                }
            })
    }

    fun getProjects(token: String, result: RequestResult<ProjectsModel>) {
        apiService.getProjects("Bearer $token").enqueue(object : retrofit2.Callback<ProjectsModel> {
            override fun onFailure(call: Call<ProjectsModel>, t: Throwable) {
                result.onError(t)
            }

            override fun onResponse(
                call: Call<ProjectsModel>,
                response: Response<ProjectsModel>
            ) {
                result.onSuccess(response)
            }
        })
    }

    fun loginUser(login: String, email: String, result: RequestResult<LoginResponseModel>) {
        apiService.login(LoginModel(login, email))
            .enqueue(object : retrofit2.Callback<LoginResponseModel> {
                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    result.onError(t)
                }

                override fun onResponse(
                    call: Call<LoginResponseModel>,
                    response: Response<LoginResponseModel>
                ) {
                    result.onSuccess(response)
                }

            })
    }

}