package com.example.individualproject.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Query
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentHomeBinding
import com.example.individualproject.databinding.LayoutFilterWordBinding
import com.example.individualproject.ui.adapter.TabAdapter
import com.example.individualproject.ui.fragment.CompletedWordsFragment
import com.example.individualproject.ui.fragment.NewWordFragment
import com.example.individualproject.ui.viewModel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            vpTabs.adapter = TabAdapter(
                this@HomeFragment,
                listOf(NewWordFragment(), CompletedWordsFragment())
            )
            svWord.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(query: String?): Boolean {
                    query?.let {
                        viewModel.run {
                            sortTitle.value = "title"
                            sortOrder.value = "ASC"
                            setQuery(it)
                        }
                    }
                    return true
                }
            })
            ivSort.setOnClickListener{showSort()}
        }


        TabLayoutMediator(binding.tlTabs, binding.vpTabs) { tab, position ->
            when (position) {
                0 -> tab.text = "New Words"
                else -> tab.text = "Completed Words"
            }
        }.attach()
    }

    private fun showSort() {
        val sortDialog = AlertDialog.Builder(requireContext()).create()
        val alertBox = LayoutFilterWordBinding.inflate(layoutInflater)

        alertBox.run {
            viewModel.run {
                sortTitle.value?.let { mrbTitle.isChecked = it == "title" }
                sortOrder.value?.let { mrbAsc.isChecked = it == "ASC" }
                mrbDate.isChecked = !mrbTitle.isChecked
                mrbDesc.isChecked = !mrbAsc.isChecked
                btnConfirm.setOnClickListener {
                    lifecycleScope.launch {
                        sortTitle.value = if(mrbTitle.isChecked) "title" else "dateCreated"
                        sortOrder.value = if (mrbTitle.isChecked) "ASC" else "DESC"
                        finish.emit(Unit)
                    }
                    sortDialog.dismiss()
                }
            }
        }
        sortDialog.setView(alertBox.root)
        sortDialog.show()
    }
}