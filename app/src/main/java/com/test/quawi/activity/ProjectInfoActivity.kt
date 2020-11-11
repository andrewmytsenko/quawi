package com.test.quawi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.quawi.R
import com.test.quawi.adapters.ProjectUsersAdapter
import com.test.quawi.models.ProjectModel
import com.test.quawi.models.ProjectNameModel
import com.test.quawi.models.UserModel
import com.test.quawi.retrofit.RequestResult
import com.test.quawi.retrofit.Requests
import com.test.quawi.utils.EditProjectNamePopup
import com.test.quawi.utils.NetworkUtils
import com.test.quawi.utils.ProgressBar
import com.test.quawi.utils.SharedPref
import kotlinx.android.synthetic.main.activity_project_info.*
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

class ProjectInfoActivity : AppCompatActivity() {
    private lateinit var adapter: ProjectUsersAdapter

    private var users: ArrayList<UserModel>? = null

    private val networkUtils = NetworkUtils()
    private val progressBar = ProgressBar()
    private val requests = Requests()
    private val prefs = SharedPref()

    private val projectInfo: ProjectModel? by lazy {
        intent.extras?.getParcelable<ProjectModel>("projectModel")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_info)

        initUI()
    }

    private fun initUI() {
        requests.initRetrofitClient()
        initProjectInfo()
        initRecyclerView()
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(project_info_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        project_info_toolbar.setNavigationOnClickListener {
            MainActivity.gotoMainPage(this)
        }
    }

    private fun changeProjectName(name: String) {
        val token = prefs.getFromPref(this, "token", "")
        val multipartName = networkUtils.multipartText(name)
        val projectId = projectInfo?.id.toString()

        progressBar.show(project_info_progress_bar)

        requests.changeProjectName(
            projectId,
            multipartName,
            token,
            object : RequestResult<ProjectNameModel> {
                override fun onSuccess(response: Response<ProjectNameModel>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {

                        Handler(Looper.getMainLooper()).postDelayed({
                            progressBar.hide(project_info_progress_bar)
                            project_name.text = name
                        }, 1500)
                    } else {
                        progressBar.hide(project_info_progress_bar)
                    }
                }

                override fun onError(t: Throwable) {
                    progressBar.hide(project_info_progress_bar)
                    Toast.makeText(
                        this@ProjectInfoActivity,
                        getString(R.string.smth_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun initRecyclerView() {
        users = projectInfo?.users

        if (users?.isNotEmpty()!!) {

            val numberOfColumns = 2
            project_info_recycler.layoutManager = GridLayoutManager(
                this,
                numberOfColumns
            )
            adapter = ProjectUsersAdapter(usersOnItemClickListener)
            adapter.addItems(users!!)

            project_info_recycler.adapter = adapter
        }
    }

    private val usersOnItemClickListener = object : ProjectUsersAdapter.OnItemClickListener {
        override fun onClickItem(userModel: UserModel) {

        }
    }

    private fun initProjectInfo() {
        when (projectInfo?.is_active) {
            1 -> active_switch.isChecked = true
            0 -> active_switch.isChecked = false
        }

        project_name.text = projectInfo?.name

        Glide.with(applicationContext).load(projectInfo?.logo_url)
            .apply(RequestOptions().placeholder(R.drawable.avatar_placeholder))
            .into(project_info_avatar)

        when (projectInfo?.dta_user_since) {
            null -> {
                tickets_visibility.text = getString(R.string.all_time)
            }

            else -> {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val newFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
                val date = dateFormat.parse(projectInfo?.dta_user_since)

                tickets_visibility.text = newFormat.format(date)
            }
        }

        when (projectInfo?.is_owner_watcher) {
            1 -> watcher_switch.isChecked = true
            0 -> watcher_switch.isChecked = false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_info_change_topic -> {
                EditProjectNamePopup(this, projectInfo?.name.toString()) { newName ->
                    changeProjectName(newName)
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun gotoProjectInfoPage(from: Activity, projectModel: ProjectModel) {
            val intent = Intent(from, ProjectInfoActivity::class.java)
            intent.putExtra("projectModel", projectModel)
            from.startActivity(intent)
            from.finish()
        }
    }
}