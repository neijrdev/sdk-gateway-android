package com.paylivre.sdk.gateway.android.data

import com.paylivre.sdk.gateway.android.data.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

class ApiServiceMock(
    private val mockCallResponseBody : MockCallResponseBody = MockCallResponseBody(),
    private val runErrorRuntimeGeneric: Boolean = false,
    private val messageRunErrorRuntimeGeneric: String = "ErrorRuntimeGeneric!",
) : ApiService {

    override fun getPixApprovalTime( ): Call<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return mockCallResponseBody
    }

    override fun getServicesStatus(): Call<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return mockCallResponseBody
    }

    override fun checkStatusDeposit(id: Int): Call<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return mockCallResponseBody
    }

    override fun checkStatusTransaction(id: Int): Call<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return mockCallResponseBody
    }

    override fun transferProof(
        order_id: Int,
        token: RequestBody,
        proof: MultipartBody.Part,
    ): Call<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return mockCallResponseBody
    }

    override fun newTransactionGateway(requestBody: RequestBody): Call<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return mockCallResponseBody
    }

    override fun checkStatusOrder(order_id: Int, token: String): Call<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return mockCallResponseBody
    }
}