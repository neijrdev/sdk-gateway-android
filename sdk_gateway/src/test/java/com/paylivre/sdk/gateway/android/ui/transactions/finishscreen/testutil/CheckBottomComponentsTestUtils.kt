package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.testutil

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.paylivre.sdk.gateway.android.R
import org.junit.Assert

class CheckBottomComponentsTestUtils(
    view: View?,
) {
    //Payment by Paylivre Data
    val txtPaymentByPaylivre = view?.findViewById<TextView>(R.id.textViewPaymentByPaylivre)
    val textViewCongratulations = view?.findViewById<TextView>(R.id.textViewCongratulations)
    val paymentByLogoPaylivre = view?.findViewById<ImageView>(R.id.paymentByLogoPaylivre)

    //Need Helper
    val textViewTitleHelper = view?.findViewById<TextView>(R.id.textViewTitleHelper)
    val textViewContacts = view?.findViewById<TextView>(R.id.textViewContacts)

    //Legal Notice
    val titleLegalNotice = view?.findViewById<TextView>(R.id.titleLegalNotice)
    val textLegalNoticePart1 = view?.findViewById<TextView>(R.id.textLegalNoticePart1)
    val textLegalNoticePart2 = view?.findViewById<TextView>(R.id.textLegalNoticePart2)
    val textLegalNoticePart3 = view?.findViewById<TextView>(R.id.textLegalNoticePart3)


    //ExpectedData
    val txtPaymentByPaylivreExpected = "Pagamento via"
    val textViewCongratulationsExpected = "Obrigado por usar a Paylivre"
    val textViewTitleHelperExpected = "Precisa de ajuda?"
    val textViewContactsExpected = "suporte@paylivre.com | 0800 500 0123 ou 3003 4559"
    val titleLegalNoticeExpected = "Aviso Legal"
    val textLegalNoticePart1Expected =
        "A Paylivre é uma empresa facilitadora de serviços de pagamento eletrônico que permite a você fazer depósitos e retiradas de valores em sites internacionais."
    val textLegalNoticePart2Expected =
        "A atuação da PAYLIVRE BRASIL atende os protocolos instituídos pelo BACEN e pela Lei 12.865/2013. A PAYLIVRE BRASIL não atua no Mercado de Capitais Brasileiro."
    val textLegalNoticePart3Expected =
        "O conteúdo aqui publicado não configura recomendação de investimento, aporte ou aposta. A PAYLIVRE BRASIL não capta recursos e não realiza investimento, aporte financeiro ou apostas."

    fun assertCheckData() {
        //Check is Showing
        Assert.assertEquals(View.VISIBLE, txtPaymentByPaylivre?.visibility)
        Assert.assertEquals(View.VISIBLE, paymentByLogoPaylivre?.visibility)
        Assert.assertEquals(View.VISIBLE, textViewCongratulations?.visibility)
        Assert.assertEquals(View.VISIBLE, textViewTitleHelper?.visibility)
        Assert.assertEquals(View.VISIBLE, textViewContacts?.visibility)
        Assert.assertEquals(View.VISIBLE, titleLegalNotice?.visibility)
        Assert.assertEquals(View.VISIBLE, textLegalNoticePart1?.visibility)
        Assert.assertEquals(View.VISIBLE, textLegalNoticePart2?.visibility)
        Assert.assertEquals(View.VISIBLE, textLegalNoticePart3?.visibility)

        Assert.assertEquals(txtPaymentByPaylivreExpected, txtPaymentByPaylivre?.text.toString())
        Assert.assertEquals(textViewCongratulationsExpected, textViewCongratulations?.text.toString())
        Assert.assertEquals(textViewTitleHelperExpected, textViewTitleHelper?.text.toString())
        Assert.assertEquals(textViewContactsExpected, textViewContacts?.text.toString())
        Assert.assertEquals(titleLegalNoticeExpected, titleLegalNotice?.text.toString())
        Assert.assertEquals(textLegalNoticePart1Expected, textLegalNoticePart1?.text.toString())
        Assert.assertEquals(textLegalNoticePart2Expected, textLegalNoticePart2?.text.toString())
        Assert.assertEquals(textLegalNoticePart3Expected, textLegalNoticePart3?.text.toString())
    }


}