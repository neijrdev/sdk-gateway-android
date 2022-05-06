package com.paylivre.sdk.gateway.android.ui.form

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentButtonsTypePixKeySelectBinding
import android.graphics.drawable.GradientDrawable
import android.widget.RelativeLayout
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Type
import com.paylivre.sdk.gateway.android.domain.model.TypePixKey
import com.paylivre.sdk.gateway.android.domain.model.checkTypeEnable
import com.paylivre.sdk.gateway.android.ui.buttons.ButtonTypeFragment
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.utils.dpToPx
import kotlin.math.roundToInt


class ButtonsTypePixKeySelect : Fragment() {
    private var _binding: FragmentButtonsTypePixKeySelectBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private var type: Int = 0
    private var operation: Int = -1

    private val binding get() = _binding!!

    private fun setButtonType(
        viewFragment: Fragment,
        type: Int,
        containerResourceId: Int,
        container: RelativeLayout,
        operation: Int,
    ) {
        val bundle = Bundle()
        bundle.putInt("type", type)
        bundle.putInt("operation", operation)
        viewFragment.arguments = bundle

        childFragmentManager.beginTransaction().apply {
            replace(containerResourceId, viewFragment)
            commit()
        }

        container.visibility = View.VISIBLE
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentButtonsTypePixKeySelectBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var pixKeySelectCode: Int? = null

//        mainViewModel.buttonTypeSelected.observe(viewLifecycleOwner, {
//            isTypeSelected = it
//            errorTypeSelect.visibility = View.INVISIBLE
//            if (it != Type.WALLET.code) {
//                errorEmailWallet.visibility = View.GONE
//                errorPassword.visibility = View.GONE
//            }
//        })


        //Set Title select option Pix Key Type
        mainViewModel.operation.observe(viewLifecycleOwner, {
            operation = it
            if (it == Operation.WITHDRAW.code) {
                binding.textViewTitleTypes.text = getString(R.string.choose_pix_key_type)
            }
        })

        mainViewModel.buttonPixKeyTypeSelected.observe(viewLifecycleOwner, {
            pixKeySelectCode = it
        })


        //Set Options Button select option Type
        mainViewModel.type.observe(viewLifecycleOwner, {
            if (operation == Operation.WITHDRAW.code) {
                if (pixKeySelectCode == -1) {
                    mainViewModel.setButtonPixKeyTypeSelected(TypePixKey.DOCUMENT.code)
                }

                setButtonType(
                    ButtonTypeFragment(),
                    TypePixKey.DOCUMENT.code,
                    R.id.buttonType1,
                    binding.containerType1,
                    Operation.WITHDRAW.code
                )

                setButtonType(
                    ButtonTypeFragment(),
                    TypePixKey.EMAIL.code,
                    R.id.buttonType2,
                    binding.containerType2,
                    Operation.WITHDRAW.code
                )

                setButtonType(
                    ButtonTypeFragment(),
                    TypePixKey.PHONE.code,
                    R.id.buttonType3,
                    binding.containerType3,
                    Operation.WITHDRAW.code
                )
            }
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}