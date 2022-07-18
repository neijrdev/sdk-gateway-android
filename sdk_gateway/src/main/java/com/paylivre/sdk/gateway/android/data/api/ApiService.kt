package com.paylivre.sdk.gateway.android.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST

import retrofit2.http.Multipart

interface ApiService {
    @GET("/api/v2/gateway/averagePixApprovalTime")
    fun getPixApprovalTime(): Call<ResponseBody>

    @GET("/api/v2/gateway/deposit-status")
    fun getServicesStatus(): Call<ResponseBody>

    @GET("/api/v2/transaction/deposit/status/{id}")
    fun checkStatusDeposit(
        @Path("id")
        id: Int
    ): Call<ResponseBody>

    @GET("/api/v2/transaction/status/{id}")
    fun checkStatusTransaction(
        @Path("id")
        id: Int
    ): Call<ResponseBody>

    @Multipart
    @POST("/api/v2/gateway/{order_id}/transfer-proof")
    fun transferProof(
        @Path("order_id")
        order_id: Int,
        @Part("token")
        token: RequestBody,
        @Part proof: MultipartBody.Part,
    ): Call<ResponseBody>

    @POST("/api/v2/gateway")
    fun newTransactionGateway(
        @Body
        requestBody: RequestBody
    ): Call<ResponseBody>

    @GET("/api/v2/gateway/status/{order_id}/{token}")
    fun checkStatusOrder(
        @Path("order_id")
        order_id: Int,
        @Path("token")
        token: String,
    ): Call<ResponseBody>

//    @POST("/mocks/neijrdev/gateway/30370850/api/v2/gateway")
//    fun newTransactionGateway(
//        @Body
//        requestBody: RequestBody
//    ): Call<ResponseBody>
//
//    @GET("/mocks/neijrdev/gateway/30370850/api/v2/gateway/status/{order_id}/{token}")
//    fun checkStatusOrder(
//        @Path("order_id")
//        order_id: Int,
//        @Path("token")
//        token: String,
//    ): Call<ResponseBody>
}
