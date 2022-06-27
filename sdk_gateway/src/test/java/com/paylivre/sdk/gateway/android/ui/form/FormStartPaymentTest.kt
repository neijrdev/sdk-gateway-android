package com.paylivre.sdk.gateway.android.ui.form

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.ErrorTransaction
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.DataPixApprovalTime
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.PixApprovalTimeResponse
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Type
import com.paylivre.sdk.gateway.android.domain.model.TypePixKey
import com.paylivre.sdk.gateway.android.getOrAwaitValueTest
import com.paylivre.sdk.gateway.android.utils.ERROR_INVALID_USER_NAME_OR_PASSWORD
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.viewmodel.MockMainViewModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], qualifiers = "pt-port")
class FormFragmentTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        loadKoinModules(MockMainViewModel().mockedAppModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `CASE 1- Deposit only Pix, given document = "" and email = "", RENDER COMPONENTS`() {
        //1 - Verificar Render
        // 1.0 - Botão Voltar (Texto, is enabled)
        // 1.1 - Header (Tipo de operacao, logo)
        // 1.2 - Form
        //      1.2.0 - Titulo form, campos email e documents vazios
        //      1.2.1 - Status estimativa tempo pix (Tag, e tempo estimado)
        //      1.2.2 - Titulo types (se o type é pix)
        //      1.2.3 - Botão continuar


        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            formStartPayment.mainViewModel.setPixApprovalTime(
                PixApprovalTimeResponse(
                    status = "success",
                    status_code = 200,
                    message = "OK",
                    data = DataPixApprovalTime(
                        average_age = "00:01:10",
                        deposit_age_minutes = "30",
                        deposit_status_id = "2",
                        level = "normal",
                        level_id = 1
                    )
                )
            )

            formStartPayment.mainViewModel.setButtonTypeSelected(
                1
            )

            //THEN
            //1.0 - Button back (Close SDK)
            val buttonCloseSDK =
                formStartPayment.view?.findViewById<MaterialButton>(R.id.ButtonCloseSDK)
            buttonCloseSDK?.let { button ->
                assertTrue(button?.isEnabled)
                assertTrue(button?.text == "Voltar")
                assertTrue(button?.icon.isVisible)
            }
            //1.1 - Header (Tipo de operacao, logo, versão)
            assertEquals("Depósito", //Operation
                formStartPayment.view?.findViewById<TextView>(R.id.textViewOperation)?.text.toString())
            assertNotNull(
                formStartPayment.view?.findViewById<ImageView>(R.id.logoMerchant)?.drawable)

            //1.2.0 - Title form valid, fields email and document is empty
            assertEquals("Preencha os campos abaixo para continuar", //title
                formStartPayment.view?.findViewById<TextView>(R.id.textViewTitleForm)?.text.toString())
            assertEquals("", //edit email
                formStartPayment.view?.findViewById<EditText>(R.id.editEmail)?.text.toString())
            assertEquals("", //edit document
                formStartPayment.view?.findViewById<EditText>(R.id.editEmail)?.text.toString())

            // 1.2.1 - Status estimativa tempo pix (Tag, e tempo estimado)
            assertNotNull(
                "NORMAL",
                formStartPayment.view?.findViewById<TextView>(R.id.statusPixApprovalTime)?.text.toString())
            assertNotNull(
                "1 minuto(s) e 10 segundo(s) para conclusão.",
                formStartPayment.view?.findViewById<TextView>(R.id.textPixApprovalTime)?.text.toString())

            // 1.2.2 - Titulo types (se tem a logo do pix e esta vazio)
            assertNotNull(
                "Escolha a opção de Depósito",
                formStartPayment.view?.findViewById<TextView>(R.id.textViewTitleTypes)?.text.toString())
            assertEquals(1, formStartPayment.mainViewModel.buttonTypeSelected.getOrAwaitValueTest())

            //1.2.3 - Botão continuar
            formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)
                ?.let { button ->
                    assertTrue(button?.isEnabled)
                    assertTrue(button?.text == "Continuar")
                }
        }
    }


    @Test
    fun `CASE 2 - Deposit only Wallet, success StartPayment`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            Navigation.setViewNavController(formStartPayment.requireView(), mockNavController)

            //Inputs
            val editEmail = formStartPayment.view?.findViewById<EditText>(R.id.editEmail)
            val editDocument = formStartPayment.view?.findViewById<EditText>(R.id.editDocument)
            val editEmailWallet =
                formStartPayment.view?.findViewById<EditText>(R.id.editEmailWallet)
            val editApiToken = formStartPayment.view?.findViewById<EditText>(R.id.editApiToken)

            //TextView Errors Inputs
            val textErrorEditEmail =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmail)
            val textErrorEditDocument =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorDocument)
            val textErrorEditEmailWallet =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmailWallet)
            val textErrorEditApiToken =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorApiToken)


            //Button Start Payment
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)

            formStartPayment.mainViewModel.setOperation(Operation.DEPOSIT.code)
            editApiToken?.setText("123das45da65ssadasfads")
            formStartPayment.mainViewModel.setEditEmail("test@test.com")
            formStartPayment.mainViewModel.setEditDocument("61317581075")
            formStartPayment.mainViewModel.setButtonTypeSelected(Type.WALLET.code)
            formStartPayment.mainViewModel.setOperation(Operation.DEPOSIT.code)


            //THEN
            assertEquals("test@test.com", editEmail?.text.toString())
            assertEquals("613.175.810-75", editDocument?.text.toString())
            assertEquals("test@test.com", editEmailWallet?.text.toString())
            assertEquals("123das45da65ssadasfads", editApiToken?.text.toString())


            //Test to open modal about Generate Api Token
            val textViewHotGenerateApiToken =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewHotGenerateApiToken)
            assertEquals(View.VISIBLE, textViewHotGenerateApiToken?.visibility)
            assertEquals("Como gerar o api token?", textViewHotGenerateApiToken?.text.toString())
            textViewHotGenerateApiToken?.performClick()

            //Handle click in continue
            btnStartPayment?.performClick()

            assertTrue(textErrorEditEmail?.visibility == View.GONE)
            assertTrue(textErrorEditDocument?.visibility == View.GONE)
            assertTrue(textErrorEditEmailWallet?.visibility == View.GONE)
            assertTrue(textErrorEditApiToken?.visibility == View.GONE)

            verify(mockNavController).navigate(R.id.navigation_loading_transaction)
        }
    }

    @Test
    fun `CASE 3 - Deposit only Wallet, validate errors forms fields empty`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            //TextView Errors Inputs
            val textErrorEditEmail =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmail)
            val textErrorEditDocument =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorDocument)
            val textErrorEditEmailWallet =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmailWallet)
            val textErrorEditApiToken =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorApiToken)
            //Button Start Payment
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)

            //WHEN
            formStartPayment.mainViewModel.setButtonTypeSelected(Type.WALLET.code)
            formStartPayment.mainViewModel.setOperation(Operation.DEPOSIT.code)
            //Handle click in continue
            btnStartPayment?.performClick()

            //THEN
            assertTrue(textErrorEditEmail?.visibility == View.VISIBLE)
            assertTrue(textErrorEditDocument?.visibility == View.VISIBLE)
            assertTrue(textErrorEditEmailWallet?.visibility == View.VISIBLE)
            assertTrue(textErrorEditApiToken?.visibility == View.VISIBLE)

            assertEquals("Campo obrigatório.", textErrorEditEmail?.text.toString())
            assertEquals("Campo obrigatório.", textErrorEditDocument?.text.toString())
            assertEquals("Campo obrigatório.", textErrorEditEmailWallet?.text.toString())
            assertEquals("Campo obrigatório.", textErrorEditApiToken?.text.toString())
        }
    }

    @Test
    fun `CASE 4 - Deposit only Wallet, validate errors forms fields invalid`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            //Inputs
            val editEmail = formStartPayment.view?.findViewById<EditText>(R.id.editEmail)
            val editDocument = formStartPayment.view?.findViewById<EditText>(R.id.editDocument)
            val editEmailWallet =
                formStartPayment.view?.findViewById<EditText>(R.id.editEmailWallet)
            //TextView Errors Inputs
            val textErrorEditEmail =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmail)
            val textErrorEditDocument =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorDocument)
            val textErrorEditEmailWallet =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmailWallet)
            val textErrorEditApiToken =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorApiToken)
            //Button Start Payment
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)

            //WHEN
            formStartPayment.mainViewModel.setButtonTypeSelected(Type.WALLET.code)
            formStartPayment.mainViewModel.setOperation(Operation.DEPOSIT.code)
            editEmail?.setText("email_invalid")
            editDocument?.setText("12345678912")
            editEmailWallet?.setText("email_invalid")
            //Handle click in continue
            btnStartPayment?.performClick()

            //THEN
            assertTrue(textErrorEditEmail?.visibility == View.VISIBLE)
            assertTrue(textErrorEditDocument?.visibility == View.VISIBLE)
            assertTrue(textErrorEditEmailWallet?.visibility == View.VISIBLE)
            assertTrue(textErrorEditApiToken?.visibility == View.VISIBLE)

            assertEquals("Email inválido.", textErrorEditEmail?.text.toString())
            assertEquals("Documento inválido.", textErrorEditDocument?.text.toString())
            assertEquals("Email inválido.", textErrorEditEmailWallet?.text.toString())
        }
    }

    @Test
    fun `CASE 5 - Deposit type (15 - Pix, Billet, Wallet e Ted), validate form error, no select type`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            val textViewErrorType =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorTypeForm)
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)

            //WHEN
            formStartPayment.mainViewModel.setType(15)
            formStartPayment.mainViewModel.setOperation(Operation.DEPOSIT.code)
            //Handle click in continue
            btnStartPayment?.performClick()

            //THEN
            assertEquals(15, formStartPayment.mainViewModel.type.getOrAwaitValueTest())
            assertEquals(View.VISIBLE, textViewErrorType?.visibility)
            assertEquals("Método de Depósito é obrigatório.", textViewErrorType?.text.toString())
        }
    }

    @Test
    fun `CASE 6 - Withdraw only PIX, validate pix_key_type = phone`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            val textViewErrorType =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorTypeForm)
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)
            val editPixKeyValue =
                formStartPayment.view?.findViewById<EditText>(R.id.editPixKeyCellPhone)
            val textViewErrorPixKeyValue =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorPixKeyValue)

            //WHEN
            //to Enable validation pix key
            formStartPayment.enableOnchangeValidateFields = true

            formStartPayment.mainViewModel.setOperation(Operation.WITHDRAW.code)
            formStartPayment.mainViewModel.setType(1)
            formStartPayment.mainViewModel.setButtonPixKeyTypeSelected(TypePixKey.PHONE.code)


            //THEN
            assertEquals(View.VISIBLE, textViewErrorPixKeyValue?.visibility)
            assertEquals("Campo obrigatório.", textViewErrorPixKeyValue?.text.toString())


            //WHEN
            editPixKeyValue?.setText("99 9999")
            //THEN
            assertEquals(View.VISIBLE, textViewErrorPixKeyValue?.visibility)
            assertEquals("Celular inválido.", textViewErrorPixKeyValue?.text.toString())

            //WHEN
            editPixKeyValue?.setText("99999999999")
            //THEN
            assertEquals(View.GONE, textViewErrorPixKeyValue?.visibility)

        }
    }

    @Test
    fun `CASE 7 - Withdraw only PIX, validate pix_key_type = email`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            val textViewErrorType =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorTypeForm)
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)
            val editPixKeyValue =
                formStartPayment.view?.findViewById<EditText>(R.id.editPixKeyEmail)
            val textViewErrorPixKeyValue =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorPixKeyValue)

            //WHEN
            //to Enable validation pix key
            formStartPayment.enableOnchangeValidateFields = true

            formStartPayment.mainViewModel.setOperation(Operation.WITHDRAW.code)
            formStartPayment.mainViewModel.setType(1)
            formStartPayment.mainViewModel.setButtonPixKeyTypeSelected(TypePixKey.EMAIL.code)


            //THEN
            assertEquals(View.VISIBLE, textViewErrorPixKeyValue?.visibility)
            assertEquals("Campo obrigatório.", textViewErrorPixKeyValue?.text.toString())


            //WHEN
            editPixKeyValue?.setText("test@test")
            //THEN
            assertEquals(View.VISIBLE, textViewErrorPixKeyValue?.visibility)
            assertEquals("Email inválido.", textViewErrorPixKeyValue?.text.toString())

            //WHEN
            editPixKeyValue?.setText("test@test.com")
            //THEN
            assertEquals(View.GONE, textViewErrorPixKeyValue?.visibility)

        }
    }

    @Test
    fun `CASE 8 - Withdraw only Pix, success StartPayment, pix_key_type = phone`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            Navigation.setViewNavController(formStartPayment.requireView(), mockNavController)

            //Inputs
            val editEmail = formStartPayment.view?.findViewById<EditText>(R.id.editEmail)
            val editDocument = formStartPayment.view?.findViewById<EditText>(R.id.editDocument)

            //TextView Errors Inputs
            val textErrorEditEmail =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmail)
            val textErrorEditDocument =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorDocument)


            //Button Start Payment
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)

            formStartPayment.mainViewModel.setTypeStartCheckout(TypesStartCheckout.BY_PARAMS.code)
            formStartPayment.mainViewModel.setOperation(Operation.WITHDRAW.code)
            formStartPayment.mainViewModel.setEditEmail("test@test.com")
            formStartPayment.mainViewModel.setEditDocument("61317581075")
            formStartPayment.mainViewModel.setButtonTypeSelected(Type.PIX.code)

            formStartPayment.mainViewModel.setButtonPixKeyTypeSelected(TypePixKey.PHONE.code)
            formStartPayment.mainViewModel.setEditPixKeyValue("73999999999")


            //THEN
            assertEquals("test@test.com", editEmail?.text.toString())
            assertEquals("613.175.810-75", editDocument?.text.toString())


            //Handle click in continue
            btnStartPayment?.performClick()

            assertTrue(textErrorEditEmail?.visibility == View.GONE)
            assertTrue(textErrorEditDocument?.visibility == View.GONE)

            verify(mockNavController).navigate(R.id.navigation_loading_transaction)
        }
    }

    @Test
    fun `CASE 9 - Withdraw only Pix, success StartPayment, pix_key_type = document`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)

        fragment.onFragment { formStartPayment ->
            //GIVEN
            Navigation.setViewNavController(formStartPayment.requireView(), mockNavController)

            //Inputs
            val editEmail = formStartPayment.view?.findViewById<EditText>(R.id.editEmail)
            val editDocument = formStartPayment.view?.findViewById<EditText>(R.id.editDocument)

            //TextView Errors Inputs
            val textErrorEditEmail =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorEmail)
            val textErrorEditDocument =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorDocument)


            //Button Start Payment
            val btnStartPayment = formStartPayment.view?.findViewById<Button>(R.id.btnStartPayment)


            formStartPayment.mainViewModel.setTypeStartCheckout(TypesStartCheckout.BY_PARAMS.code)
            formStartPayment.mainViewModel.setOperation(Operation.WITHDRAW.code)
            formStartPayment.mainViewModel.setEditEmail("test@test.com")
            formStartPayment.mainViewModel.setEditDocument("61317581075")
            formStartPayment.mainViewModel.setButtonTypeSelected(Type.PIX.code)

            formStartPayment.mainViewModel.setButtonPixKeyTypeSelected(TypePixKey.DOCUMENT.code)
            formStartPayment.mainViewModel.setEditPixKeyValue("61317581075")


            //THEN
            assertEquals("test@test.com", editEmail?.text.toString())
            assertEquals("613.175.810-75", editDocument?.text.toString())


            //Handle click in continue
            btnStartPayment?.performClick()

            assertTrue(textErrorEditEmail?.visibility == View.GONE)
            assertTrue(textErrorEditDocument?.visibility == View.GONE)

            verify(mockNavController).navigate(R.id.navigation_loading_transaction)
        }
    }

    @Test
    fun `CASE 10 - FormStartPayment, functions`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            formStartPayment.mainViewModel.setIsCloseKeyboard(true)

            formStartPayment.view?.findViewById<FragmentContainerView>(R.id.headerTitle)?.performClick()
            formStartPayment.view?.findViewById<ConstraintLayout>(R.id.containerForm)?.performClick()

            formStartPayment.onBackPressed()

            formStartPayment.onDestroyView()

        }
    }

    @Test
    fun `CASE 11 - Deposit Wallet, transaction failure, error api token`() {
        val fragmentArgs = Bundle()
        val fragment = launchFragmentInContainer<FormStartPaymentFragment>(fragmentArgs,
            themeResId = R.style.Theme_SDKGatewayAndroid)

        fragment.onFragment { formStartPayment ->
            formStartPayment.mainViewModel.setIsCloseKeyboard(true)

            formStartPayment.mainViewModel.transactionFailure(
                ErrorTransaction(
                    original_message = ERROR_INVALID_USER_NAME_OR_PASSWORD
                )
            )

            val textErrorEditApiToken =
                formStartPayment.view?.findViewById<TextView>(R.id.textViewErrorApiToken)

            assertEquals(View.VISIBLE, textErrorEditApiToken?.visibility)
            assertEquals("API Token Paylivre inválido", textErrorEditApiToken?.text.toString())
        }
    }
}
