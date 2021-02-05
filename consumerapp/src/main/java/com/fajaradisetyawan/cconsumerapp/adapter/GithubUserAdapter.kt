package com.fajaradisetyawan.cconsumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fajaradisetyawan.cconsumerapp.R
import com.fajaradisetyawan.cconsumerapp.model.GithubUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item.view.*

class GithubUserAdapter(private val githubUser: ArrayList<GithubUser>) : RecyclerView.Adapter<GithubUserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setUser(user: ArrayList<GithubUser>){
        githubUser.clear()
        githubUser.addAll(user)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        ))
    }

    override fun getItemCount(): Int = githubUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(githubUser[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(githubUser[holder.adapterPosition])
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(githubUser: GithubUser) {
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