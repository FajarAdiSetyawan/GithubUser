package com.fajaradisetyawan.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.githubuser.model.GithubResponse
import com.fajaradisetyawan.githubuser.model.GithubUser
import com.fajaradisetyawan.githubuser.network.ApiClient
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class GithubSearchViewModel : ViewModel() {
    companion object{
        private val TAG = GithubSearchViewModel::class.java.simpleName
    }

    private val githubUserList = MutableLiveData<ArrayList<GithubUser>>()
    private val noConnection = MutableLiveData<String>()

    fun setUser(username: String){
        ApiClient.instance.getUser(username).enqueue(object : Callback<GithubResponse>{
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                noConnection.value = "Check Your Connection"
            }

            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                val items = response.body()?.items
                Log.d(TAG, items.toString())
                githubUserList.postValue(items)
            }
        })
    }

    fun getUser(): LiveData<ArrayList<GithubUser>>{
        return githubUserList
    }

    fun noConnection(): LiveData<String>{
        return noConnection
    }
}