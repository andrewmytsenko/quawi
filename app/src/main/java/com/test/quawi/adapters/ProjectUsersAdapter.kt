package com.test.quawi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.quawi.R
import com.test.quawi.models.UserModel
import kotlinx.android.synthetic.main.user_item.view.*

class ProjectUsersAdapter(private val onItemClickListener: OnItemClickListener) :
    BaseRecyclerAdapter<UserModel, UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UsersViewHolder(view, onItemClickListener)
    }

    interface OnItemClickListener {
        fun onClickItem(userModel: UserModel)
    }
}

class UsersViewHolder(
    itemView: View,
    private val onItemClickListener: ProjectUsersAdapter.OnItemClickListener
) : BaseViewHolder<UserModel>(itemView) {
    private lateinit var item: UserModel
    override fun bindItem(item: UserModel) {
        this.item = item

        Glide.with(itemView.context).load(item.avatar_url)
            .apply(RequestOptions().placeholder(R.drawable.avatar_placeholder))
            .into(itemView.user_avatar)

        itemView.user_name.text = item.name
    }

    init {
        itemView.setOnClickListener {
            onItemClickListener.onClickItem(item)
        }
    }
}