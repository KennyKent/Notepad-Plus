package com.suonk.notepadplus

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {

    @Query("SELECT * FROM note")
    fun getAllNotes() : LiveData<List<Note>>


    /**
     * val note dao.getNoteById(3).observe(this, Observer { user -> ...})
     * */
    @Query("SELECT * FROM note WHERE id = :userId")
    fun getNoteById(noteId: Int) : LiveData<Note>

    /**
     * Usage :
     * dao.InsertNote(note)
     * */
    @Insert
    fun insertNote(note: Note)

    /**
     * Usage :
     * val id = dao.InsertNoteReturnId(note)
     * */
    @Insert
    fun insertNoteReturnId(note: Note): Long

    /**
     * Usage :
     * val ids = dao.insertNotes(notes)
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: ArrayList<Note>): ArrayList<Long>

    /**
     * Usage :
     * val ids = dao.insertNotes(note1, note2 etc......)
     * */
    @Insert
    fun insertNotes(vararg notes: Note): Array<Long>

    /**
     * Usage :
     * dao.updateNote
     * */
    @Update
    fun updateNote(note: Note): Int

    /**
     * Usage :
     * val rows = dao.updateNotes(note1, note2 etc......)
     * */
    @Update
    fun updateNotes(notes: ArrayList<Note>): Int

    /**
     * Usage :
     * dao.updateNote
     * */
    @Delete
    fun deleteNote(note: Note): Int
}