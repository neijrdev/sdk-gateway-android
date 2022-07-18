package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.deposit.CheckStatusDepositResponse
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.order.StatusWithdrawOrder
import com.paylivre.sdk.gateway.android.data.model.transaction.CheckStatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transaction.DataStatusTransaction
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.services.countdowntimer.CountDownTimerService
import com.paylivre.sdk.gateway.android.services.countdowntimer.MockCountDownTimerGivenHelper
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


var checkStatusDepositResponseMock = CheckStatusDepositResponse(
    status = "",
    status_code = 0,
    data = null,
    message = ""
)

var checkStatusTransactionResponseMock = CheckStatusTransactionResponse(
    status = "success",
    status_code = 200,
    message = "OK",
    data = DataStatusTransaction(transaction_status_id = 2)
)

var statusTransactionResponseMock = StatusTransactionResponse(
    isLoading = false,
    isSuccess = false,
    data = null,
    error = null
)

var statusWithdrawOrderMock = StatusWithdrawOrder(
    withdrawal_status_id = null,
    withdrawal_type_id = null,
    merchant_approval_status_id = null,
    order_status_id = null
)

data class MainViewModelMockData(
    var mockMainViewModel: MainViewModel,
    val logEventsServiceImpl: LogEventsService,
    val mockCountDownTimerServiceImpl: CountDownTimerService,
)

fun getResponseCommonTransactionDataByFile(fileName: String): ResponseCommonTransactionData? {
    val responseExpectedString = FileTestsUtils().loadJsonAsString(fileName)

    return Gson().fromJson(
        responseExpectedString, ResponseCommonTransactionData::class.java
    )
}

fun checkTextView(textView: TextView?, expectedValue: String?) {
    Assert.assertEquals(View.VISIBLE, textView?.visibility)
    Assert.assertEquals(expectedValue, textView?.text)
}

