package com.test.quawi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.test.quawi.R
import com.test.quawi.models.LoginResponseModel
import com.test.quawi.retrofit.RequestResult
import com.test.quawi.retrofit.Requests
import com.test.quawi.utils.ProgressBar
import com.test.quawi.utils.SharedPref
import com.test.quawi.utils.Validation
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response
import java.net.HttpURLConnection

class LoginActivity : AppCompatActivity() {

    private val progressBar = ProgressBar()
    private val validation = Validation()
    private val requests = Requests()
    private val prefs = SharedPref()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
    }

    private fun initUI() {
        requests.initRetrofitClient()
        initButtonClick()
        checkToken()
    }

    private fun initButtonClick() {
        login_button.setOnClickListener {
            val login = login_email.text.toString()
            val pass = login_pass.text.toString()

            when {
                validation.isPasswordValid(login) && validation.isPasswordValid(pass) -> loginUser(
                    login,
                    pass
                )
                validation.isEmailValid(login) && !validation.isPasswordValid(pass) -> login_email.error =
                    getString(R.string.pass_to_short)
                !validation.isEmailValid(login) && validation.isPasswordValid(pass) -> login_email.error =
                    getString(R.string.incorrect_email)
                else -> login_email.error = null
            }
        }
    }

    private fun loginUser(login: String, email: String) {
        progressBar.show(login_progress_bar)

        requests.loginUser(login, email, object : RequestResult<LoginResponseModel> {
            override fun onSuccess(response: Response<LoginResponseModel>) {
                doOnLoginSuccess(response)
            }

            override fun onError(t: Throwable) {
                doOnLoginFailure(t)
            }

        })
    }

    private fun doOnLoginSuccess(response: Response<LoginResponseModel>) {
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

    private fun doOnLoginFailure(t: Throwable) {
        Log.e("LoginError", t.message.toString())
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