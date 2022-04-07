package com.paylivre.sdk.gateway.android

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

data class MockOrderDataRequest(
    val name: String?,
    val document: String?,
    val password: String?
)

fun removeElementFromJsonObject(dataRequest: MockOrderDataRequest) : String {
    val gson = Gson()
    val dataRequestWithoutPass = gson.toJsonTree(dataRequest).asJsonObject
    dataRequestWithoutPass.remove("password")
    return dataRequestWithoutPass.toString()
}

class GsonRemoveFieldTest {

    @Test
    fun removeElementFromJsonObjectTest(){
        Assert.assertEquals("", removeElementFromJsonObject(
            MockOrderDataRequest("João", "12345678912", "123456")
        ))
    }

}