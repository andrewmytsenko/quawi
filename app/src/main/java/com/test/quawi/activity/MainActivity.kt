package com.test.quawi.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.quawi.R
import com.test.quawi.adapters.ProjectsAdapter
import com.test.quawi.models.ProjectModel
import com.test.quawi.models.ProjectsModel
import com.test.quawi.retrofit.RequestResult
import com.test.quawi.retrofit.Requests
import com.test.quawi.utils.ProgressBar
import com.test.quawi.utils.SharedPref
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.missing_projects.*
import retrofit2.Response
import java.net.HttpURLConnection


class MainActivity : AppCompatActivity() {
    private lateinit var projects: ArrayList<ProjectModel>
    private lateinit var adapter: ProjectsAdapter

    private val progressBar = ProgressBar()
    private val requests = Requests()
    private val prefs = SharedPref()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }

    private fun initUI() {
        projects = arrayListOf()
        requests.initRetrofitClient()
        getProjects()
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(projects_toolbar)
    }

    private fun initRecycleCategoryGallery(projects: ArrayList<ProjectModel>) {
        if (projects.isNotEmpty()) {
            project_recycler.layoutManager = LinearLayoutManager(this)
            project_recycler.setHasFixedSize(true)
            adapter = ProjectsAdapter(projectsOnItemClickListener)
            adapter.addItems(projects)

            project_recycler.adapter = adapter
        } else {
            missing_projects.visibility = View.VISIBLE
        }
    }

    private fun getProjects() {
        progressBar.show(project_progress_bar)

        val token = prefs.getFromPref(this, "token", "")

        projects.clear()
        requests.getProjects(token, object : RequestResult<ProjectsModel> {
            override fun onSuccess(response: Response<ProjectsModel>) {
                doOnChangeProjectSuccess(response)
            }

            override fun onError(t: Throwable) {
                doOnChangeProjectFailure(t)
            }

        })
    }

    private fun doOnChangeProjectSuccess(response: Response<ProjectsModel>) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            response.body()?.projects?.forEach {
                projects.add(it)
            }

            progressBar.hide(project_progress_bar)

            initRecycleCategoryGallery(projects)
        } else {
            progressBar.hide(project_progress_bar)
        }
    }

    private fun doOnChangeProjectFailure(t: Throwable) {
        progressBar.hide(project_progress_bar)
        missing_projects.visibility = View.VISIBLE
        missing_projects_view.text = getString(R.string.missing_internet_connection)
    }

    private val projectsOnItemClickListener = object : ProjectsAdapter.OnItemClickListener {
        override fun onClickItem(projectModel: ProjectModel) {
            ProjectInfoActivity.gotoProjectInfoPage(this@MainActivity, projectModel)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_setting -> {
                prefs.clearPrefs(this)
                LoginActivity.gotoLoginPage(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun gotoMainPage(from: Activity) {
            val intent = Intent(from, MainActivity::class.java)
            from.startActivity(intent)
            from.finish()
        }
    }
}