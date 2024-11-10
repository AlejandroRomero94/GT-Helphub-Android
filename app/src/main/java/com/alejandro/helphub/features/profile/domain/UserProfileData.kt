package com.alejandro.helphub.features.profile.domain

import android.graphics.Bitmap
import android.net.Uri

data class UserProfileData (
    val postalCode:String="",
    val userDescription:String="",
    val userPhotoUri: Uri? =null,
    val photoBitmap: Bitmap? = null,
    val availability:String?=null,
    val selectedDays:List<String> = emptyList(),
    val categoriesOfInterest:List<String> =emptyList()
)