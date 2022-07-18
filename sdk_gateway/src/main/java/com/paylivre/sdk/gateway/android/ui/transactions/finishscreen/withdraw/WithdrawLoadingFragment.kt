package com.paylivre.sdk.gateway.android.ui.transactions.finishscreen.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.data.model.order.CheckStatusOrderDataRequest
import com.paylivre.sdk.gateway.android.databinding.FragmentLoadingWithdrawBinding
import com.paylivre.sdk.gateway.android.domain.model.Type
import com.paylivre.sdk.gateway.android.services.postdelayed.PostDelayedService
import com.paylivre.sdk.gateway.android.ui.error.getErrorScreen
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val TIMER_INTERVAL_LOADING_WAIT_WITHDRAW: Long = 1000 * 10;

class WithdrawLoadingFragment : Fragment() {
    private var _binding: FragmentLoadingWithdrawBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()
    private val binding get() = _binding!!
    private var language: String? = null
    private var orderId: Int = -1
    private var token: String = ""
    private val postDelayedService: PostDelayedService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentLoadingWithdrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun navigateToFinishScreen(view: View, typeWithdraw: Int?) {
        if (typeWithdraw == Type.PIX.code) {
            Navigation.findNavController(view)
                .navigate(R.id.navigation_finish_screen_withdraw_pix)
        } else {
            Navigation.findNavController(view)
                .navigate(R.id.navigation_finish_screen_withdraw_pix)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.language.observe(viewLifecycleOwner) { language = it }

        mainViewModel.statusResponseTransaction.observe(viewLifecycleOwner) {
            if (it != null && it.isSuccess == true) {
                if (it.data?.order?.id != null) {
                    orderId = it.data?.order?.id
                }
                token = it.data?.token ?: ""
            }
        }

        postDelayedService.postDelayed({
            mainViewModel.checkStatusOrder(
                CheckStatusOrderDataRequest(
                    orderId,
                    token
                )
            )
        }, TIMER_INTERVAL_LOADING_WAIT_WITHDRAW)


        mainViewModel.checkStatusOrderDataResponse.observe(viewLifecycleOwner) {
            when (it.isSuccess) {
                true -> {
                    navigateToFinishScreen(view, it.data?.withdrawal_type_id)
                }
                //Handle error in check status order request
                false -> {
                    Navigation.findNavController(view).navigate(getErrorScreen(it.error))
                }
            }
        }
    }

}