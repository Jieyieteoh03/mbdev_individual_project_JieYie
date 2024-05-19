package com.example.individualproject.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.individualproject.data.model.Words
import com.example.individualproject.databinding.LayoutCardItemBinding

class WordsAdapter(
    private var words: List<Words>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val binding = LayoutCardItemBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
        return CardItemViewHolder(binding)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val word = words[position]
        if (holder is CardItemViewHolder) {
            return holder.bind(word)
        }
    }

    fun getWords() = words

    fun setWords(words: List<Words>) {
        this.words = words
        notifyDataSetChanged()
    }

    inner class CardItemViewHolder(
        private val binding: LayoutCardItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(words: Words) {
            binding.tvTitle.text = words.title
            binding.tvDef.text = words.meaning
            binding.cvWords.setOnClickListener { listener?.onClick(words) }
        }
    }

    interface Listener {
        fun onClick(words: Words)
    }


}