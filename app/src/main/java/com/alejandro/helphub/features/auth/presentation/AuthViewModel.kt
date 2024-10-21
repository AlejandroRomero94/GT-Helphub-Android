package com.alejandro.helphub.features.auth.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alejandro.helphub.R
import com.alejandro.helphub.features.auth.domain.CountriesModel
import com.alejandro.helphub.features.auth.domain.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    //<!--------------------Credentials Screen ---------------->
    private val _isCheckBoxChecked = MutableStateFlow(false)
    val isCheckBoxChecked: StateFlow<Boolean> = _isCheckBoxChecked.asStateFlow()

    private val _isExpanded = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean> = _isExpanded.asStateFlow()

    private val _isSwitchChecked = MutableStateFlow(false)
    val isSwitchChecked: StateFlow<Boolean> = _isSwitchChecked.asStateFlow()

    private val _isSignUpButtonEnabled=MutableStateFlow(false)
    val isSignUpButtonEnabled:StateFlow<Boolean> = _isSignUpButtonEnabled.asStateFlow()

    fun toggleExpanded() {
        _isExpanded.value = !(_isExpanded.value ?: false)
    }

    fun onSwitchCheckedChanged(isChecked: Boolean) {
        _isSwitchChecked.value = isChecked
    }

    fun updateSignUpButtonState() {
        _isSignUpButtonEnabled.value = userData.value.name.isNotBlank() &&
                userData.value.surname1.isNotBlank() &&
                userData.value.surname2.isNotBlank() &&
                userData.value.phoneNumber.isNotBlank() &&
                userData.value.email.isNotBlank() &&
                userData.value.password.isNotBlank() &&
                _isCheckBoxChecked.value == true
    }

    fun updatePhoneNumber(phoneNumber:String){
        Log.d("AuthViewModel", "Updating phoneNumber to: $phoneNumber")
        _userData.update{it.copy(phoneNumber = phoneNumber)}
        updateSignUpButtonState()
    }

    fun updateSelectedCountry(country: CountriesModel) {
        _userData.update{it.copy(countryCode=country.code)}
    }

    fun updateUserEmail(email:String){
        Log.d("AuthViewModel", "Updating email to: $email")
        val updateUserData= userData.value.copy(email = email)
        _userData.value=updateUserData
        updateSignUpButtonState()
    }

    fun updateUserPassword(password:String){
        Log.d("AuthViewModel", "Updating password to: $password")
        val updateUserData= userData.value.copy(password = password)
        _userData.value=updateUserData
        updateSignUpButtonState()
    }

    fun updateUserName(name: String){
        Log.d("AuthViewModel", "Updating name to: $name")
        val updateUserData= userData.value.copy(name = name)
        _userData.value=updateUserData
        updateSignUpButtonState()
    }

    fun updateUserSurname2(surname2: String){
        Log.d("AuthViewModel", "Updating surname2 to: $surname2")
        val updateUserData= userData.value.copy(surname2 = surname2)
        _userData.value=updateUserData
        updateSignUpButtonState()
    }

    fun updateUserSurname1(surname1: String){
        Log.d("AuthViewModel", "Updating surname1 to: $surname1")
        val updateUserData= userData.value.copy(surname1 = surname1)
        _userData.value=updateUserData
        updateSignUpButtonState()
    }

    fun onCheckBoxCheckedChanged(isChecked: Boolean) {
        _isCheckBoxChecked.value = isChecked
        updateSignUpButtonState()
    }

    //<!--------------------SignUpStep1 Screen ---------------->

    fun updateUserDescription(userDescription: String) {
        Log.d("AuthViewModel", "Updating postTitle to: $userDescription")
        val updateUserData= userData.value.copy(userDescription = userDescription)
        _userData.value=updateUserData
        navigateToStep2()
    }

    fun updatePostalCode(postalCode: String) {
        Log.d("AuthViewModel", "Updating postalCode to: $postalCode")
        val updateUserData=userData.value.copy(postalCode = postalCode )
        _userData.value= updateUserData
        navigateToStep2()
    }

    private val _isNavigationToStep2Enabled=MutableStateFlow(false)
    val isNavigationToStep2Enabled:StateFlow<Boolean> = _isNavigationToStep2Enabled.asStateFlow()

    fun navigateToStep2(){
        _isNavigationToStep2Enabled.value= userData.value.userDescription.isNotBlank()&&
                userData.value.postalCode.isNotBlank()
    }

    //<!--------------------SignUpStep2 Screen ---------------->

    private val _isNavigationToStep3Enabled=MutableStateFlow(false)
    val isNavigationToStep3Enabled:StateFlow<Boolean> = _isNavigationToStep3Enabled.asStateFlow()

    fun updateUserPhotoUri(photoUri: Uri){
        val updateUserData=userData.value.copy(userPhotoUri=photoUri)
        _userData.value=updateUserData
        navigateToStep3()
    }

    fun navigateToStep3(){
        _isNavigationToStep3Enabled.value= userData.value.userPhotoUri!=null
    }

    //<!--------------------SignUpStep3 Screen ---------------->

    private val _isNavigationToStep4PostEnabled=MutableStateFlow(false)
    val isNavigationToStep4PostEnabled:StateFlow<Boolean> = _isNavigationToStep4PostEnabled.asStateFlow()

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()

    private val _selectedDays = MutableStateFlow<List<String>>(emptyList())
    val selectedDays: StateFlow<List<String>> = _selectedDays

    val daysOfWeek = listOf(
        R.string.monday,
        R.string.tuesday,
        R.string.wednesday,
        R.string.thursday,
        R.string.friday,
        R.string.saturday,
        R.string.sunday
    )
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
        _userData.value = _userData.value.copy(selectedDays = days)
        navigateToStep4Post()
    }

    fun updateAvailability(availability: String) {
        Log.d("AuthViewModel", "Updating selectedLevel to: $availability")
        val updateUserData=_userData.value.copy(availability = availability )
        _userData.value=updateUserData
        navigateToStep4Post()
    }

    fun navigateToStep4Post(){
        _isNavigationToStep4PostEnabled.value= userData.value.selectedDays.isNotEmpty()&&
                userData.value.availability!=null
    }

    //<!--------------------SignUpStep4Post Screen ---------------->

    private val _isNavigationToStep4SkillEnabled=MutableStateFlow(false)
    val isNavigationToStep4SkillEnabled:StateFlow<Boolean> = _isNavigationToStep4SkillEnabled.asStateFlow()

    fun updatePostTitle(postTitle: String) {
        Log.d("AuthViewModel", "Updating postTitle to: $postTitle")
        val updateUserData= userData.value.copy(postTitle = postTitle)
        _userData.value=updateUserData
        navigateToStep4Skill()
    }

    fun updateSelectedLevel(level: String) {
        Log.d("AuthViewModel", "Updating selectedLevel to: $level")
        val updateUserData=_userData.value.copy(selectedLevel = level )
        _userData.value=updateUserData
        navigateToStep4Skill()
    }

    fun updateLearningMode(mode:String){
        Log.d("AuthViewModel", "Updating learning mode to: $mode")
        val updateUserData= userData.value.copy(mode = mode)
        _userData.value=updateUserData
        navigateToStep4Skill()
    }

    fun navigateToStep4Skill(){
        _isNavigationToStep4SkillEnabled.value= userData.value.postTitle.isNotBlank()&&
                userData.value.selectedLevel!=null&&
                userData.value.mode!=null
    }

    //<!--------------------SignUpStep4Skill Screen ---------------->
    private val _isNavigationToStep5Enabled=MutableStateFlow(false)
    val isNavigationToStep5Enabled:StateFlow<Boolean> = _isNavigationToStep5Enabled.asStateFlow()

    val categories = listOf(
        R.string.languages,
        R.string.fitness,
        R.string.design,
        R.string.private_lessons,
        R.string.animals,
        R.string.diy,
        R.string.consultancy,
        R.string.it,
        R.string.self_care,
        R.string.help
    )

    fun updateSkillDescription(skillDescription:String){
        Log.d("AuthViewModel", "Updating skillDescription to: $skillDescription")
        val updateUserData= userData.value.copy(skillDescription = skillDescription)
        _userData.value=updateUserData
        navigateToStep5()
    }

    fun updateSelectedCategory(category: String) {
        Log.d("AuthViewModel", "Updating selectedCategory to: $category")
        val updateUserData=_userData.value.copy(selectedCategory = category )
        _userData.value= updateUserData
        navigateToStep5()
    }

    fun navigateToStep5(){
        _isNavigationToStep5Enabled.value= userData.value.skillDescription.isNotBlank()&&
                userData.value.selectedCategory!=null}

    //<!--------------------SignUpStep5 Screen ---------------->
    val popularCategories=listOf(
        R.string.languages,
        R.string.fitness,
        R.string.design,
        R.string.private_lessons,
        R.string.animals,
        R.string.diy,
        R.string.consultancy,
        R.string.it,
        R.string.self_care,
    )

    fun updateCategoriesOfInterest(category: String,isSelected:Boolean){
        val currentCategories = _userData.value.categoriesOfInterest.toMutableList()
        if (isSelected) {
            currentCategories.add(category)
        } else {
            currentCategories.remove(category)
        }
        _userData.value = _userData.value.copy(categoriesOfInterest = currentCategories)
    }

    fun updateSelectedCategoriesOfInterest(category: String){
        val currentCategories=userData.value.categoriesOfInterest.toMutableList()?:mutableListOf()
        currentCategories.add(category)
        _userData.value=_userData.value.copy(categoriesOfInterest = currentCategories)
    }
    fun removeCategoryOfInterest(category: String) {
        val currentCategories = _userData.value.categoriesOfInterest?.toMutableList() ?: mutableListOf()
        currentCategories.remove(category)
        _userData.value = _userData.value.copy(categoriesOfInterest = currentCategories)
    }

    //Otros

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun updateUserPhotoBitmap(bitmap: Bitmap) {
        _userData.value = _userData.value.copy(photoBitmap = bitmap)
    }

   fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnable.value = enableLogin(email, password)
    }

    private fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6
}