package com.fajaradisetyawan.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser (
    var id: Int = 0,
    var login: String? = null,
    var name: String? = null,
    var type: String? = null,
    var avatar_url: String? = null,
    var location: String? = null,
    var blog: String? = null,
    var public_repos: Int? = 0,
    var following: Int? = 0,
    var followers: Int? = 0

): Parcelable