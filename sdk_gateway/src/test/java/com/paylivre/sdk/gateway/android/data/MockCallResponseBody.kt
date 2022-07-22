package com.paylivre.sdk.gateway.android.data

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class EnqueueCallbackTypes {
    ON_RESPONSE,
    ON_FAILURE
}

class MockCallResponseBody(
    private val code: Int = 200,
    private val rawResponseBodySuccess: String = "",
    private val rawResponseBodyError: String = "",
    private val enqueueCallback: EnqueueCallbackTypes = EnqueueCallbackTypes.ON_RESPONSE,
    private val messageOnFailure: String = "generic error OnFailure",
    private val runErrorRuntimeGeneric: Boolean = false,
    private val messageRunErrorRuntimeGeneric: String = "ErrorRuntimeGeneric!",
) : Call<ResponseBody> {
    override fun clone(): Call<ResponseBody> {
        return this
    }

    override fun execute(): Response<ResponseBody> {
        if (runErrorRuntimeGeneric) throw RuntimeException(messageRunErrorRuntimeGeneric)
        return if (code < 200 || code >= 300) {
            val responseResponseBody = ResponseBody.create(null, rawResponseBodyError)
            Response.error(code, responseResponseBody)
        } else {
            val responseResponseBody = ResponseBody.create(null, rawResponseBodySuccess)
            Response.success(code, responseResponseBody)
        }
    }

    override fun enqueue(callback: Callback<ResponseBody>) {
        if (enqueueCallback == EnqueueCallbackTypes.ON_RESPONSE) {
            callback.onResponse(clone(), execute())
        } else {
            callback.onFailure(clone(), RuntimeException(messageOnFailure))
        }

    }

    override fun isExecuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun request(): Request {
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }

}