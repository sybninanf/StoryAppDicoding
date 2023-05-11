package com.example.storyappdicoding.api.models

import com.google.gson.annotations.SerializedName

data class RegisRequest(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)
