package com.paylivre.sdk.gateway.android.ui.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.paylivre.sdk.gateway.android.R
import com.paylivre.sdk.gateway.android.databinding.FragmentButtonsTypeSelectBinding
import android.widget.RelativeLayout
import androidx.fragment.app.activityViewModels
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Type
import com.paylivre.sdk.gateway.android.domain.model.checkTypeEnable
import com.paylivre.sdk.gateway.android.ui.buttons.ButtonTypeFragment
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel


class ButtonsTypeSelectFragment : Fragment() {
    private var _binding: FragmentButtonsTypeSelectBinding? = null
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

        _binding = FragmentButtonsTypeSelectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Set Title select option Type
        mainViewModel.operation.observe(viewLifecycleOwner, {

            operation = it
            val typeDescription =
                if (operation == Operation.DEPOSIT.code) getString(R.string.deposit) else getString(
                    R.string.withdraw
                )

            binding.textViewTitleTypes.text =
                "${getString(R.string.title_types_operation)} $typeDescription"

        })


        //Set Options Button select option Type
        mainViewModel.type.observe(viewLifecycleOwner, {
            var listTypesEnabled: MutableList<Int> = mutableListOf()

            if (checkTypeEnable(it, Type.PIX.code)) {
                setButtonType(
                    ButtonTypeFragment(),
                    Type.PIX.code,
                    R.id.buttonType1,
                    binding.containerType1,
                    Operation.DEPOSIT.code
                )
                listTypesEnabled.add(Type.PIX.code)
            }

            if (checkTypeEnable(it, Type.BILLET.code)) {
                setButtonType(
                    ButtonTypeFragment(),
                    Type.BILLET.code,
                    R.id.buttonType2,
                    binding.containerType2,
                    Operation.DEPOSIT.code
                )
                listTypesEnabled.add(Type.BILLET.code)
            }

            if (checkTypeEnable(it, Type.WIRETRANSFER.code)) {
                setButtonType(
                    ButtonTypeFragment(),
                    Type.WIRETRANSFER.code,
                    R.id.buttonType3,
                    binding.containerType3,
                    Operation.DEPOSIT.code
                )
                listTypesEnabled.add(Type.WIRETRANSFER.code)
            }

            if (checkTypeEnable(it, Type.WALLET.code)) {
                setButtonType(
                    ButtonTypeFragment(),
                    Type.WALLET.code,
                    R.id.buttonType4,
                    binding.containerType4,
                    Operation.DEPOSIT.code
                )
                listTypesEnabled.add(Type.WALLET.code)
            }

            if (listTypesEnabled.size == 1) {
                mainViewModel.setButtonTypeSelected(listTypesEnabled[0])
            }


        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}