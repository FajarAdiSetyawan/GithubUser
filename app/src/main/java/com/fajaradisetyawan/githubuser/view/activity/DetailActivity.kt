package com.fajaradisetyawan.githubuser.view.activity

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fajaradisetyawan.githubuser.R
import com.fajaradisetyawan.githubuser.adapter.ViewPagerAdapter
import com.fajaradisetyawan.githubuser.db.DatabaseContract
import com.fajaradisetyawan.githubuser.db.DatabaseContract.UserColum.Companion.CONTENT_URI
import com.fajaradisetyawan.githubuser.db.GithubHelper
import com.fajaradisetyawan.githubuser.model.GithubUser
import com.fajaradisetyawan.githubuser.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import www.sanju.motiontoast.MotionToast

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var githubHelper: GithubHelper
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val login = intent.getParcelableExtra<GithubUser>(EXTRA_DETAIL)
        val id = login?.id
        val username = login?.login.toString()
        val type = login?.type.toString()
        val avatar = login?.avatar_url.toString()
        initToolbar()

        // setup view pager
        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        viewPagerAdapter.username = login?.login
        viewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewPager)

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

        // set user helper
        githubHelper = GithubHelper.getInstance(applicationContext)
        githubHelper.open()

        // set favorite user
        var statusFavorite = false
        setStatusFavorite(statusFavorite)
        btn_favorite.setOnClickListener{
            if (!statusFavorite){
                val value = contentValuesOf(
                        DatabaseContract.UserColum._ID to id,
                        DatabaseContract.UserColum.USERNAME to username,
                        DatabaseContract.UserColum.TYPE to type,
                        DatabaseContract.UserColum.AVATAR to avatar
                )
                contentResolver.insert(CONTENT_URI, value)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
//                Toast.makeText(this, R.string.success_add_favorite, Toast.LENGTH_SHORT).show()
                MotionToast.createToast(this,
                        "Success ️",
                        "Success add favorite",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.lato))
            }else{
                uriWithId = Uri.parse("$CONTENT_URI/$id")
                contentResolver.delete(uriWithId, null, null)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
//                Toast.makeText(this, R.string.success_remove_favorite, Toast.LENGTH_SHORT).show()
                MotionToast.createToast(this,
                        "Success ️",
                        "Successfully remove from favorites",
                        MotionToast.TOAST_DELETE,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.lato))
            }
        }

        // db state
        val cursor: Cursor = githubHelper.queryById(id.toString())
        if (cursor.moveToNext()){
            statusFavorite = true
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "$txt_username\'s Profile"

        btn_favorite.setOnClickListener {
            setStatusFavorite(true)
        }
    }

}