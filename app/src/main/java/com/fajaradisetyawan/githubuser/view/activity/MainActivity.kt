@file:Suppress("PLUGIN_WARNING")

package com.fajaradisetyawan.githubuser.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajaradisetyawan.githubuser.R
import com.fajaradisetyawan.githubuser.adapter.GithubUserAdapter
import com.fajaradisetyawan.githubuser.model.GithubUser
import com.fajaradisetyawan.githubuser.viewmodel.GithubSearchViewModel
import kotlinx.android.synthetic.main.acivity_main.*
import www.sanju.motiontoast.MotionToast

class MainActivity : AppCompatActivity() {

    private lateinit var githubSearchViewModel: GithubSearchViewModel
    private val githubUser = ArrayList<GithubUser>()
    private val githubUserAdapter = GithubUserAdapter(githubUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acivity_main)

        //setting toolbar
        setSupportActionBar(toolbar)

        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // setup recycle view
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.setHasFixedSize(true)

        githubUserAdapter.notifyDataSetChanged()
        rv_user.adapter = githubUserAdapter

        setSearch()
        getViewModel()

        githubUserAdapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(githubUser: GithubUser) {
                val detail = Intent(this@MainActivity, DetailActivity::class.java)
                detail.putExtra(DetailActivity.EXTRA_DETAIL, githubUser)
                startActivity(detail)
            }
        })
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            val favorite = Intent(this, FavoriteActivity::class.java)
            startActivity(favorite)
            true
        }
        R.id.action_setting ->{
            val setting = Intent(this, SettingActivity::class.java)
            startActivity(setting)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun stateNoResult() {
        find_user.visibility = View.INVISIBLE
        offline.visibility = View.INVISIBLE
        rv_user.visibility = View.INVISIBLE
        rotateloading.visibility = View.INVISIBLE
        not_found.visibility = View.VISIBLE
        baseLoading.visibility = View.INVISIBLE
    }

    private fun stateSuccess() {
        find_user.visibility = View.INVISIBLE
        not_found.visibility = View.INVISIBLE
        rotateloading.visibility = View.INVISIBLE
        rv_user.visibility = View.VISIBLE
        offline.visibility = View.INVISIBLE
        baseLoading.visibility = View.INVISIBLE
    }

    private fun stateQueryListener() {
        rotateloading.visibility = View.VISIBLE
        baseLoading.visibility = View.VISIBLE
        rv_user.visibility = View.INVISIBLE
        not_found.visibility = View.INVISIBLE
        find_user.visibility = View.INVISIBLE
        offline.visibility = View.INVISIBLE
    }

    private fun setSearch(){
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                stateQueryListener()
                githubSearchViewModel.setUser(query)

                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
    }

    private fun getViewModel(){
        githubSearchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(GithubSearchViewModel::class.java)

        githubSearchViewModel.getUser().observe(this, Observer { users ->
            if ((users != null) && (users.size != 0)){
                githubUserAdapter.setGithubUser(users)
                stateSuccess()
            }else{
                stateNoResult()
            }
        })

        githubSearchViewModel.noConnection().observe(this, Observer { result ->
            offline.visibility = View.VISIBLE
            baseLoading.visibility = View.INVISIBLE
            MotionToast.createToast(this,
                    "No Connection ☹️",
                    result,
                    MotionToast.TOAST_NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.lato))
        })
    }
}