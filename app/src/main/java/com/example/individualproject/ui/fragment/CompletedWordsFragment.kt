package com.example.individualproject.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.individualproject.R
import com.example.individualproject.data.model.Words
import com.example.individualproject.databinding.FragmentCompletedWordsBinding
import com.example.individualproject.ui.adapter.WordsAdapter
import com.example.individualproject.ui.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompletedWordsFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    ) {
        HomeViewModel.Factory
    }
    private lateinit var binding: FragmentCompletedWordsBinding
    private lateinit var adapter: WordsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCompletedWordsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        lifecycleScope.launch{
            viewModel.run {
                query.observe(viewLifecycleOwner) {
                    lifecycleScope.launch {
                        viewModel.getAllCompleted().collect {
                            adapter.setWords(it)
                            binding.tvNoCompletedWords.isInvisible = adapter.itemCount != 0
                        }
                    }
                }
                finish.collect {
                    val words = viewModel.sortWord(adapter.getWords())
                    adapter.setWords(words)
                }
            }
        }
    }


    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = WordsAdapter(emptyList())
        adapter.listener = object: WordsAdapter.Listener{
            override fun onClick(words: Words) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToViewWord(words.id!!)
                )
            }
        }
        binding.rvWords.adapter = adapter
        binding.rvWords.layoutManager = layoutManager
    }
}