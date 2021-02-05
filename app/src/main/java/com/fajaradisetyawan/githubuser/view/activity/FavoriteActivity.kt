package com.fajaradisetyawan.githubuser.view.activity

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.githubuser.R
import com.fajaradisetyawan.githubuser.adapter.GithubUserAdapter
import com.fajaradisetyawan.githubuser.db.DatabaseContract.UserColum.Companion.CONTENT_URI
import com.fajaradisetyawan.githubuser.helper.MappingHelper
import com.fajaradisetyawan.githubuser.model.GithubUser
import kotlinx.android.synthetic.main.acivity_main.toolbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private val githubUser = ArrayList<GithubUser>()
    private val githubUserAdapter = GithubUserAdapter(githubUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        //setting toolbar
        setSupportActionBar(toolbar)

        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // setup recycle view
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)

        githubUserAdapter.notifyDataSetChanged()
        rv_favorite.adapter = githubUserAdapter


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        //get data
        loadUserAsync()

        githubUserAdapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback{
            override fun onItemClicked(githubUser: GithubUser) {
                val detail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                detail.putExtra(DetailActivity.EXTRA_DETAIL, githubUser)
                startActivity(detail)
            }
        })
    }

    private fun loadUserAsync(){
        GlobalScope.launch(Dispatchers.Main){
            val deferredUser = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            if (users.size > 0){
                empty_favorite.visibility = View.INVISIBLE
                githubUserAdapter.setGithubUser(users)
            }else{
                rv_favorite.visibility = View.INVISIBLE
                empty_favorite.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}