package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.domain.model.TransactionStatus

fun setTextAcceptTerms(
    currentViewFragment: Fragment,
    viewFragment: Fragment,
    containerResourceId: Int,
    description: String = viewFragment.getString(R.string.text_accept_terms_finishscreen),
) {
    val bundle = Bundle()
    if (bundle != null) {
        bundle.putString("description", description)
    }
    viewFragment.arguments = bundle

    currentViewFragment.childFragmentManager.beginTransaction().apply {
        replace(containerResourceId, viewFragment)
        commit()
    }
}

fun setTransactionData(
    currentViewFragment: Fragment,
    viewFragment: Fragment,
    containerResourceId: Int,
    transactionId: Int? = 0,
    originalAmount: Int? = 0,
    originalCurrency: String?,
    taxAmount: Int? = 0,
    taxCurrency: String?,
    totalAmount: Int? = 0,
    finalCurrency: String?,
    dueDate: String?,
    limitsKyc: String?,
    language: String? = null
) {
    val bundle = Bundle()
    if (language != null) {
        bundle.putString("language", language)
    }
    if (transactionId != null) {
        bundle.putInt("transactionId", transactionId)
    }
    if (originalAmount != null) {
        bundle.putInt("originalAmount", originalAmount)
    }
    bundle.putString("originalCurrency", originalCurrency)
    if (taxAmount != null) {
        bundle.putInt("taxAmount", taxAmount)
        bundle.putString("taxCurrency", taxCurrency)
    }
    if (totalAmount != null) {
        bundle.putInt("totalAmount", totalAmount)
    }
    bundle.putString("finalCurrency", finalCurrency)
    bundle.putString("dueDate", dueDate)
    bundle.putString("limitsKyc", limitsKyc)
    viewFragment.arguments = bundle

    currentViewFragment.childFragmentManager.beginTransaction().apply {
        replace(containerResourceId, viewFragment)
        commit()
    }
}

fun setTransactionStatus(
    currentViewFragment: Fragment,
    viewFragment: Fragment,
    containerResourceId: Int,
    container: FragmentContainerView,
    statusId: Int? = TransactionStatus.PENDING.code,
) {
    val bundle = Bundle()
    if (statusId != null) {
        bundle.putInt("statusId", statusId)
    }
    viewFragment.arguments = bundle

    currentViewFragment.childFragmentManager.beginTransaction().apply {
        replace(containerResourceId, viewFragment)
        commit()
    }
    container.visibility = View.VISIBLE
}