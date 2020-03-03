package com.suonk.notepadplus

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.suonk.notepadplus.R

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"
        val REQUEST_EDIT_NOTE = 1

        val ACTION_SAVE_NOTE = "com.suonk.notepadplus.actions.ACTION_SAVE_NOTE"
        val ACTION_DELETE_NOTE = "com.suonk.notepadplus.actions.ACTION_DELETE_NOTE"
    }

    //region ========================================= Val or Var ===========================================

    lateinit var note: Note
    var noteIndex: Int = 0

    private lateinit var noteDetailTitleEditText: AppCompatEditText
    private lateinit var noteDetailLongTextEditText: AppCompatEditText
    lateinit var noteDetailCoordinatorLayout: CoordinatorLayout

    lateinit var noteDetailDate: TextView
    lateinit var noteDetailNbChar: TextView

    var noteDetailTitleText = ""
    var noteDetailTitleTextIsChanged = ""

    var noteDetailLongTextText = ""
    var noteDetailLongTextTextIsChanged = ""

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        //region ========================================= Toolbar ==========================================

        val toolbar = findViewById<Toolbar>(R.id.note_list_detail_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(getColor(R.color.colorWhite))
        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        //endregion

        note = intent.getParcelableExtra(EXTRA_NOTE)!!
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, 0)

        noteDetailTitleEditText = findViewById(R.id.note_detail_title)
        noteDetailLongTextEditText = findViewById(R.id.note_detail_text)
        noteDetailNbChar = findViewById(R.id.note_detail_nb_char)
        noteDetailDate = findViewById(R.id.note_detail_date)
        noteDetailCoordinatorLayout = findViewById(R.id.note_detail_coordinator_layout)

        noteDetailTitleEditText.setText(note.title)
        noteDetailLongTextEditText.setText(note.text)

        noteDetailTitleText = note.title!!
        noteDetailLongTextText = note.text!!
//        noteDetailDate.text = note.date

        var nbChar = noteDetailLongTextEditText.text!!.length.toString()

        if (noteDetailLongTextEditText.text!!.length > 1) {
            noteDetailNbChar.text =
                nbChar + " " + getString(R.string.note_detail_nb_characters)
        } else {
            noteDetailNbChar.text =
                nbChar + " " + getString(R.string.note_detail_nb_character)
        }

//        noteDetailTitleEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//                noteDetailTitleTextIsChanged = noteDetailLongTextEditText.text.toString()
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                nbChar = noteDetailLongTextEditText.text!!.length.toString()
//
//                if (noteDetailLongTextEditText.text!!.length > 1) {
//                    noteDetailNbChar.text =
//                        nbChar + " " + getString(R.string.note_detail_nb_characters)
//                } else {
//                    noteDetailNbChar.text =
//                        nbChar + " " + getString(R.string.note_detail_nb_character)
//                }
//            }
//        })

        noteDetailLongTextEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                noteDetailLongTextTextIsChanged = noteDetailLongTextEditText.text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                nbChar = noteDetailLongTextEditText.text!!.length.toString()

                if (noteDetailLongTextEditText.text!!.length > 1) {
                    noteDetailNbChar.text =
                        nbChar + " " + getString(R.string.note_detail_nb_characters)
                } else {
                    noteDetailNbChar.text =
                        nbChar + " " + getString(R.string.note_detail_nb_character)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (noteDetailLongTextEditText.text.toString() != noteDetailLongTextText || noteDetailTitleEditText.text.toString() != noteDetailTitleText) {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.note_detail_material_builder_back_on_pressed_title))
                        .setMessage(getString(R.string.note_detail_material_builder_back_on_pressed_message))
                        .setPositiveButton(getString(R.string.note_detail_material_builder_back_on_pressed_positive_button)) { _, _ ->
                            onBackPressed()
                        }
                        .setNegativeButton(R.string.note_detail_material_builder_back_on_pressed_negative_button) { dialog, _ ->
                            dialog.cancel()
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    onBackPressed()
                }
                true
            }
            R.id.note_detail_action_undo -> {
                noteDetailLongTextEditText
                true
            }
            R.id.note_detail_action_redo -> {
                noteDetailLongTextEditText
                true
            }
            R.id.note_detail_action_save -> {
                saveNote()
                true
            }
            R.id.note_detail_action_delete -> {
                saveConfirmDeleteNoteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveConfirmDeleteNoteDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.note_detail_material_builder_delete_note_title))
            .setMessage(getString(R.string.note_detail_material_builder_delete_note_message) + " \"${noteDetailTitleEditText.text}\" ?")
            .setPositiveButton(getString(R.string.note_detail_material_builder_delete_note_positive_button)) { _, _ ->
                deleteNote()
            }
            .setNegativeButton(R.string.note_detail_material_builder_delete_note_negative_button) { dialog, _ ->
                dialog.cancel()
                dialog.dismiss()
            }
            .show()
    }

    private fun saveNote() {
        if (noteDetailTitleEditText.text!!.isEmpty()) {
            Snackbar.make(
                noteDetailCoordinatorLayout, getString(R.string.note_detail_is_empty),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            note.title = noteDetailTitleEditText.text.toString()
            note.text = noteDetailLongTextEditText.text.toString()

//        val date = LocalDate.now()
//        val time = LocalTime.now()

//        note.date = "$date $time"

            intent = Intent(ACTION_SAVE_NOTE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun deleteNote() {
        intent = Intent(ACTION_DELETE_NOTE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
