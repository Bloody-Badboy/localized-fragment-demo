package dev.arpan.localized.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.arpan.localized.fragment.databinding.FragmentFifthBinding
import java.util.*


class FifthFragment : BaseFragment<FragmentFifthBinding>(R.layout.fragment_fifth) {

    override fun applyOverrideLocal(): Locale? {
        return Locale.CHINESE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tv2.text = getString(R.string.info, requireContext() is Activity)
        binding.tv3.text = getString(R.string.hello_fifth_fragment)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.to_first)
        }

        binding.buttonToast.setOnClickListener {
            Toast.makeText(
                requireContext(),
                R.string.hello_fifth_fragment,
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.buttonDialog.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.hello_fifth_fragment)
                .setPositiveButton(android.R.string.ok) { _, _ ->

                }.show()
        }
    }
}