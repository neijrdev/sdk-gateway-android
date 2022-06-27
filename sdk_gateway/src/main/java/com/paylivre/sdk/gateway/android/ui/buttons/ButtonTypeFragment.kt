package com.paylivre.sdk.gateway.android.ui.buttons

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
import com.paylivre.sdk.gateway.android.databinding.FragmentButtonTypeBinding
import android.graphics.drawable.GradientDrawable
import com.paylivre.sdk.gateway.android.domain.model.Type
import com.paylivre.sdk.gateway.android.ui.viewmodel.MainViewModel
import com.paylivre.sdk.gateway.android.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.roundToInt


class ButtonTypeFragment : Fragment() {

    private var _binding: FragmentButtonTypeBinding? = null
    val mainViewModel: MainViewModel by sharedViewModel()

    private var type: Int = 0

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentButtonTypeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonType = binding.buttonType
        val textViewOperation: TextView = binding.textViewType
        val logoImageView: ImageView = binding.logoButton

        buttonType.isClickable = true


        data class ResponseGetLayoutParams(
            val widthImg: Int,
            val heightImg: Int,
            val srcImg: Int,
            val typeName: String,
        )

        fun dpToPxLocalInt(value: Float): Int {
            return dpToPx(resources, value).roundToInt()
        }


        fun getLayoutParamsType(type: Int): ResponseGetLayoutParams {
            return when (type) {
                Type.PIX.code -> {
                    ResponseGetLayoutParams(
                        dpToPxLocalInt(62f),
                        dpToPxLocalInt(22f),
                        R.drawable.ic_pix,
                        ""
                    )
                }
                Type.BILLET.code -> {
                    ResponseGetLayoutParams(
                        dpToPxLocalInt(19F),
                        dpToPxLocalInt(12f),
                        R.drawable.ic_billet,
                        getString(R.string.type_billet)
                    )
                }
                Type.WALLET.code -> {
                    ResponseGetLayoutParams(
                        dpToPxLocalInt(14f),
                        dpToPxLocalInt(18f),
                        R.drawable.ic_wallet_type,
                        getString(R.string.type_wallet)
                    )
                }

                Type.WIRETRANSFER.code -> {
                    ResponseGetLayoutParams(
                        dpToPxLocalInt(25f),
                        dpToPxLocalInt(18f),
                        R.drawable.ic_wiretransfer,
                        getString(R.string.type_wiretransfer)
                    )
                }

                else -> ResponseGetLayoutParams(
                    dpToPxLocalInt(40f),
                    dpToPxLocalInt(15f),
                    R.drawable.ic_pix,
                    ""
                )
            }
        }


        fun setLayoutParams(type: Int) {
            val customLayoutParams = getLayoutParamsType(type)
            val params = logoImageView.layoutParams as ViewGroup.MarginLayoutParams
            params.width = customLayoutParams.widthImg
            params.height = customLayoutParams.heightImg
            logoImageView.layoutParams = params
            logoImageView.setImageResource(customLayoutParams.srcImg)
            textViewOperation.text = customLayoutParams.typeName
        }

        val widthDeviceDP = resources.displayMetrics.widthPixels
        val marginsHorizontalDP = dpToPxLocalInt(80f)
        val widthDeviceDPMinusMargin = widthDeviceDP - marginsHorizontalDP
        val widthButton = widthDeviceDPMinusMargin * 0.5 - dpToPxLocalInt(10f)


        fun setActivateBorderButton(color: Int) {
            val border = GradientDrawable()
            border.setColor(Color.TRANSPARENT)
            border.setStroke(dpToPx(resources, 1F).roundToInt(), color)
            border.cornerRadius = dpToPx(resources, 5F)

            buttonType.background = border

            //Parametros Botão
            val params = buttonType.layoutParams
            params.width = widthButton.toInt()
            buttonType.layoutParams = params
        }


        fun setActivateButton(isActive: Boolean) {
            if (isActive) {
                val colorActive = ContextCompat.getColor(requireContext(), R.color.primary_color_paylivre_sdk)
                logoImageView.setColorFilter(colorActive)
                textViewOperation.setTextColor(colorActive)
                setActivateBorderButton(colorActive)
            } else {
                val colorNotActive = ContextCompat.getColor(requireContext(), R.color.grey_color_paylivre_sdk)
                logoImageView.setColorFilter(colorNotActive)
                textViewOperation.setTextColor(colorNotActive)
                setActivateBorderButton(colorNotActive)
            }

        }

        //Default Value is
        setActivateButton(false)

        //Recebe os parametros de quem instanciou o ButtonType
        val bundle = this.arguments
        if (bundle != null) {
            type = bundle.getInt("type", Type.PIX.code)
            setLayoutParams(type)
        }

        mainViewModel.buttonTypeSelected.observe(viewLifecycleOwner) { buttonTypeSelected ->
            setActivateButton(buttonTypeSelected == type)
        }


        binding.buttonType.setOnClickListener {
            mainViewModel.setButtonTypeSelected(type)
            mainViewModel.setIsCloseKeyboard(true)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}