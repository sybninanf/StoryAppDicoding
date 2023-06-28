package com.example.storyappdicoding.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappdicoding.Resource
import com.example.storyappdicoding.StoryAppRepository
import com.example.storyappdicoding.api.models.Story
import com.example.storyappdicoding.api.models.User
import com.example.storyappdicoding.api.models.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: StoryAppRepository,
    private val userPref: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getStoryWithLocation(
        token: String,
        location : Int = 1,
        onSuccess: (List<Story>) -> Unit
    ) = viewModelScope.launch {
        repository.getStoryWithLocation(token, location).collect{ response ->
            when(response){
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Success -> {
                    _isLoading.value = false
                    if(!response.data.error){
                        onSuccess(response.data.listStory as List<Story>)
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
        userPref.getUser().collect{
            user(it)
        }
    }
}