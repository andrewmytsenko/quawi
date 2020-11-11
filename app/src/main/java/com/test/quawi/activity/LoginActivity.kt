package com.test.quawi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.test.quawi.R
import com.test.quawi.retrofit.APIService
import com.test.quawi.retrofit.Client
import com.test.quawi.models.LoginModel
import com.test.quawi.models.LoginResponseModel
import com.test.quawi.utils.API_URL
import com.test.quawi.utils.ProgressBar
import com.test.quawi.utils.SharedPref
import com.test.quawi.utils.Validation
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: APIService

    private val progressBar = ProgressBar()
    private val prefs = SharedPref()
    private val validation = Validation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
    }

    private fun initUI() {
        initRetrofitClient()
        initButtonClick()
        checkToken()
    }

    private fun initRetrofitClient() {
        apiService = Client.getClient(API_URL).create(APIService::class.java)
    }

    private fun initButtonClick() {
        login_button.setOnClickListener {
            val login = login_email.text.toString()
            val pass = login_pass.text.toString()

            when {
                validation.isPasswordValid(login) && validation.isPasswordValid(pass) -> loginUser(login, pass)
                validation.isEmailValid(login) && !validation.isPasswordValid(pass) -> login_email.error = getString(R.string.pass_to_short)
                !validation.isEmailValid(login) && validation.isPasswordValid(pass) -> login_email.error = getString(R.string.incorrect_email)
                else -> login_email.error = null
            }
        }
    }

    private fun loginUser(login: String, email: String) {
        progressBar.show(login_progress_bar)

        apiService.login(LoginModel(login, email))
            .enqueue(object : retrofit2.Callback<LoginResponseModel> {
                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    Log.e("LoginError", t.message.toString())
                }

                override fun onResponse(
                    call: Call<LoginResponseModel>,
                    response: Response<LoginResponseModel>
                ) {
                    when (HttpURLConnection.HTTP_OK) {
                        response.code() -> {
                            val token = response.body()?.token.toString()
                            Log.i("LoginSuccess", token)

                            prefs.saveToPref(this@LoginActivity, "token", token)
                            progressBar.hide(login_progress_bar)
                            MainActivity.gotoMainPage(this@LoginActivity)
                        }
                        else -> {
                            Log.e("LoginError", getString(R.string.wrong_pass_or_email))
                            progressBar.hide(login_progress_bar)
                            error_message.visibility = View.VISIBLE
                        }
                    }
                }

            })

    }

    private fun checkToken() {
        if (prefs.getFromPref(this, "token", "") != "") {
            MainActivity.gotoMainPage(this@LoginActivity)
        }
    }

    companion object {
        fun gotoLoginPage(from: Activity) {
            val intent = Intent(from, LoginActivity::class.java)
            from.startActivity(intent)
            from.finish()
        }
    }
}