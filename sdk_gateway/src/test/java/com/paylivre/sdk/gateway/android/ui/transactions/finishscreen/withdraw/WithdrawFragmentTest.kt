package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.BuildConfig
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.*
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.services.countdowntimer.CountDownTimerService
import com.paylivre.sdk.gateway.android.services.countdowntimer.MockCountDownTimerGivenHelper
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImplTest
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class WithdrawFragmentTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var fileTestsUtils = FileTestsUtils()

    @After
    fun tearDown() {
        stopKoin()
    }

    data class WithdrawMainViewModelMockData(
        var mockMainViewModel: MainViewModel,
        val logEventsServiceImpl: LogEventsService,
        val mockCountDownTimerServiceImpl: CountDownTimerService,
    )

    private var checkStatusOrderResponseMock = CheckStatusOrderResponse(
        isLoading = false,
        isSuccess = false,
        data = null,
        error = null
    )

    private var statusTransactionResponseMock = StatusTransactionResponse(
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

    private fun createWithdrawMainViewModel(
        statusTransactionResponse: StatusTransactionResponse? = statusTransactionResponseMock,
        checkStatusOrderResponse: CheckStatusOrderResponse = checkStatusOrderResponseMock,
        statusWithdrawOrderExpected: StatusWithdrawOrder = statusWithdrawOrderMock,
        mockCountDownTimerServiceImpl: CountDownTimerService = MockCountDownTimerGivenHelper(),
    ): WithdrawMainViewModelMockData {
        val mockMainViewModel: MainViewModel = mockk()
        val logEventsServiceImpl = LogEventsServiceImplTest()

        loadKoinModules(module(override = true) {
            single<LogEventsService> {
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
            mockMainViewModel.logoResId
        } returns MutableLiveData(-1)

        coEvery {
            mockMainViewModel.operation
        } returns MutableLiveData(Operation.WITHDRAW.code)

        coEvery {
            mockMainViewModel.logoUrl
        } returns MutableLiveData(null)

        coEvery {
            mockMainViewModel.statusResponseTransaction
        } returns MutableLiveData(statusTransactionResponse)

        coEvery {
            mockMainViewModel.checkStatusOrderDataResponse
        } returns MutableLiveData(checkStatusOrderResponse)

        coEvery {
            mockMainViewModel.statusWithdrawOrder
        } returns MutableLiveData(statusWithdrawOrderMock)


        fun setStatusWithdrawOrder() {
            coEvery {
                mockMainViewModel.statusWithdrawOrder
            } returns MutableLiveData(statusWithdrawOrderExpected)
        }

        coEvery {
            mockMainViewModel.setStatusWithdrawOrder(statusWithdrawOrderExpected)
        } returns setStatusWithdrawOrder()

        return WithdrawMainViewModelMockData(
            mockMainViewModel = mockMainViewModel,
            logEventsServiceImpl = logEventsServiceImpl,
            mockCountDownTimerServiceImpl = mockCountDownTimerServiceImpl
        )
    }

    private fun getBitmap(drawable: Drawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun checkTextView(textView: TextView?, expectedValue: String?) {
        Assert.assertEquals(View.VISIBLE, textView?.visibility)
        Assert.assertEquals(expectedValue, textView?.text)
    }

    private fun checkDrawableImageView(imageView: ImageView?, drawableExpected: Drawable): Boolean {
        return try {
            val bitmap = imageView?.let { getBitmap(it.drawable) }
            val otherBitmap = getBitmap(drawableExpected)
            bitmap!!.sameAs(otherBitmap)
        } catch (e: Exception) {
            false
        }
    }

    private fun checkHeaderWithdraw(fragment: Fragment) {
        //check button close sdk
        val headerView = fragment?.view?.findViewById<FragmentContainerView>(R.id.header_title)
        Assert.assertEquals(View.VISIBLE, headerView?.visibility)

        val buttonCloseSDK = headerView?.findViewById<MaterialButton>(R.id.ButtonCloseSDK)
        Assert.assertEquals(View.VISIBLE, buttonCloseSDK?.visibility)
        Assert.assertEquals("Voltar", buttonCloseSDK?.text)

        checkTextView(headerView?.findViewById(R.id.textViewOperation), "Saque")
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

    private fun checkWithdrawStatus(
        fragment: Fragment,
        statusPayment: String,
        statusInMerchant: String,
        iconStatusInMerchantId: Int,
        iconStatusPaymentId: Int,
    ) {
        val withdrawStatus =
            fragment?.view?.findViewById<FragmentContainerView>(R.id.fragmentWithdrawStatus)

        Assert.assertEquals(View.VISIBLE, withdrawStatus?.visibility)

        checkTextView(withdrawStatus?.findViewById(R.id.txtTitleDetails), "Detalhes")
        checkTextView(withdrawStatus?.findViewById(R.id.txtTitleStatus), "Status")
        checkTextView(withdrawStatus?.findViewById(R.id.txtLabelStatusInMerchant),
            "Aprovação pelo Merchant:")
        checkTextView(withdrawStatus?.findViewById(R.id.txtStatusInMerchant),
            statusPayment)

        checkTextView(withdrawStatus?.findViewById(R.id.txtLabelStatusPayment),
            "Pagamento:")
        checkTextView(withdrawStatus?.findViewById(R.id.txtStatusPayment),
            statusInMerchant)

        //Check icons status
        //Check merchant approval icon status
        val imgIconStatusInMerchant =
            withdrawStatus?.findViewById<ImageView>(R.id.iconStatusInMerchant)
        val logoMerchantExpectedDrawable: Drawable =
            fragment.resources.getDrawable(iconStatusInMerchantId)
        Assert.assertTrue(checkDrawableImageView(imgIconStatusInMerchant,
            logoMerchantExpectedDrawable))

        //Check payment icon status
        val imgIconStatusPayment = withdrawStatus?.findViewById<ImageView>(R.id.iconStatusPayment)
        val imgIconStatusPaymentExpectedDrawable: Drawable =
            fragment.resources.getDrawable(iconStatusPaymentId)
        Assert.assertTrue(checkDrawableImageView(imgIconStatusPayment,
            imgIconStatusPaymentExpectedDrawable))
    }

    private fun checkComponentsBottom(fragment: Fragment) {
        Assert.assertEquals(View.VISIBLE,
            fragment?.view?.findViewById<ConstraintLayout>(R.id.payment_by_paylivre)?.visibility)

        Assert.assertEquals(View.VISIBLE,
            fragment?.view?.findViewById<LinearLayout>(R.id.need_helper)?.visibility)

        Assert.assertEquals(View.VISIBLE,
            fragment?.view?.findViewById<LinearLayout>(R.id.legal_notice)?.visibility)
    }

    @Test
    fun `CASE 01, given status order withdraw PIX is success and final_amount is not null`() {
        //Create mocked instances
        //GIVEN
        val statusTransactionResponseDataString =
            fileTestsUtils.loadJsonAsString("res_withdraw_pix_success.json")
        val statusTransactionResponseSuccessData = Gson().fromJson(
            statusTransactionResponseDataString, ResponseCommonTransactionData::class.java)

        val checkStatusOrderResponseDataString =
            fileTestsUtils.loadJsonAsString("res_gateway_status_withdraw_pix_success.json")
        val checkStatusOrderResponseDataSuccess = Gson().fromJson(
            checkStatusOrderResponseDataString, CheckStatusOrderDataResponse::class.java)
        val checkStatusOrderResponse = CheckStatusOrderResponse(
            isLoading = false,
            isSuccess = true,
            data = checkStatusOrderResponseDataSuccess,
        )
        val statusTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = statusTransactionResponseSuccessData,
            error = null
        )
        val withdrawOrderStatusExpected = StatusWithdrawOrder(
            withdrawal_type_id = checkStatusOrderResponseDataSuccess?.withdrawal_type_id,
            withdrawal_status_id = checkStatusOrderResponseDataSuccess?.withdrawal?.status_id,
            merchant_approval_status_id = checkStatusOrderResponseDataSuccess?.order?.merchant_approval_status_id,
            order_status_id = checkStatusOrderResponseDataSuccess?.order?.status_id
        )

        val withdrawMainViewModelMockData =
            createWithdrawMainViewModel(statusTransactionResponse,
                checkStatusOrderResponse,
                withdrawOrderStatusExpected)

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawFragment ->
            //To run childFragmentManager transactions
            withdrawFragment.childFragmentManager.executePendingTransactions()

            //THEN
            //check header
            checkHeaderWithdraw(withdrawFragment)

            //check withdraw status
            checkWithdrawStatus(fragment = withdrawFragment,
                statusPayment = "Completo",
                statusInMerchant = "Aguardando pagamento",
                iconStatusInMerchantId = R.drawable.ic_completed,
                iconStatusPaymentId = R.drawable.ic_rotate_cw
            )

            //Check Title type withdraw
            checkTextView(withdrawFragment?.view?.findViewById(R.id.titleWithdrawType),
                "PIX")

            //Check Data Transaction
            val containerFragmentTransactionData =
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.containerFragmentTransactionData)
            Assert.assertEquals(
                View.VISIBLE,
                containerFragmentTransactionData
                    ?.visibility
            )
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textIdValue),
                "17393")
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textOriginalAmountValue),
                "R$ 5,00")
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textTaxAmountValue),
                "R$ 0,00")
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textTotalAmountValue),
                "R$ 5,00")
            //Check kyc limit value
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textLimitAmountValue),
                "R$ 99.999.999.268,08")

            //Check terms
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<FragmentContainerView>(R.id.containerAcceptTerms)
                    ?.visibility
            )
            //Check button back to merchant
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<FragmentContainerView>(R.id.fragmentBackMerchant)
                    ?.visibility
            )

            //Check Withdraw Pix instructions
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.instructionsPix)
                    ?.visibility
            )
            Assert.assertEquals(
                View.GONE,
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.instructionsWallet)
                    ?.visibility
            )

            //check components bottom
            checkComponentsBottom(withdrawFragment)

            //Check setStatusWithdrawOrder given check status order is success
            verify {
                withdrawMainViewModelMockData.mockMainViewModel.setStatusWithdrawOrder(
                    withdrawOrderStatusExpected
                )
            }

        }
    }

    @Test
    fun `CASE 02, given status order withdraw Wallet is success and final_amount is not null`() {
        //Create mocked instances
        //GIVEN
        val statusTransactionResponseDataString =
            fileTestsUtils.loadJsonAsString("res_withdraw_wallet_success.json")
        val statusTransactionResponseSuccessData = Gson().fromJson(
            statusTransactionResponseDataString, ResponseCommonTransactionData::class.java)

        val checkStatusOrderResponseDataString =
            fileTestsUtils.loadJsonAsString("res_gateway_status_withdraw_wallet_success.json")
        val checkStatusOrderResponseDataSuccess = Gson().fromJson(
            checkStatusOrderResponseDataString, CheckStatusOrderDataResponse::class.java)
        val checkStatusOrderResponse = CheckStatusOrderResponse(
            isLoading = false,
            isSuccess = true,
            data = checkStatusOrderResponseDataSuccess,
        )
        val statusTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = statusTransactionResponseSuccessData,
            error = null
        )
        val withdrawOrderStatusExpected = StatusWithdrawOrder(
            withdrawal_type_id = checkStatusOrderResponseDataSuccess?.withdrawal_type_id,
            withdrawal_status_id = checkStatusOrderResponseDataSuccess?.withdrawal?.status_id,
            merchant_approval_status_id = checkStatusOrderResponseDataSuccess?.order?.merchant_approval_status_id,
            order_status_id = checkStatusOrderResponseDataSuccess?.order?.status_id
        )

        val withdrawMainViewModelMockData =
            createWithdrawMainViewModel(statusTransactionResponse,
                checkStatusOrderResponse,
                withdrawOrderStatusExpected)

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawFragment ->
            //To run childFragmentManager transactions
            withdrawFragment.childFragmentManager.executePendingTransactions()

            //THEN
            //check header
            checkHeaderWithdraw(withdrawFragment)

            //check withdraw status
            checkWithdrawStatus(fragment = withdrawFragment,
                statusPayment = "Completo",
                statusInMerchant = "Completo",
                iconStatusInMerchantId = R.drawable.ic_completed,
                iconStatusPaymentId = R.drawable.ic_completed
            )

            //Check Title type withdraw
            checkTextView(withdrawFragment?.view?.findViewById(R.id.titleWithdrawType),
                "Carteira Paylivre")

            //Check Data Transaction
            val containerFragmentTransactionData =
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.containerFragmentTransactionData)
            Assert.assertEquals(
                View.VISIBLE,
                containerFragmentTransactionData
                    ?.visibility
            )
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textIdValue),
                "17419")
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textOriginalAmountValue),
                "R$ 5,00")
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textTaxAmountValue),
                "R$ 0,00")
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textTotalAmountValue),
                "R$ 5,00")
            //Check kyc limit value
            checkTextView(containerFragmentTransactionData?.findViewById(R.id.textLimitAmountValue),
                "R$ 99.999.999.094,08")

            //Check terms
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<FragmentContainerView>(R.id.containerAcceptTerms)
                    ?.visibility
            )
            //Check button back to merchant
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<FragmentContainerView>(R.id.fragmentBackMerchant)
                    ?.visibility
            )

            //Check Withdraw Pix instructions
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.instructionsWallet)
                    ?.visibility
            )
            Assert.assertEquals(
                View.GONE,
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.instructionsPix)
                    ?.visibility
            )

            //check components bottom
            checkComponentsBottom(withdrawFragment)

            //Check setStatusWithdrawOrder given check status order is success
            verify {
                withdrawMainViewModelMockData.mockMainViewModel.setStatusWithdrawOrder(
                    withdrawOrderStatusExpected
                )
            }

        }
    }

    @Test
    fun `CASE 03, given status order withdraw is loading`() {
        //Create mocked instances
        //GIVEN
        createWithdrawMainViewModel()

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawFragment ->
            //To run childFragmentManager transactions
            withdrawFragment.childFragmentManager.executePendingTransactions()

            //THEN
            //check header
            checkHeaderWithdraw(withdrawFragment)

            //Check Title type withdraw
            checkTextView(withdrawFragment?.view?.findViewById(R.id.titleWithdrawType),
                "Carteira Paylivre")

            //Check loading withdraw status bar
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<FragmentContainerView>(R.id.fragmentCheckingWithdrawLoading)
                    ?.visibility
            )

            //Check Data Transaction is hidden
            val containerFragmentTransactionData =
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.containerFragmentTransactionData)
            Assert.assertEquals(
                View.GONE,
                containerFragmentTransactionData
                    ?.visibility
            )

            //Check terms
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<FragmentContainerView>(R.id.containerAcceptTerms)
                    ?.visibility
            )
            //Check button back to merchant
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<FragmentContainerView>(R.id.fragmentBackMerchant)
                    ?.visibility
            )

            //Check Withdraw Pix instructions
            Assert.assertEquals(
                View.VISIBLE,
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.instructionsWallet)
                    ?.visibility
            )
            Assert.assertEquals(
                View.GONE,
                withdrawFragment?.view?.findViewById<ConstraintLayout>(R.id.instructionsPix)
                    ?.visibility
            )

            //check components bottom
            checkComponentsBottom(withdrawFragment)

        }
    }

    @Test
    fun `CASE 04, given countDownTimer is finish`() {
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()

        val withdrawMainViewModelMockData = createWithdrawMainViewModel(
            mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
        )

        //GIVEN
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawFragment ->
            //To run childFragmentManager transactions
            withdrawFragment.childFragmentManager.executePendingTransactions()

            //WHEN
            mockCountDownTimerGivenHelper.dispatchOnFinish()


            //THEN
            //Check is not showing transaction data
            Assert.assertEquals(
                View.GONE,
                withdrawFragment?.view
                    ?.findViewById<ConstraintLayout>(R.id.containerFragmentTransactionData)
                    ?.visibility
            )

            //Check instruction to check withdraw status in email
            val txtViewCheckOrderWithdrawInEmail =
                withdrawFragment?.view?.findViewById<TextView>(R.id.txtViewCheckOrderWithdrawInEmail)
            Assert.assertEquals(
                View.VISIBLE,
                txtViewCheckOrderWithdrawInEmail?.visibility
            )
            Assert.assertEquals(
                "Confira seu email para maiores detalhes.",
                txtViewCheckOrderWithdrawInEmail?.text
            )

        }
    }

    @Test
    fun `CASE 05, check dispatchCheckStatusWithdraw given secondsLeft == restDivision`() {
        //GIVEN
        val statusTransactionResponseDataString =
            fileTestsUtils.loadJsonAsString("res_withdraw_pix_success.json")
        val statusTransactionResponseSuccessData = Gson().fromJson(
            statusTransactionResponseDataString, ResponseCommonTransactionData::class.java)
        val statusTransactionResponse = StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = statusTransactionResponseSuccessData,
            error = null
        )
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()
        val withdrawMainViewModelMockData = createWithdrawMainViewModel(
            statusTransactionResponse = statusTransactionResponse,
            mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper
        )

        coEvery {
            withdrawMainViewModelMockData.mockMainViewModel.checkStatusOrder(any())
        } returns Unit

        //GIVEN
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawFragment ->
            //To run childFragmentManager transactions
            withdrawFragment.childFragmentManager.executePendingTransactions()

            mockCountDownTimerGivenHelper.dispatchOnTick(10000)

            verify {
                withdrawMainViewModelMockData.mockMainViewModel.checkStatusOrder(
                    CheckStatusOrderDataRequest(
                        17393,
                        "JDJ5JDEwJHIxVWk0RnhCNklLbVBTUG9KdHZWSWVFcmRVSE5XUTQ0MFBtL0JZZDk1dEFYYmxHMUV1TFRp"
                    ))
            }
        }
    }

    @Test
    fun `CASE 06, check dispatch CountDownTimer cancel given fragment is destroy`() {
        //GIVEN
        val mockCountDownTimerGivenHelper = MockCountDownTimerGivenHelper()
        createWithdrawMainViewModel(mockCountDownTimerServiceImpl = mockCountDownTimerGivenHelper)

        //GIVEN
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawFragment ->
            //To run childFragmentManager transactions
            withdrawFragment.childFragmentManager.executePendingTransactions()

            //WHEN
            withdrawFragment.onDestroy()

            //THEN
            Assert.assertTrue(mockCountDownTimerGivenHelper.calledCancel)
        }
    }

    @Test
    fun `CASE 07, check merchant approval is pending`() {
        //GIVEN
        var checkStatusOrderResponseMock = CheckStatusOrderResponse(
            isLoading = false,
            isSuccess = false,
            data = CheckStatusOrderDataResponse(
                order = Order(
                    null, null, null, null, null,
                    merchant_approval_status_id = MerchantApprovalStatusOrder.PENDING.code,
                    merchant_approval_status_name = "Pending"
                )
            ),
            error = null
        )

        createWithdrawMainViewModel(checkStatusOrderResponse = checkStatusOrderResponseMock)

        //GIVEN
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<WithdrawFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { withdrawFragment ->
            //To run childFragmentManager transactions
            withdrawFragment.childFragmentManager.executePendingTransactions()

            //THEN
            checkTextView(withdrawFragment?.view?.findViewById(R.id.txtViewMessageMerchantApprovalPending),
                "O saque será processado assim que a retirada do valor de sua conta for aprovada. Você receberá um email notificando a aprovação."
            )

            //Check is not showing transaction data
            Assert.assertEquals(
                View.GONE,
                withdrawFragment?.view
                    ?.findViewById<ConstraintLayout>(R.id.containerFragmentTransactionData)
                    ?.visibility
            )
        }
    }
}