package com.paylivre.sdk.gateway.android.ui.loadingTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.OrderDataRequest
import com.paylivre.sdk.gateway.android.data.model.order.StatusTransactionResponse
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.databinding.FragmentLoadingTransactionBinding
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Type
import com.paylivre.sdk.gateway.android.services.log.LogEvents
import com.paylivre.sdk.gateway.android.ui.error.handleNavigateToErrorScreen

class LoadingTransaction : Fragment() {

    private var _binding: FragmentLoadingTransactionBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentLoadingTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var orderData = OrderDataRequest(
            "", 0, "", "", "", "",
            "", "", "", "", "", "", "",
            "", "", "", "", "", ""
        )

        //Set Log Analytics
        LogEvents.setLogEventAnalytics("Screen_LoadingTransaction")

        mainViewModel.order_data.observe(viewLifecycleOwner) {
            if (it != null) {
                orderData = it
            }
        }


        fun navigateToFinishScreen() {
            if (orderData.operation == Operation.WITHDRAW.code.toString()) {
                Navigation.findNavController(view)
                    .navigate(R.id.navigation_loading_withdraw)
            } else {
                when (orderData.selected_type) {
                    Type.PIX.code.toString() -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.navigation_finish_screen_deposit_pix)
                    }
                    Type.WALLET.code.toString() -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.navigation_finish_screen_deposit_wallet)
                    }
                    Type.BILLET.code.toString() -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.navigation_finish_screen_deposit_billet)
                    }
                    Type.WIRETRANSFER.code.toString() -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.navigation_finish_screen_deposit_wiretransfer)
                    }
                    else -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.navigation_transaction_completion)
                    }
                }
            }

        }



        fun handleNavigationScreen(statusTransactionResponse: StatusTransactionResponse) {
            val isSuccess = statusTransactionResponse.isSuccess
            if (isSuccess == true) {
                navigateToFinishScreen()
            } else {
                handleNavigateToErrorScreen(view, statusTransactionResponse.error)
            }
        }

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {
            val isLoading = it.isLoading

            if (isLoading == false) {
                handleNavigationScreen(it)
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}