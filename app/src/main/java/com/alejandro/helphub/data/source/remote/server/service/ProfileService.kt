package com.alejandro.helphub.data.source.remote.server.service


import android.util.Log
import com.alejandro.helphub.data.source.remote.dto.profile.CreateProfileDTO
import com.alejandro.helphub.data.source.remote.server.ProfileClient
import com.alejandro.helphub.data.source.remote.server.response.ApiResponse
import com.alejandro.helphub.data.source.remote.server.response.ProfileImageResponse
import com.alejandro.helphub.data.source.remote.server.response.ProfileResponse
import com.alejandro.helphub.data.source.remote.server.response.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ProfileService @Inject constructor(private val profileClient: ProfileClient) {

    suspend fun getProfileImageByUserId(id: String):Response<ResponseBody>{
        return profileClient.getProfileImageByUserId(id)
    }
    suspend fun getProfileByUserId(id: String):Response<ProfileResponse>{
        return profileClient.getProfileByUserId(id)
    }

    suspend fun updateProfileImage(id:String,idUserPart: MultipartBody.Part, imageProfilePart: MultipartBody.Part ):ProfileImageResponse{
        val response= profileClient.updateProfileImageByUserId(id,idUserPart,imageProfilePart)
        return if (response.isSuccessful) {
            response.body() ?: ProfileImageResponse(message = "Unknown error", idImage = "",statusCode="")
        } else {
            ProfileImageResponse(message = "Error: ${response.message()}", idImage = "",statusCode="")
        }
    }

suspend fun updateProfile(id:String, createProfileDTO: CreateProfileDTO):Response<ProfileResponse>{
    return profileClient.updateProfile(id,createProfileDTO)
}

    suspend fun getProfileImageByImageId(id: String):Response<ResponseBody>{
        return profileClient.getProfileImageByImageId(id)
    }
    suspend fun uploadProfileImage(idUserPart: MultipartBody.Part, imageProfilePart: MultipartBody.Part): ProfileImageResponse {
        val response = profileClient.uploadProfileImage(idUserPart, imageProfilePart)
        return if (response.isSuccessful) {
            response.body() ?: ProfileImageResponse(message = "Unknown error", idImage = "",statusCode="")
        } else {
            ProfileImageResponse(message = "Error: ${response.message()}", idImage = "",statusCode="")
        }
    }

    suspend fun getProfileById(id: String):Response<ProfileResponse>{
        return profileClient.getProfileById(id)
    }

    suspend fun createProfile(createProfileDTO: CreateProfileDTO): String {
        return withContext(Dispatchers.IO) {
            val response = profileClient.createProfile(createProfileDTO)
            response.body()?.code ?: ""
        }
    }
    suspend fun fetchProfile(): ApiResponse<ProfileResponse> {
        return try {
            val response = profileClient.fetchProfile()
            when {
                response.isSuccessful -> {
                    val profile = response.body()
                    if (profile==null) {
                        Log.i("FetchProfileService", "(null response)")
                        ApiResponse.Success(
                            ProfileResponse(
                                message = "Profile not found",
                                statusCode = 406,
                                id="",
                                userId = UserId(id = ""),
                                email= "",
                                nameUser = "",
                                surnameUser = "",
                                description = "",
                                interestedSkills = emptyList(),
                                location = "",
                                profilePicture = null,
                                preferredTimeRange = "",
                                selectedDays = emptyList()
                            )
                        )
                    } else {
                        Log.i("FetchProfileService", "${response.code()}+${response.message()}") //200+OK
                        ApiResponse.Success(profile)
                    }
                }
                else -> {
                    Log.e("FetchProfileService", "Error response: ${response.code()}+${response.message()}")
                    ApiResponse.Error(
                        code = response.code(),
                        message = response.message()
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("FetchProfileService", "Exception occurred: ${e.message}")
            ApiResponse.Error(
                code = 500,
                message = e.message ?: "Unknown error occurred"
            )
        }
    }
}