package com.example.storyappdicoding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappdicoding.Resource
import com.example.storyappdicoding.StoryAppRepository
import com.example.storyappdicoding.api.models.StoryResponse
import com.example.storyappdicoding.api.models.User
import com.example.storyappdicoding.api.models.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository : StoryAppRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun tambahStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null,
        onSuccess: (StoryResponse) -> Unit
    ) = viewModelScope.launch {
        repository.inputStory(token, file, description, lat, lon).collect { response ->
            when (response) {
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Success -> {
                    _isLoading.value = false
                    if (!response.data.error) {
                        onSuccess(response.data)
                    }
                }
                is Resource.Error -> {
                    _isLoading.value = false
                }
            }
        }
    }

    fun getUser(
        user: (User) -> Unit
    ) = viewModelScope.launch {
        userPreference.getUser().collect {
            user(it)
        }
    }
}

