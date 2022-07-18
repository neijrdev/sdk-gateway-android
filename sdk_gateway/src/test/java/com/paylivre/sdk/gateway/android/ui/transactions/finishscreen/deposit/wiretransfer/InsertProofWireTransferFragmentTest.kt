package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.deposit.wiretransfer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.paylivre.sdk.gateway.android.FileTestsUtils
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.ResponseCommonTransactionData
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.data.model.transferProof.InsertTransferProofDataRequest
import com.paylivre.sdk.gateway.android.domain.model.OriginTypeInsertProof
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.services.log.LogEventsServiceImplTest
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import io.mockk.*
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.net.URI

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class InsertProofWireTransferFragmentTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var fileTestsUtils = FileTestsUtils()

    @Before
    fun setup() {
        //configure a mocked MainViewModel instance for all tests
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }


    private fun getMockStatusTransactionResponseSuccess(): StatusTransactionResponse {
        val responseExpectedString =
            fileTestsUtils.loadJsonAsString("res_deposit_wiretransfer_success.json")

        val expectedDataResponse = Gson().fromJson(
            responseExpectedString, ResponseCommonTransactionData::class.java
        )

        return StatusTransactionResponse(
            isLoading = false,
            isSuccess = true,
            data = expectedDataResponse,
            error = null
        )
    }


    @Test
    fun `CASE 01, inserted the proof`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<InsertProofWireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { insertProofWireTransferFragment ->
            //GIVEN
            val mockStatusTransactionResponse = getMockStatusTransactionResponseSuccess()
            insertProofWireTransferFragment.mainViewModel.setStatusTransactionResponse(
                mockStatusTransactionResponse
            )

            //WHEN
            insertProofWireTransferFragment.mainViewModel.setOriginTypeInsertProof(
                OriginTypeInsertProof.CAMERA
            )

            insertProofWireTransferFragment.mainViewModel.setOriginTypeInsertProof(
                OriginTypeInsertProof.GALLERY
            )

            val mockIntent = Intent()
            val uri: Uri =
                Uri.parse("file:///storage/emulated/0/Android/data/com.example.paylivre.sdk.gateway/files/DCIM/IMG_20220603_001948249.jpg")
            mockIntent.data = uri
            insertProofWireTransferFragment.handleImagePickerSuccess(mockIntent)

            //THEN
            //Check data containerInsertProof
            Assert.assertEquals("Comprovante de depósito",
                insertProofWireTransferFragment.view?.findViewById<TextView>(R.id.textTitleDepositProof)?.text.toString())
            Assert.assertEquals("Nenhum arquivo selecionado",
                insertProofWireTransferFragment.view?.findViewById<TextView>(R.id.textNameSelectedFile)?.text.toString())
            Assert.assertEquals(View.VISIBLE,
                insertProofWireTransferFragment.view?.findViewById<ConstraintLayout>(R.id.btnChooseFile)?.visibility)
            Assert.assertEquals("Alterar arquivo escolhido",
                insertProofWireTransferFragment.view?.findViewById<TextView>(R.id.txtChooseFile)?.text.toString())
            Assert.assertEquals("O arquivo deve ser do tipo: jpg, png, jpeg e tamanho máximo de 5MB.",
                insertProofWireTransferFragment.view?.findViewById<TextView>(R.id.textInstructionFile)?.text.toString())

            //Button submit
            Assert.assertEquals(View.VISIBLE,
                insertProofWireTransferFragment.view?.findViewById<Button>(R.id.btnSubmit)?.visibility)
            Assert.assertEquals(true,
                insertProofWireTransferFragment.view?.findViewById<Button>(R.id.btnSubmit)?.isEnabled)
            Assert.assertEquals("Enviar",
                insertProofWireTransferFragment.view?.findViewById<Button>(R.id.btnSubmit)?.text.toString())
        }
    }

    @Test
    fun `CASE 02, setProofImageUri given Uri is null`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<InsertProofWireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { insertProofWireTransferFragment ->
            insertProofWireTransferFragment.mainViewModel.setProofImageUri(null)
        }
    }

    @Test
    fun `CASE 03, test btnChooseFile`() {
        val mockMainViewModel: MainViewModel = mockk()
        val logEventsServiceImpl = LogEventsServiceImplTest()

        loadKoinModules(module(override = true) {
            single<LogEventsService> {
                logEventsServiceImpl
            }
            viewModel {
                mockMainViewModel
            }
        })

        coEvery {
            mockMainViewModel.proof_image_uri
        } returns MutableLiveData(null)

        coEvery {
            mockMainViewModel.statusResponseTransaction
        } returns MutableLiveData(StatusTransactionResponse(isLoading = false,
            isSuccess = false,
            null,
            null))

        coEvery {
            mockMainViewModel.origin_type_insert_proof
        } returns MutableLiveData(null)

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<InsertProofWireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { insertProofWireTransferFragment ->
            val btnChooseFile =
                insertProofWireTransferFragment.view?.findViewById<ConstraintLayout>(R.id.btnChooseFile)

            btnChooseFile?.performClick()

            //To run childFragmentManager transactions
            insertProofWireTransferFragment.childFragmentManager.executePendingTransactions()

            Assert.assertEquals(
                mutableListOf<String?>(
                    "Btn_ChooseFile",
                    "ModalSelectOriginImportProof"),
                logEventsServiceImpl.logEventAnalytics)
        }
    }

    @Test
    fun `CASE 04, test btnSubmit`() {
        val mockMainViewModel: MainViewModel = mockk()
        val logEventsServiceImpl = LogEventsServiceImplTest()
        val uriMocked: Uri =
            Uri.parse("file:///storage/emulated/0/Android/data/com.example.paylivre.sdk.gateway/files/DCIM/IMG_20220603_001948249.jpg")

        loadKoinModules(module(override = true) {
            single<LogEventsService> {
                logEventsServiceImpl
            }
            viewModel {
                mockMainViewModel
            }
        })

        coEvery {
            mockMainViewModel.proof_image_uri
        } returns MutableLiveData(null)

        coEvery {
            mockMainViewModel.statusResponseTransaction
        } returns MutableLiveData(getMockStatusTransactionResponseSuccess())

        coEvery {
            mockMainViewModel.origin_type_insert_proof
        } returns MutableLiveData(null)

        coEvery {
            mockMainViewModel.setProofImageUri(any())
        } returns Unit

        coEvery {
            mockMainViewModel.insertTransferProof(any())
        } returns Unit

        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<InsertProofWireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { insertProofWireTransferFragment ->
            //GIVEN
            val mockIntent = Intent()

            mockIntent.data = uriMocked
            insertProofWireTransferFragment.handleImagePickerSuccess(mockIntent)
            val btnSubmit =
                insertProofWireTransferFragment.view?.findViewById<Button>(R.id.btnSubmit)

            //THEN
            btnSubmit?.performClick()
            //To run childFragmentManager transactions
            insertProofWireTransferFragment.childFragmentManager.executePendingTransactions()

            verify {
                mockMainViewModel.setProofImageUri(uriMocked)
            }

            verify {
                mockMainViewModel.insertTransferProof(InsertTransferProofDataRequest(
                    file = File(uriMocked.path),
                    order_id = 17128,
                    token = "JDJ5JDEwJHkxdXRra1MubTZCaXZSRzI3Tzd3US4uNEt5aWhiMFFYVVBiZy9XTU1pZG5DQ0dzTkdpQ29h"
                ))
            }
        }
    }

    @Test
    fun `CASE 05, test handleImagePicker`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<InsertProofWireTransferFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { insertProofWireTransferFragment ->
            insertProofWireTransferFragment.handleImagePickerError(null)
            insertProofWireTransferFragment.handleImagePickerCancelled()
        }
    }

}