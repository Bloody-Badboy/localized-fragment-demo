package dev.arpan.localized.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import dev.arpan.localized.fragment.databinding.FragmentSecondBinding
import java.util.*


class SecondFragment : BaseFragment<FragmentSecondBinding>(R.layout.fragment_second) {

    override fun applyOverrideTheme(): Int {
        return R.style.Theme_LocalizedFragment_Green
    }

    override fun applyOverrideLocal(): Locale {
        return Locale("ar")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tv2.text = getString(R.string.info, requireContext() is Activity)
        binding.tv3.text = getString(R.string.hello_second_fragment)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.to_third)
        }
    }
}