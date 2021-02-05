package com.fajaradisetyawan.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.githubuser.model.GithubUser
import com.fajaradisetyawan.githubuser.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {
    companion object{
        private val TAG = FollowerViewModel::class.java.simpleName
    }

    private val githubUser = MutableLiveData<ArrayList<GithubUser>>()
    private val noConnection = MutableLiveData<String>()

    fun setFollower(username: String){
        ApiClient.instance.getFollower(username).enqueue(object : Callback<ArrayList<GithubUser>>{
            override fun onFailure(call: Call<ArrayList<GithubUser>>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                noConnection.value = "Check Your Connection"
            }

            override fun onResponse(
                call: Call<ArrayList<GithubUser>>,
                response: Response<ArrayList<GithubUser>>
            ) {
                val result = response.body()
                githubUser.postValue(result)
            }
        })
    }

    fun getFollower(): LiveData<ArrayList<GithubUser>>{
        return githubUser
    }

    fun noConnection(): LiveData<String>{
        return noConnection
    }
}