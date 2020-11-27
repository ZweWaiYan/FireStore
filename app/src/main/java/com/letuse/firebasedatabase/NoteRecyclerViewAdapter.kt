package com.letuse.firebasedatabase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.letuse.firebasedatabase.model.Note

class NoteRecyclerViewAdapter(val notesList: MutableList<Note>, val context: Context, val firestoreDB: FirebaseFirestore) : RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView
        internal var content: TextView
        internal var edit: ImageView
        internal var delete: ImageView

        init {
            title = view.findViewById(R.id.tvTitle)
            content = view.findViewById(R.id.tvContent)

            edit = view.findViewById(R.id.ivEdit)
            delete = view.findViewById(R.id.ivDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_next, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]

        holder!!.title.text = note.title
        holder.content.text = note.content

        holder.edit.setOnClickListener { updateNote(note) }
        holder.delete.setOnClickListener { deleteNote(note.id!!, position) }
    }

    private fun updateNote(note: Note) {
        val intent = Intent(context, noteActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateNoteId", note.id)
        intent.putExtra("UpdateNoteTitle", note.title)
        intent.putExtra("UpdateNoteContent", note.content)
        context.startActivity(intent)
    }

    private fun deleteNote(id: String, position: Int) {
        firestoreDB.collection("notes").document(id).delete().addOnCompleteListener {
                notesList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, notesList.size)
                Toast.makeText(context, "Note has been deleted!", Toast.LENGTH_SHORT).show()
            }
    }
}