package com.example.skripsi.ui.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
import java.util.Collections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatchFragment : Fragment() {

    private inner class WordAdapter(private val words: MutableList<String>) :
        RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

            inner class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
                val wordText: TextView = view.findViewById(R.id.wordText)
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            WordViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false))

        override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
            holder.wordText.text = words[position]
        }

        override fun getItemCount() = words.size

        fun swapItems(from: Int, to: Int) {
            Collections.swap(words, from, to)
            notifyItemMoved(from, to)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val words = mutableListOf("Kumaha", "damang", "?") // Hardcoded untuk sementara -- tolong nanti link ke db
        val recyclerView = view.findViewById<RecyclerView>(R.id.wordRV)
        recyclerView.adapter = WordAdapter(words)

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeMovementFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                (recyclerView as WordAdapter).swapItems(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }).attachToRecyclerView(recyclerView)
    }
}