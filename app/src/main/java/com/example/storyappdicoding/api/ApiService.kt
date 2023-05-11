package com.example.storyappdicoding.api

import com.example.storyappdicoding.api.models.LoginRequest
import com.example.storyappdicoding.api.models.RegisRequest
import com.example.storyappdicoding.api.models.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun registerUser(
        @Body registerRequest: RegisRequest
    ): StoryResponse

    @POST("login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): StoryResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") header: String,
        @Query("page") page : Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ) : StoryResponse

    @Multipart
    @POST("stories")
    suspend fun inputStory(
        @Header("Authorization") header: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ) : StoryResponse
}