package com.paylivre.sdk.gateway.android.ui.checkservices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.DataGenerateSignature
import com.paylivre.sdk.gateway.android.data.model.order.getDataAutoRequestUrlWithSelectedType
import com.paylivre.sdk.gateway.android.data.model.order.getDataWithOnlySelectedType
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentCheckServicesStatusBinding
import com.paylivre.sdk.gateway.android.domain.model.*
import com.paylivre.sdk.gateway.android.services.log.LogEventsService
import com.paylivre.sdk.gateway.android.utils.TypesStartCheckout
import com.paylivre.sdk.gateway.android.utils.getNameByTypesKeys
import com.paylivre.sdk.gateway.android.utils.getTypesKeyNameInNumberTypes
import io.sentry.Sentry
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CheckServicesStatus : Fragment() {

    private var _binding: FragmentCheckServicesStatusBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val logEventsService : LogEventsService by inject()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentCheckServicesStatusBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var type = -1
        var operation = -1
        var typeStartCheckout = -1
        var dataStartCheckout: DataStartCheckout? = null
        val emptyDataGenerateSignature = DataGenerateSignature(
            "", 0, "", "",
            "", "", "", "", "",
            "", "", "", "",
            "", "", "", "", 0
        )
        var dataStartPayment = emptyDataGenerateSignature

        //Set Log Analytics
        logEventsService.setLogEventAnalytics("Screen_CheckServicesStatus")

        fun checkIsAutoStart() {
            if (checkDataToAutoStartTransaction(dataStartPayment)) {
                if (typeStartCheckout == TypesStartCheckout.BY_PARAMS.code) {
                    val orderDataWithSelectedType = getDataWithOnlySelectedType(dataStartPayment)
                    mainViewModel.startPayment(orderDataWithSelectedType)
                } else {
                    val orderDataWithSelectedType = dataStartCheckout?.let {
                        getDataAutoRequestUrlWithSelectedType(it)
                    }

                    if (orderDataWithSelectedType != null) {
                        mainViewModel.startPaymentByURL(orderDataWithSelectedType)
                    } else {
                        Navigation.findNavController(view).navigate(R.id.navigation_fatal_error)
                    }
                }
                Navigation.findNavController(view).navigate(R.id.navigation_loading_transaction)
            } else {
                Navigation.findNavController(view).navigate(R.id.navigation_form_start_payment)
            }
        }

        mainViewModel.type.observe(viewLifecycleOwner) {
            type = it
        }

        mainViewModel.type_start_checkout.observe(viewLifecycleOwner) {
            typeStartCheckout = it

            Sentry.setExtra("type_start_checkout", TypesStartCheckout.fromInt(it).toString())
        }

        mainViewModel.dataStartCheckout.observe(viewLifecycleOwner) {
            dataStartCheckout = it
        }

        fun dispatchGetPixApprovalTime(TypeEnabled: Int) {
            val isTypePixContain = checkTypeEnable(TypeEnabled, Type.PIX.code)
            val isDeposit = operation == Operation.DEPOSIT.code

            if (isTypePixContain && isDeposit) {
                mainViewModel.getPixApprovalTime()
            }
        }

        mainViewModel.type.observe(viewLifecycleOwner) {
            type = it
        }


        mainViewModel.isFatalError.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(view).navigate(R.id.navigation_fatal_error)
            }
        }

        mainViewModel.data_start_payment.observe(viewLifecycleOwner) {
            dataStartPayment = if (dataStartPayment != null) {
                it!!
            } else {
                emptyDataGenerateSignature
            }
        }

        mainViewModel.operation.observe(viewLifecycleOwner) {
            operation = it
            if (it == Operation.DEPOSIT.code) {
                mainViewModel.getServicesStatus()
            } else {
                checkIsAutoStart()
            }
        }

        mainViewModel.getServicesStatusSuccess.observe(viewLifecycleOwner) {
            if (operation == Operation.DEPOSIT.code) {
                if (it.isSuccess == true) {
                    var typeNumberBitwiseAnd = type and it.typeStatusServices

                    //If all services is disabled -> Navigation to error screen
                    if (typeNumberBitwiseAnd == 0) {
                        val typesKeyNameDisabled = getTypesKeyNameInNumberTypes(type)
                        val depositsDisabled =
                            getNameByTypesKeys(requireContext(), typesKeyNameDisabled)
                        mainViewModel.setIsFatalError(true, "key_error_deposits_disabled")
                        mainViewModel.setErrorTags("$depositsDisabled.")
                        mainViewModel.setMessageDetailsError("key_error_wait_a_few_moments")

                        //Insert error not enabled services
                        // in mainViewModel to return in Register result data
                        mainViewModel.setStatusResponseCheckServices(
                            CheckEnablerServices(
                                messageError = getString(R.string.key_error_deposits_disabled) + " $depositsDisabled.",
                                messageErrorDetails = getString(R.string.key_error_wait_a_few_moments)
                            )
                        )
                        Navigation.findNavController(view).navigate(R.id.navigation_fatal_error)
                    } else {
                        dispatchGetPixApprovalTime(typeNumberBitwiseAnd)
                        mainViewModel.setType(typeNumberBitwiseAnd)
                        checkIsAutoStart()
                    }
                }

                if (it.isSuccess == false) {
                    mainViewModel.setType(type)
                    dispatchGetPixApprovalTime(type)
                    checkIsAutoStart()
                }
            } else {
                checkIsAutoStart()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}