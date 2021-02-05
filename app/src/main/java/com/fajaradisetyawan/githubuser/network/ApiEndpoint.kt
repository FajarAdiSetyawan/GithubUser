package com.fajaradisetyawan.githubuser.network

import com.fajaradisetyawan.githubuser.BuildConfig
import com.fajaradisetyawan.githubuser.model.GithubResponse
import com.fajaradisetyawan.githubuser.model.GithubUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
interface ApiEndpoint {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getUser(@Query("q") username: String): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getDetail(@Path("username") username: String): Call<GithubUser>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<GithubUser>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getFollower(@Path("username") username: String): Call<ArrayList<GithubUser>>
}