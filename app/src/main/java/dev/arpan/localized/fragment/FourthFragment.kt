package dev.arpan.localized.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dev.arpan.localized.fragment.databinding.FragmentFourthBinding
import java.util.*


class FourthFragment : BaseFragment<FragmentFourthBinding>(R.layout.fragment_fourth) {

    override fun applyOverrideTheme(): Int {
        return R.style.Theme_LocalizedFragment_Yellow
    }

    override fun applyOverrideLocal(): Locale? {
        return Locale.CHINESE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tv2.text = getString(R.string.info, requireContext() is Activity)
        binding.tv3.text = getString(R.string.hello_fourth_fragment)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.to_first)
        }
    }
}