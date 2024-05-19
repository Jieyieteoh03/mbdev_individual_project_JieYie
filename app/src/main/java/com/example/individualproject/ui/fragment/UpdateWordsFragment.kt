package com.example.individualproject.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentUpdateWordsBinding
import com.example.individualproject.ui.viewModel.UpdateWordsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UpdateWordsFragment : Fragment() {
    private lateinit var binding: FragmentUpdateWordsBinding
    private val viewModel: UpdateWordsViewModel by viewModels {
        UpdateWordsViewModel.Factory
    }
    private val args: UpdateWordsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateWordsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            viewModel.run {
                getWordById(args.id)
                word.observe(viewLifecycleOwner) { setWord() }
                finish.collect{ findNavController().popBackStack() }
            }
        }

        lifecycleScope.launch {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}