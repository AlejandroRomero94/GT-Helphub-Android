package com.alejandro.helphub.features.auth.domain.usecases

import com.alejandro.helphub.features.auth.data.AuthRepository
import com.alejandro.helphub.features.auth.data.TwofaRepository
import com.alejandro.helphub.features.auth.domain.UserData
import javax.inject.Inject

class RegisterNewUserUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(userData: UserData): String {
        return authRepository.registerNewUser(userData)
    }
}
