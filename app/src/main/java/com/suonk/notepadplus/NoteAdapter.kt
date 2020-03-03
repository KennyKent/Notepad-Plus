package com.suonk.notepadplus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suonk.notepadplus.R

class NoteAdapter(
    private val listOfNotes: ArrayList<Note>,
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapterViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)

        return NoteAdapterViewHolder(viewItem)
    }

    override fun getItemCount(): Int {
        return listOfNotes.size
    }

    override fun onBindViewHolder(holder: NoteAdapterViewHolder, position: Int) {
        val note = listOfNotes[position]
        holder.noteAdapterRelativeLayout!!.tag = position
        holder.noteAdapterTitle!!.text = note.title
        holder.noteAdapterExcerpt!!.text = note.text

        holder.noteAdapterRelativeLayout!!.setOnClickListener(onClickListener)
    }

    class NoteAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteAdapterRelativeLayout: RelativeLayout? = null
        var noteAdapterTitle: TextView? = null
        var noteAdapterExcerpt: TextView? = null

        init {
            noteAdapterRelativeLayout = itemView.findViewById(R.id.item_note_layout)
            noteAdapterTitle = itemView.findViewById(R.id.item_note_title)
            noteAdapterExcerpt = itemView.findViewById(R.id.item_note_excerpt)
        }
    }
}