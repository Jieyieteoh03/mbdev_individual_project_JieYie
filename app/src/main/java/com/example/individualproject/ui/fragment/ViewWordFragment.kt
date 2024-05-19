package com.example.individualproject.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentViewWordBinding
import com.example.individualproject.databinding.LayoutAlertBinding
import com.example.individualproject.ui.viewModel.ViewWordViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ViewWordFragment : Fragment() {
    private lateinit var binding: FragmentViewWordBinding
    private val viewModel: ViewWordViewModel by viewModels {
        ViewWordViewModel.Factory
    }
    private val args: ViewWordFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewWordBinding.inflate(
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
        viewModel.run {
            getWordById(args.id)
            word.observe(viewLifecycleOwner){
                setWords()
                binding.btnDone.isInvisible = it.isCompleted == true
            }
        }
        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack(
                    findNavController().graph.startDestinationId, false
                )
            }
        }

        lifecycleScope.launch {
            viewModel.toastMessage.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.run {
            btnDone.setOnClickListener {
                showAlert("Do you want this word to move to complete list")

            }
            btnDelete.setOnClickListener {
                showAlert("You want to delete this word")
            }
            btnUpdate.setOnClickListener {
                findNavController().navigate(
                    ViewWordFragmentDirections.actionViewWordToUpdateWords(args.id)
                )
            }
        }
    }

    private fun showAlert(type: String) {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        val alertBox = LayoutAlertBinding.inflate(layoutInflater)

        alertBox.run {
            tvQues.text = getString(R.string.ques_desc, type)
            btnNo.setOnClickListener { alertDialog.dismiss() }
            btnYes.setOnClickListener {
                if(type == "You want to delete this word") viewModel.deleteWords()
                else if(type == "Do you want this word to move to complete list") viewModel.completeWords()
                alertDialog.dismiss()
            }
        }
        alertDialog.setView(alertBox.root)
        alertDialog.show()
    }

}