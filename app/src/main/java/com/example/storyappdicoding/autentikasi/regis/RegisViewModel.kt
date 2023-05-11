package com.example.storyappdicoding.autentikasi.regis

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappdicoding.Resource
import com.example.storyappdicoding.StoryAppRepository
import com.example.storyappdicoding.api.models.RegisRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisViewModel@Inject constructor(
    private val repository: StoryAppRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(
        context: Context,
        registerRequest: RegisRequest,
        onSuccess: (Boolean) -> Unit
    ) = viewModelScope.launch {
        repository.registerUser(registerRequest).collect { response ->
            when (response) {
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Success -> {
                    _isLoading.value = false
                    Toast.makeText(
                        context,
                        response.data.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    onSuccess(true)
                }
                is Resource.Error -> {
                    _isLoading.value = false
                    Toast.makeText(context, "Error: ${response.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}