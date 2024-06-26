package com.misoramen.hobbyapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface HobbyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registerUser(vararg user:User)

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUsername(username: String): User //Tinggal method register di viewmodel

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun loginUser(username: String, password: String): User

    @Query("SELECT * FROM user WHERE id = :idUser")
    fun getCertainUser(idUser: String): User

    @Query("UPDATE user SET first_name= :firstName, last_name= :lastName, password= :password WHERE id= :idUser")
    fun update(firstName: String, lastName: String, password: String, idUser: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(genres: List<Genre?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: List<News?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContents(contents: List<Contents?>?)

    @Query("SELECT n.*, u.first_name as author, g.nama as genre FROM news n INNER JOIN user u on n.user_id = u.id INNER JOIN genre g on n.genre_id = g.id")
    fun selectAllNews(): List<NewsWithAuthor>

    @Query("SELECT n.*, u.first_name as author, g.nama as genre FROM news n INNER JOIN user u on n.user_id = u.id INNER JOIN genre g on n.genre_id = g.id WHERE n.id = :idNews")
    fun selectCertainNews(idNews: Int): List<NewsWithAuthor>

    @Query("SELECT * FROM Contents WHERE news_id = :idNews LIMIT :index, 1")
    fun selectNewsContent(idNews:Int, index:Int): List<Contents>

    @Query("SELECT COUNT(*) FROM Contents WHERE news_id = :idNews")
    fun selectPageContent(idNews: Int): String

    @Delete
    fun deleteNews(news:News)
}
