package com.paylivre.sdk.gateway.android.ui.form

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.data.model.pixApprovalTime.getDataAveragePixApprovalTime
import com.paylivre.sdk.gateway.android.databinding.PixApprovalTimeBinding
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Type
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.utils.dpToPx
import com.paylivre.sdk.gateway.android.utils.getStringByKey
import com.paylivre.sdk.gateway.android.utils.getStringIdByKey
import java.util.*


class PixApprovalTimeFragment : Fragment() {
    private var _binding: PixApprovalTimeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PixApprovalTimeBinding.inflate(inflater, container, false)

        var pixTypeIsSelected = false
        var pixApprovalTimeIsSuccess = false
        var pixApprovalTimeIsLoading = false
        var operation = -1

        val textAveragePixApprovalTime = binding.textPixApprovalTime
        val statusPixApprovalTime = binding.statusPixApprovalTime

        mainViewModel.operation.observe(viewLifecycleOwner, {
            operation = it
        })

//        mainViewModel.pixApprovalTimeIsLoading.observe(viewLifecycleOwner, {
//            pixApprovalTimeIsLoading = it
//            if (it && pixTypeIsSelected) {
//                binding.loadingBar.visibility = View.VISIBLE
//            } else {
//                binding.loadingBar.visibility = View.GONE
//            }
//        })


        mainViewModel.pixApprovalTime.observe(viewLifecycleOwner, {
            val isSuccess = it.status == "success" && it.data != null

            pixApprovalTimeIsSuccess = isSuccess



            if (it.data != null) {
                if (pixTypeIsSelected && isSuccess && operation == Operation.DEPOSIT.code) {
                    binding.containerPixApprovalTime.visibility = View.VISIBLE
                }

                val data = getDataAveragePixApprovalTime(it.data)
                val statusText = "  ${getStringByKey(context, data.level_key)}  "
                val colorStatus = data.flag_color_id
                val stringIdAveragePix = getStringIdByKey(context, data.average_time_key)

                val textAverageTime = resources.getString(
                    stringIdAveragePix,
                    data.average_unit_time_1,
                    data.average_unit_time_2
                )

                statusPixApprovalTime.text = statusText.uppercase(Locale.getDefault())

                val border = GradientDrawable()
                border.setColor(ContextCompat.getColor(requireActivity(), colorStatus))
                border.cornerRadius = dpToPx(resources, 5F)

                statusPixApprovalTime.background = border

                textAveragePixApprovalTime.text = textAverageTime
            }
        })

        mainViewModel.buttonTypeSelected.observe(viewLifecycleOwner, {
            if (it == Type.PIX.code) {
                pixTypeIsSelected = true
                if (pixApprovalTimeIsSuccess && operation == Operation.DEPOSIT.code) {
                    binding.containerPixApprovalTime.visibility = View.VISIBLE
                }
//                if (pixApprovalTimeIsLoading) {
//                    binding.loadingBar.visibility = View.VISIBLE
//                }
            } else {
                pixTypeIsSelected = false
                binding.containerPixApprovalTime.visibility = View.GONE
            }
        })




        return binding.root
    }

}