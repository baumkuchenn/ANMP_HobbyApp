package com.misoramen.hobbyapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @ColumnInfo(name="username")
    var username: String,
    @ColumnInfo(name="password")
    var password: String,
    @ColumnInfo(name="email")
    var email: String,
    @ColumnInfo(name="first_name")
    var firstName: String,
    @ColumnInfo(name="last_name")
    var lastName: String,
    @ColumnInfo(name="created_at")
    var createdAt: String,
    @ColumnInfo(name="deleted_at")
    var deletedAt: String,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}

@Entity
data class News(
    @ColumnInfo(name="judul")
    var judul: String,
    @ColumnInfo(name="description")
    var description: String,
    @ColumnInfo(name="image_url")
    var imageUrl: String,
    @ColumnInfo(name="created_at")
    var createdAt: String,
    @ColumnInfo(name="deleted_at")
    var deletedAt: String,
    @ColumnInfo(name="user_id")
    var userId: Int,
    @ColumnInfo(name="genre_id")
    var genreId: Int,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}

@Entity
data class Contents(
    @ColumnInfo(name="isi")
    var isi: String,
    @ColumnInfo(name="created_at")
    var createdAt: String,
    @ColumnInfo(name="deleted_at")
    var deletedAt: String,
    @ColumnInfo(name="news_id")
    var newsId: Int
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}

@Entity
data class Genre(
    @ColumnInfo(name="nama")
    var nama: String,
    @ColumnInfo(name="deskripsi")
    var deskripsi: String,
    @ColumnInfo(name="created_at")
    var createdAt: String,
    @ColumnInfo(name="deleted_at")
    var deletedAt: String,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}

data class NewsWithAuthor(
    var id: Int,
    var judul: String,
    var description: String,
    @ColumnInfo("image_url")
    var imageUrl: String,
    @ColumnInfo("created_at")
    var createdAt: String,
    var author: String,
    var genre: String
)