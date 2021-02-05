package com.fajaradisetyawan.cconsumerapp.view

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.cconsumerapp.R
import com.fajaradisetyawan.cconsumerapp.adapter.GithubUserAdapter
import com.fajaradisetyawan.cconsumerapp.db.DatabaseContract.UserColum.Companion.CONTENT_URI
import com.fajaradisetyawan.cconsumerapp.helper.MappingHelper
import com.fajaradisetyawan.cconsumerapp.model.GithubUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val githubUser = ArrayList<GithubUser>()
    private val githubUserAdapter = GithubUserAdapter(githubUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting toolbar
        setSupportActionBar(toolbar)

        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // setup recycle view
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)

        githubUserAdapter.notifyDataSetChanged()
        rv_favorite.adapter = githubUserAdapter

        githubUserAdapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(githubUser: GithubUser) {
                val detail = Intent(this@MainActivity, DetailActivity::class.java)
                detail.putExtra(DetailActivity.EXTRA_DETAIL, githubUser)
                startActivity(detail)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val observer = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, observer)

        loadUserAsync()
    }

    private fun loadUserAsync(){
        GlobalScope.launch ( Dispatchers.Main ){
            val deferredUser = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            if (users.size > 0){
                empty_favorite.visibility = View.INVISIBLE
                githubUserAdapter.setUser(users)
            }else{
                rv_favorite.visibility = View.INVISIBLE
                empty_favorite.visibility = View.VISIBLE
            }
        }
    }
}