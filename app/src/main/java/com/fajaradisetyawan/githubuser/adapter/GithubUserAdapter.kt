package com.fajaradisetyawan.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.githubuser.R
import com.fajaradisetyawan.githubuser.model.GithubUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item.view.*

class GithubUserAdapter(private val githubList: ArrayList<GithubUser>) : RecyclerView.Adapter<GithubUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setGithubUser(githubUsers: ArrayList<GithubUser>){
        githubList.clear()
        githubList.addAll(githubUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        ))
    }

    override fun getItemCount(): Int = githubList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(githubList[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(githubList[holder.adapterPosition])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(githubUser: GithubUser){
            with(itemView){
                Picasso.get().load(githubUser.avatar_url).placeholder(R.drawable.ic_baseline_account_circle_24).into(img_avatar)
                tv_username.text = githubUser.login
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(githubUser: GithubUser)
    }
}