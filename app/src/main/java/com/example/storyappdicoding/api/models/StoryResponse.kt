package com.example.storyappdicoding.api.models


import com.google.gson.annotations.SerializedName


data class StoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("listStory")
    val listStory: List<Story>?= null,

    @field:SerializedName("loginResult")
    val loginResult: User? = null,

    @field:SerializedName("message")
    val message: String
)