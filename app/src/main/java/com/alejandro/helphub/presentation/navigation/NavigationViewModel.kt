package com.alejandro.helphub.presentation.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.helphub.data.source.remote.server.response.ApiResponse
import com.alejandro.helphub.domain.models.ProfileUIState
import com.alejandro.helphub.domain.usecase.profile.FetchProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val fetchProfileUseCase: FetchProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUIState>(ProfileUIState.Idle)
    val uiState: StateFlow<ProfileUIState> = _uiState.asStateFlow()

    private val _id=MutableStateFlow<String?>(null)
    val id:StateFlow<String?> get()= _id


    fun fetchUserProfile() {
        Log.i("NavigationViewModel", "fetchUserProfile() called")
        viewModelScope.launch {
            try {
                _uiState.value = ProfileUIState.Loading
                when (val response = fetchProfileUseCase()) {
                    is ApiResponse.Success -> {
                        response.data?.let { profileData ->
                            Log.i("ProfileResponse", "Profile found: $profileData")
                            if (profileData.statusCode == 404) {
                                _uiState.value = ProfileUIState.ProfileNotFound
                            } else {
                                _uiState.value = ProfileUIState.Success(profileData)
                                _id.value= profileData.id
                                Log.i("NavigationViewModel", "Updated userId: ${_id.value}")
                            }
                        } ?: run {
                            Log.e("ProfileResponse", "Profile response was null")
                            _uiState.value = ProfileUIState.Error(500)
                        }
                    }
                    is ApiResponse.Error -> {
                        Log.e("ProfileResponse", "Error: ${response.message}, Code: ${response.code}")
                        _uiState.value = ProfileUIState.Error(response.code)
                    }
                    is ApiResponse.Loading -> {
                        _uiState.value = ProfileUIState.Loading
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileResponse", "Exception: ${e.message}")
                _uiState.value = ProfileUIState.Error(500)
            }
        }
    }


    fun resetState() {
        _uiState.value = ProfileUIState.Idle
    }
}