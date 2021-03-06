package dev.arpan.localized.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            findNavController().navigate(R.id.to_fifth)
        }

        binding.buttonToast.setOnClickListener {
            Toast.makeText(
                requireContext(),
                R.string.hello_fourth_fragment,
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.buttonDialog.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.hello_fourth_fragment)
                .setPositiveButton(android.R.string.ok) { _, _ ->

                }.show()
        }
    }
}