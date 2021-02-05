package com.fajaradisetyawan.cconsumerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fajaradisetyawan.cconsumerapp.R
import com.fajaradisetyawan.cconsumerapp.model.GithubUser
import com.fajaradisetyawan.cconsumerapp.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val login = intent.getParcelableExtra<GithubUser>(EXTRA_DETAIL)
        val id = login?.id
        val username = login?.login.toString()
        val type = login?.type.toString()
        val avatar = login?.avatar_url.toString()
        initToolbar()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        detailViewModel.setDetail(username)
        detailViewModel.getDetail().observe(this, Observer { user ->
            if (user != null){
                Picasso.get().load(user.avatar_url).placeholder(R.drawable.ic_baseline_account_circle_24).into(img_avatar)
                txt_fullname.text = user.name
                txt_username.text = user.login
                txt_location.text = user.location
                txt_repo.text = user.public_repos.toString()
                txt_follower.text = user.followers.toString()
                txt_following.text = user.following.toString()
                progressDetail.visibility = View.INVISIBLE
            }
        })

        detailViewModel.noConnection().observe(this, Observer { result ->
            progressDetail.visibility = View.INVISIBLE
        })

    }

    private fun initToolbar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "$txt_username\'s Profile"
    }
}