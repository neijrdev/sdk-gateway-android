package com.paylivre.sdk.gateway.android.services.argon2i

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import com.lambdapioneer.argon2kt.Argon2Version
import com.paylivre.sdk.gateway.android.DefaultDispatcherProvider
import com.paylivre.sdk.gateway.android.DispatcherProvider
import kotlinx.coroutines.*
//
//suspend fun generateArgon2iHash(stringPass: String, salt: String? = null) : String {
//    val saltInString = if(salt.isNullOrEmpty()) "5iX9MxZ6c399Xfrv" else salt
//    val iterationsInt = 2
//    val memory = 16
//    val parallelism = 1
//    val hashLengthBytes = 16
//
//    return withContext(Dispatchers.Default){
//        val result = Argon2Kt().hash(
//            mode = Argon2Mode.ARGON2_I,
//            password = stringPass.toByteArray(),
//            salt = saltInString.toByteArray(),
//            tCostInIterations = iterationsInt,
//            mCostInKibibyte = memory,
//            version = Argon2Version.V13,
//            parallelism = parallelism,
//            hashLengthInBytes = hashLengthBytes
//        )
//        result.encodedOutputAsString()
//    }
//}



class Argon2iHash(private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()) {

    suspend fun generateArgon2iHash(stringPass: String, salt: String? = null) : String {
        try {
            val saltInString = if(salt.isNullOrEmpty()) "5iX9MxZ6c399Xfrv" else salt
            val iterationsInt = 2
            val memory = 16
            val parallelism = 1
            val hashLengthBytes = 16

            return withContext(dispatchers.default()){
                val result = Argon2Kt().hash(
                    mode = Argon2Mode.ARGON2_I,
                    password = stringPass.toByteArray(),
                    salt = saltInString.toByteArray(),
                    tCostInIterations = iterationsInt,
                    mCostInKibibyte = memory,
                    version = Argon2Version.V13,
                    parallelism = parallelism,
                    hashLengthInBytes = hashLengthBytes
                )
                result.encodedOutputAsString()
            }
        } catch (e:Exception){
            println(e)
            return ""
        }

    }
}
