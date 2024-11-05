package com.alejandro.helphub.features.profile.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandro.helphub.features.profile.data.network.response.SearchResponse
import com.alejandro.helphub.features.profile.domain.UserProfileData
import com.alejandro.helphub.features.profile.domain.usecases.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): ViewModel(){
    private val _userProfileData= MutableStateFlow(UserProfileData())
    val userProfileData: StateFlow<UserProfileData> = _userProfileData.asStateFlow()

    private val _userInfo=MutableStateFlow<Result<SearchResponse>?>(null)
    val userInfo:StateFlow<Result<SearchResponse>?> get()= _userInfo

    //<!--------------------Profile Setup Step 1 ---------------->

    fun updateUserDescription(userDescription: String) {
        Log.d("AuthViewModel", "Updating postTitle to: $userDescription")
        val updateUserData =
            userProfileData.value.copy(userDescription = userDescription)
        _userProfileData.value = updateUserData
        navigateToStep2()
    }

    fun updatePostalCode(postalCode: String) {
        Log.d("AuthViewModel", "Updating postalCode to: $postalCode")
        val updateUserData = userProfileData.value.copy(postalCode = postalCode)
        _userProfileData.value = updateUserData
        navigateToStep2()
    }

    private val _isNavigationToStep2Enabled = MutableStateFlow(false)
    val isNavigationToStep2Enabled: StateFlow<Boolean> =
        _isNavigationToStep2Enabled.asStateFlow()

    fun navigateToStep2() {
        _isNavigationToStep2Enabled.value =
            userProfileData.value.userDescription.isNotBlank() &&
                    userProfileData.value.postalCode.isNotBlank()
    }

//<!--------------------Profile Setup Step 2 ---------------->

    private val _isNavigationToStep3Enabled = MutableStateFlow(false)
    val isNavigationToStep3Enabled: StateFlow<Boolean> =
        _isNavigationToStep3Enabled.asStateFlow()

    fun updateUserPhotoUri(photoUri: Uri) {
        val updateUserData = userProfileData.value.copy(userPhotoUri = photoUri)
        _userProfileData.value = updateUserData
        navigateToStep3()
    }

    fun navigateToStep3() {
        _isNavigationToStep3Enabled.value = userProfileData.value.userPhotoUri != null
    }

    //<!--------------------Profile Setup Step 3 ---------------->

    private val _isNavigationToStep4PostEnabled = MutableStateFlow(false)
    val isNavigationToStep4PostEnabled: StateFlow<Boolean> =
        _isNavigationToStep4PostEnabled.asStateFlow()

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()

    private val _selectedDays = MutableStateFlow<List<String>>(emptyList())
    val selectedDays: StateFlow<List<String>> = _selectedDays

    fun toggleDropdown() {
        _expanded.value = !_expanded.value
    }

    fun onDayChecked(day: String, isChecked: Boolean) {
        val currentDays = _selectedDays.value.toMutableList()
        if (isChecked) {
            currentDays.add(day)
        } else {
            currentDays.remove(day)
        }
        _selectedDays.value = currentDays
        updateUserDays(currentDays)
    }

    fun updateUserDays(days: List<String>) {
        _userProfileData.value = _userProfileData.value.copy(selectedDays = days)
        navigateToStep4Post()
    }

    fun updateAvailability(availability: String) {
        Log.d("AuthViewModel", "Updating selectedLevel to: $availability")
        val updateUserData = _userProfileData.value.copy(availability = availability)
        _userProfileData.value = updateUserData
        navigateToStep4Post()
    }

    fun navigateToStep4Post() {
        _isNavigationToStep4PostEnabled.value =
            userProfileData.value.selectedDays.isNotEmpty() &&
                    userProfileData.value.availability != null
    }
    //<!--------------------Profile Setup Step 4a ---------------->

    private val _isNavigationToStep4SkillEnabled = MutableStateFlow(false)
    val isNavigationToStep4SkillEnabled: StateFlow<Boolean> =
        _isNavigationToStep4SkillEnabled.asStateFlow()

    fun updatePostTitle(postTitle: String) {
        Log.d("AuthViewModel", "Updating postTitle to: $postTitle")
        val updateUserData = userProfileData.value.copy(postTitle = postTitle)
        _userProfileData.value = updateUserData
        navigateToStep4Skill()
    }

    fun updateSelectedLevel(level: String) {
        Log.d("AuthViewModel", "Updating selectedLevel to: $level")
        val updateUserData = userProfileData.value.copy(selectedLevel = level)
        _userProfileData.value = updateUserData
        navigateToStep4Skill()
    }

    fun updateLearningMode(mode: String) {
        Log.d("AuthViewModel", "Updating learning mode to: $mode")
        val updateUserData = userProfileData.value.copy(mode = mode)
        _userProfileData.value = updateUserData
        navigateToStep4Skill()
    }

    fun navigateToStep4Skill() {
        _isNavigationToStep4SkillEnabled.value =
            userProfileData.value.postTitle.isNotBlank() &&
                    userProfileData.value.selectedLevel != null &&
                    userProfileData.value.mode != null
    }

    //<!--------------------Profile Setup Step 4b ---------------->
    private val _isNavigationToStep5Enabled = MutableStateFlow(false)
    val isNavigationToStep5Enabled: StateFlow<Boolean> =
        _isNavigationToStep5Enabled.asStateFlow()

    private val _selectedCategories =
        MutableStateFlow<List<String>>(emptyList())
    val selectedCategories: StateFlow<List<String>> =
        _selectedCategories.asStateFlow()

    fun updateSkillDescription(skillDescription: String) {
        Log.d(
            "AuthViewModel",
            "Updating skillDescription to: $skillDescription"
        )
        val updateUserData =
            userProfileData.value.copy(skillDescription = skillDescription)
        _userProfileData.value = updateUserData
        navigateToStep5()
    }

    fun onCategoryChecked(category: String, isChecked: Boolean) {
        val currentCategories = _selectedCategories.value.toMutableList()
        when {
            isChecked && currentCategories.size < 2 -> currentCategories.add(
                category
            )

            !isChecked -> currentCategories.remove(category)
        }
        _selectedCategories.value = currentCategories
        updateSelectedCategories(currentCategories)
    }

    fun updateSelectedCategories(categories: List<String>) {
        Log.d("AuthViewModel", "Updating selectedCategories to: $categories")
        _userProfileData.value = userProfileData.value.copy(selectedCategories = categories)
        navigateToStep5()
    }

    fun fetchUserInfo(email:String){
        viewModelScope.launch{
            _userInfo.value=getUserInfoUseCase(email)
        }
    }

    fun navigateToStep5() {
        Log.d(
            "AuthViewModel",
            "Skill Description: ${userProfileData.value.skillDescription}"
        )
        Log.d(
            "AuthViewModel",
            "Selected Categories: ${userProfileData.value.selectedCategories}"
        )
        _isNavigationToStep5Enabled.value =
            userProfileData.value.skillDescription.isNotBlank() &&
                    userProfileData.value.selectedCategories.isNotEmpty()
    }


    //<!--------------------Profile Setup Step 5 ---------------->

    private val _isNavigationToHomeEnabled = MutableStateFlow(false)
    val isNavigationToHomeEnabled: StateFlow<Boolean> =
        _isNavigationToHomeEnabled.asStateFlow()

    private val _selectedCategoriesOfInterest =
        MutableStateFlow<List<String>>(emptyList())
    val selectedCategoriesOfInterest: StateFlow<List<String>> =
        _selectedCategoriesOfInterest.asStateFlow()

    fun onCategoriesOfInterestChecked(category: String, isSelected: Boolean) {
        val currentCategories =
            _selectedCategoriesOfInterest.value.toMutableList()
        when {
            isSelected && currentCategories.size < 3 -> currentCategories.add(
                category
            )

            !isSelected -> currentCategories.remove(category)
        }
        _selectedCategoriesOfInterest.value = currentCategories
        updateCategoriesOfInterest(currentCategories)
    }

    fun updateCategoriesOfInterest(categories: List<String>) {
        _userProfileData.value =
            userProfileData.value.copy(categoriesOfInterest = categories)
        navigateToHome()
    }

    fun navigateToHome() {
        Log.d(
            "AuthViewModel",
            "Categories Of Interest: ${userProfileData.value.categoriesOfInterest}"
        )
        _isNavigationToHomeEnabled.value =
            userProfileData.value.categoriesOfInterest.isNotEmpty()
    }
}