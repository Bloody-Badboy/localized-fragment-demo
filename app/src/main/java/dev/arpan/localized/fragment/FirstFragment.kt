package dev.arpan.localized.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dev.arpan.localized.fragment.databinding.FragmentFirstBinding

class FirstFragment : BaseFragment<FragmentFirstBinding>(R.layout.fragment_first) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tv2.text = getString(R.string.info, requireContext() is Activity)
        binding.tv3.text = getString(R.string.hello_first_fragment)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.to_second)
        }
    }
}