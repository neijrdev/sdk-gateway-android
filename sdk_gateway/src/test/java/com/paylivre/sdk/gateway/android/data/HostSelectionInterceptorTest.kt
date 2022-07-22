package com.paylivre.sdk.gateway.android.data

import android.os.Build
import com.paylivre.sdk.gateway.android.App
import com.paylivre.sdk.gateway.android.data.api.HostSelectionInterceptor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class HostSelectionInterceptorTest {

    @Test
    fun `CASE 01, test HostSelectionInterceptorTest given App getHostAPI()`() {
        //GIVEN
        val hostSelectionInterceptor = HostSelectionInterceptor()
        val mockChainInterceptor: okhttp3.Interceptor.Chain = mockk()
        val mockHttpUrl: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("www.google.com")
            .build();

        val mockRequest: Request = Request.Builder().url(mockHttpUrl).build()

        val mockResponse: Response = Response.Builder()
            .code(200)
            .request(mockRequest)
            .protocol(Protocol.HTTP_1_0)
            .message("message test")
            .build()

        every {
            mockChainInterceptor.request()
        } returns mockRequest

        every {
            mockChainInterceptor.proceed(any())
        } returns mockResponse


        //WHen
        hostSelectionInterceptor.intercept(mockChainInterceptor)


        //THEN
        verify {
            mockChainInterceptor.proceed(any())
        }

        verify {
            mockChainInterceptor.request()
        }
    }

    @Test
    fun `CASE 02, test HostSelectionInterceptorTest given App getHostAPI()`() {
        //GIVEN
        val hostSelectionInterceptor = HostSelectionInterceptor()
        val mockChainInterceptor: okhttp3.Interceptor.Chain = mockk()
        val mockHttpUrl: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("www.google.com")
            .build();

        val mockRequest: Request = Request.Builder().url(mockHttpUrl).build()

        val mockResponse: Response = Response.Builder()
            .code(200)
            .request(mockRequest)
            .protocol(Protocol.HTTP_1_0)
            .message("message test")
            .build()

        every {
            mockChainInterceptor.request()
        } returns mockRequest

        every {
            mockChainInterceptor.proceed(any())
        } returns mockResponse


        //WHen
        App.setHostAPI("")
        hostSelectionInterceptor.intercept(mockChainInterceptor)


        //THEN
        verify {
            mockChainInterceptor.proceed(any())
        }

        verify {
            mockChainInterceptor.request()
        }
    }

    @Test
    fun `CASE 03, test HostSelectionInterceptorTest given App getHostAPI()`() {
        //GIVEN
        val hostSelectionInterceptor = HostSelectionInterceptor()
        val mockChainInterceptor: okhttp3.Interceptor.Chain = mockk()
        val mockHttpUrl: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("www.google.com")
            .build();

        val mockRequest: Request = Request.Builder().url(mockHttpUrl).build()

        val mockResponse: Response = Response.Builder()
            .code(200)
            .request(mockRequest)
            .protocol(Protocol.HTTP_1_0)
            .message("message test")
            .build()

        every {
            mockChainInterceptor.request()
        } returns mockRequest

        every {
            mockChainInterceptor.proceed(any())
        } returns mockResponse


        //WHen
        App.setHostAPI("www.google.com.br")
        hostSelectionInterceptor.intercept(mockChainInterceptor)


        //THEN
        verify {
            mockChainInterceptor.proceed(any())
        }

        verify {
            mockChainInterceptor.request()
        }
    }


}