package com.alejandro.helphub.features.auth.data.network

import com.alejandro.helphub.features.auth.data.network.clients.TwofaClient
import com.alejandro.helphub.features.auth.domain.SendTwofaDTO
import com.alejandro.helphub.features.auth.domain.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TwofaService @Inject constructor(private val twofaClient: TwofaClient) {

    suspend fun sendTwoFaRegister(userDTO: UserDTO): String {
        return withContext(Dispatchers.IO) {
            val response = twofaClient.sendTwoFaRegister(userDTO)
            response.body()?.code ?: "No code"
        }
    }

    suspend fun sendTwoFaResetPassword(sendTwofaDTO: SendTwofaDTO): String {
        return withContext(Dispatchers.IO) {
            val response = twofaClient.sendTwoFaResetPassword(sendTwofaDTO)
            response.body()?.code ?: "No code"
        }
    }

    suspend fun sendTwofaLogin(sendTwofaDTO: SendTwofaDTO): String {
        return withContext(Dispatchers.IO) {
            val response = twofaClient.sendTwoFaLogin(sendTwofaDTO)
            response.body()?.code ?: "No code"
        }
    }
}