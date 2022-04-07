package com.paylivre.sdk.gateway.android

class CheckDataToAutoStartTransactionTest {
//
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only pix not empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "1", "", "",
//            "person_user_gateway@test.com", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only pix empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "1", "", "", "",
//            "60712326006", "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only pix not empty email e empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "1", "", "", "person_user_gateway@test.com",
//            "", "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only pix empty email e empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "1", "", "", "",
//            "", "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only billet not empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "2", "", "",
//            "person_user_gateway@test.com", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only billet empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "2", "", "",
//            "", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only billet not empty email e empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "2", "", "",
//            "person_user_gateway@test.com", "",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only billet empty email e empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "2", "", "", "",
//            "", "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only wiretransfer not empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "4", "", "",
//            "person_user_gateway@test.com", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(true, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only wiretransfer empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "4", "", "",
//            "", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only wiretransfer not empty email e empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "4", "", "",
//            "person_user_gateway@test.com", "",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction only wiretransfer empty email e empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "4", "", "", "", "",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction many types(3) not empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "3", "", "",
//            "person_user_gateway@test.com", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction many types(10) not empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "10", "", "",
//            "person_user_gateway@test.com", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction many types(15) not empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "15", "", "",
//            "person_user_gateway@test.com", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
//
//    @Test
//    fun `test checkDataToAutoStartTransaction many types(7) not empty email e not empty document`() {
//        val mockData = DataGenerateSignature(
//            "", "", "", "",
//            "", "", Operation.DEPOSIT.code.toString(),
//            "", "7", "", "",
//            "person_user_gateway@test.com", "60712326006",
//            "", "", "", ""
//        )
//        Assert.assertEquals(false, checkDataToAutoStartTransaction(mockData))
//    }
}