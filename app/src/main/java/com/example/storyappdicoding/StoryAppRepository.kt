package com.example.storyappdicoding

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyappdicoding.Constant.TAG
import com.example.storyappdicoding.Resource
import com.example.storyappdicoding.api.ApiService
import com.example.storyappdicoding.api.db.StoryDb
import com.example.storyappdicoding.api.models.LoginRequest
import com.example.storyappdicoding.api.models.RegisRequest
import com.example.storyappdicoding.api.models.Story
import com.example.storyappdicoding.api.models.StoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryAppRepository  @Inject constructor(
    private val apiService: ApiService,
    private val db: StoryDb
) {
    fun registerUser(registerRequest: RegisRequest) = flow<Resource<StoryResponse>> {
        emit(Resource.loading())
        val response = apiService.registerUser(registerRequest)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.error(it.message))
        }
    }.catch {
        Log.d(TAG, "registerUser: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)


    fun loginUser(loginRequest: LoginRequest) = flow<Resource<StoryResponse>> {
        emit(Resource.loading())
        val response = apiService.loginUser(loginRequest)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.Error(it.message))
        }
    }.catch {
        Log.d(TAG, "loginUser: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)


    @ExperimentalPagingApi
    fun getAllStories(
        token: String
    ): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(apiService, db, token),
            pagingSourceFactory = {
                db.storyDao().getAllStory()
            }
        ).flow
    }

    fun inputStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = flow<Resource<StoryResponse>> {
        emit(Resource.loading())
        val response = apiService.inputStory(token, file, description, lat, lon)
        response.let {
            if (!it.error) emit(Resource.success(it))
            else emit(Resource.error(it.message))
        }
    }.catch {
        Log.d(TAG, "getAllStories: ${it.message}")
        emit(Resource.error(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}
