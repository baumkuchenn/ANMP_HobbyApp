package com.misoramen.hobbyapp.model

import com.google.gson.annotations.SerializedName

data class User(
    var id: Int?,
    var username: String?,
    var password: String?,
    var salt: String?,
    var email: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("created_at")
    var createdAt: String?
)

data class News(
    var id: Int?,
    var judul: String?,
    var description: String?,
    @SerializedName("image_url")
    var imageUrl: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("users_id")
    var usersId: Int?,
    var author: String?,
    var genre: String?
)

data class Contents(
    var id: Int?,
    var isi: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("news_id")
    var newsId: Int?
)