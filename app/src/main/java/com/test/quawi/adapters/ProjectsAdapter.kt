package com.test.quawi.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.quawi.R
import com.test.quawi.models.ProjectModel
import kotlinx.android.synthetic.main.project_item.view.*

class ProjectsAdapter(private val onItemClickListener: OnItemClickListener) :
    BaseRecyclerAdapter<ProjectModel, ProjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_item, parent, false)
        return ProjectsViewHolder(view, onItemClickListener)
    }

    interface OnItemClickListener {
        fun onClickItem(projectModel: ProjectModel)
    }
}

class ProjectsViewHolder(
    itemView: View,
    private val onItemClickListener: ProjectsAdapter.OnItemClickListener
) : BaseViewHolder<ProjectModel>(itemView) {
    private lateinit var item: ProjectModel
    override fun bindItem(item: ProjectModel) {
        this.item = item
        Glide.with(itemView.context).load(item.logo_url)
            .apply(RequestOptions().placeholder(R.drawable.avatar_placeholder))
            .into(itemView.project_avatar)

        itemView.project_name.text = item.name

        when (item.is_active) {
            ProjectStatus.ACTIVE -> {
                itemView.project_status.text = itemView.context.getString(R.string.active)
                itemView.project_status.setTextColor(Color.parseColor(itemView.context.getString(R.string.green_color)))
            }

            ProjectStatus.INACTIVE -> {
                itemView.project_status.text = itemView.context.getString(R.string.inactive)
                itemView.project_status.setTextColor(Color.parseColor(itemView.context.getString(R.string.red_color)))
            }
        }

        when (item.users.size) {
            WorkersCount.ZERO -> itemView.project_workers.text =
                itemView.context.getString(R.string.workers, "No")
            WorkersCount.ONE -> itemView.project_workers.text =
                itemView.context.getString(R.string.worker, "1")
            else -> itemView.project_workers.text =
                itemView.context.getString(R.string.workers, item.users.size.toString())
        }
    }

    init {
        itemView.setOnClickListener {
            onItemClickListener.onClickItem(item)
        }
    }

    object WorkersCount {
        const val ZERO = 0
        const val ONE = 1
    }

    object ProjectStatus {
        const val ACTIVE = 1
        const val INACTIVE = 0
    }
}