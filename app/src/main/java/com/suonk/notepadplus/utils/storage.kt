//package com.suonk.notepadplus.utils
//
//import android.content.Context
//import android.text.TextUtils
//import android.util.Log
//import java.io.ObjectInputStream
//import java.io.ObjectOutputStream
//import java.util.*
//
//private const val TAG = "storage"
//
//fun persistNote(context: Context, note: Note) {
//
//    if (TextUtils.isEmpty(note.filename)) {
//        note.filename = UUID.randomUUID().toString() + ".note"
//    }
//
//    Log.i(TAG, "Saving note $note")
//    val fileOutput = context.openFileOutput(note.filename, Context.MODE_PRIVATE)
//    val outputStream = ObjectOutputStream(fileOutput)
//    outputStream.writeObject(note)
//    outputStream.close()
//}
//
//fun loadNotes(context: Context): ArrayList<Note> {
//    val listOfNotes = arrayListOf<Note>()
//
//    val notesDir = context.filesDir
//
//    for (fileName in notesDir.list()) {
//        val note = loadNote(context, fileName)
//        Log.i(TAG, "Loaded note $note")
//        listOfNotes.add(note)
//    }
//    return listOfNotes
//}
//
//private fun loadNote(context: Context, filename: String): Note {
//    val fileInput = context.openFileInput(filename)
//    val inputStream = ObjectInputStream(fileInput)
//    val note = inputStream.readObject() as Note
//    inputStream.close()
//
//    return note
//}
//
//fun deleteNote(context: Context, filename: String?) {
//    context.deleteFile(filename)
//}