fun getBitmap(drawable: Drawable): Bitmap? {
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun checkDrawableImageView(imageView: ImageView?, drawableExpected: Drawable): Boolean {
    return try {
        val bitmap = imageView?.let { getBitmap(it.drawable) }
        val otherBitmap = getBitmap(drawableExpected)
        bitmap!!.sameAs(otherBitmap)
    } catch (e: Exception) {
        false
    }
}

fun checkTransactionData(
    fragment: Fragment,
    transactionId: String,
    originalValue: String,
    taxValue: String,
    dueDateValue: String,
    totalValue: String,
    availableLimitValue: String,
) {
    checkTextView(fragment.view?.findViewById(R.id.textIdValue), transactionId)
    checkTextView(fragment.view?.findViewById(R.id.textOriginalAmountValue), originalValue)
    checkTextView(fragment.view?.findViewById(R.id.textTaxAmountValue), taxValue)
    checkTextView(fragment.view?.findViewById(R.id.textDueDateValue), dueDateValue)
    checkTextView(fragment.view?.findViewById(R.id.textTotalAmountValue), totalValue)
    checkTextView(fragment.view?.findViewById(R.id.textLimitAmountValue), availableLimitValue)

}

fun checkStatusDeposit(fragment: Fragment, title: String, subtitle: String, drawableId: Int) {
    val textViewTitle = fragment.view?.findViewById<TextView>(R.id.textViewTitle)
    val textViewBody = fragment.view?.findViewById<TextView>(R.id.textViewBody)
    Assert.assertEquals(title, textViewTitle?.text?.toString())
    Assert.assertEquals(subtitle, textViewBody?.text?.toString())

    //check icon status
    val imgLocaleImage = fragment.view?.findViewById<ImageView>(R.id.imageStatusIcon)
    val expectedDrawable: Drawable = fragment.resources.getDrawable(drawableId)
    Assert.assertTrue(checkDrawableImageView(imgLocaleImage, expectedDrawable))
}

fun checkButtonBackToMerchant(fragment: Fragment) {
    //Check button back to merchant
    Assert.assertEquals(
        View.VISIBLE,
        fragment?.view?.findViewById<FragmentContainerView>(R.id.fragmentBackMerchant)
            ?.visibility
    )
}

fun checkTerms(fragment: Fragment) {
    //Check terms
    Assert.assertEquals(
        View.VISIBLE,
        fragment?.view?.findViewById<FragmentContainerView>(R.id.containerAcceptTerms)
            ?.visibility
    )
}

fun checkDrawable(fragment: Fragment, imageViewId: Int, expectedDrawableId: Int) {
    val imgViewDrawable = fragment.view?.findViewById<ImageView>(imageViewId)
    val expectedDrawable: Drawable = fragment.resources.getDrawable(expectedDrawableId)
    Assert.assertTrue(checkDrawableImageView(imgViewDrawable, expectedDrawable))
}

fun checkHeaderDeposit(fragment: Fragment) {
    //check button close sdk
    val headerView = fragment?.view?.findViewById<FragmentContainerView>(R.id.header_title)
    Assert.assertEquals(View.VISIBLE, headerView?.visibility)

    val buttonCloseSDK = headerView?.findViewById<MaterialButton>(R.id.ButtonCloseSDK)
    Assert.assertEquals(View.VISIBLE, buttonCloseSDK?.visibility)
    Assert.assertEquals("Voltar", buttonCloseSDK?.text)

    checkTextView(headerView?.findViewById(R.id.textViewOperation), "Depósito")
    checkTextView(headerView?.findViewById(R.id.textVersionSDK),
        "v${BuildConfig.VERSION_NAME}")

    //check flag image current locale
    val imgLocaleImage = headerView?.findViewById<ImageView>(R.id.flagLocaleImage)
    val drawableIdExpected = R.drawable.current_lang
    val expectedDrawable: Drawable = fragment.resources.getDrawable(drawableIdExpected)
    Assert.assertTrue(checkDrawableImageView(imgLocaleImage, expectedDrawable))

    //check logo merchant default (Paylivre logo)
    val imgLogoMerchant = headerView?.findViewById<ImageView>(R.id.logoMerchant)
    val logoMerchantDrawableIdExpected = R.drawable.ic_logo_paylivre_blue
    val logoMerchantExpectedDrawable: Drawable =
        fragment.resources.getDrawable(logoMerchantDrawableIdExpected)
    Assert.assertTrue(checkDrawableImageView(imgLogoMerchant, logoMerchantExpectedDrawable))
}


fun createDepositInstances(
    statusTransactionResponse: StatusTransactionResponse? = statusTransactionResponseMock,
    checkStatusDepositResponse: CheckStatusDepositResponse? = checkStatusDepositResponseMock,
    checkStatusDepositLoading: Boolean = false,
    mockCountDownTimerServiceImpl: CountDownTimerService = MockCountDownTimerGivenHelper(),
    checkStatusTransactionResponse: CheckStatusTransactionResponse = checkStatusTransactionResponseMock,
    checkStatusTransactionLoading: Boolean? = null,
): MainViewModelMockData {
    val mockMainViewModel: MainViewModel = mockk()
    val logEventsServiceImpl = mockk<LogEventsService>(relaxed = true)

    loadKoinModules(module {
        single {
            logEventsServiceImpl
        }
        single {
            mockCountDownTimerServiceImpl
        }
        viewModel {
            mockMainViewModel
        }
    })


    coEvery {
        mockMainViewModel.language
    } returns MutableLiveData("pt_BR")

    coEvery {
        mockMainViewModel.checkStatusDepositLoading
    } returns MutableLiveData(checkStatusDepositLoading)

    coEvery {
        mockMainViewModel.statusResponseTransaction
    } returns MutableLiveData(statusTransactionResponse)

    coEvery {
        mockMainViewModel.checkStatusDepositResponse
    } returns MutableLiveData(checkStatusDepositResponse)

    coEvery {
        mockMainViewModel.logoUrl
    } returns MutableLiveData(null)

    coEvery {
        mockMainViewModel.logoResId
    } returns MutableLiveData(-1)

    coEvery {
        mockMainViewModel.operation
    } returns MutableLiveData(Operation.DEPOSIT.code)

    coEvery {
        mockMainViewModel.checkStatusDeposit(any())
    } returns Unit

    coEvery {
        mockMainViewModel.checkStatusTransactionLoading
    } returns MutableLiveData(checkStatusTransactionLoading)

    coEvery {
        mockMainViewModel.checkStatusTransactionResponse
    } returns MutableLiveData(checkStatusTransactionResponse)

    coEvery {
        mockMainViewModel.checkStatusTransaction(any())
    } returns Unit

    return MainViewModelMockData(
        mockMainViewModel = mockMainViewModel,
        logEventsServiceImpl = logEventsServiceImpl,
        mockCountDownTimerServiceImpl = mockCountDownTimerServiceImpl,
    )
}