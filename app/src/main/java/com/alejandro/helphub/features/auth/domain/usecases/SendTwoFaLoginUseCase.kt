package com.alejandro.helphub.features.auth.domain.usecases

import com.alejandro.helphub.features.auth.data.TwofaRepository
import com.alejandro.helphub.features.auth.domain.UserAuthData
import javax.inject.Inject

class SendTwoFaLoginUseCase @Inject constructor(private val twofaRepository: TwofaRepository) {
    suspend operator fun invoke(userAuthData: UserAuthData): String {
        return twofaRepository.sendTwoFaLogin(userAuthData)
    }
}