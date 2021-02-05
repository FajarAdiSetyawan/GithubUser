package com.fajaradisetyawan.cconsumerapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fajaradisetyawan.cconsumerapp.model.GithubUser
import com.fajaradisetyawan.cconsumerapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    companion object{
        private val TAG = DetailViewModel::class.java.simpleName
    }

    private val githubUser = MutableLiveData<GithubUser>()
    private val noConnection = MutableLiveData<String>()

    fun setDetail(username: String){
        ApiClient.instance.getDetail(username).enqueue(object : Callback<GithubUser> {
            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                noConnection.value = "Check Your Connection"
            }

            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                val result = response.body()
                githubUser.postValue(result)
            }
        })
    }

    fun getDetail(): LiveData<GithubUser> {
        return githubUser
    }

    fun noConnection(): LiveData<String> {
        return noConnection
    }
}