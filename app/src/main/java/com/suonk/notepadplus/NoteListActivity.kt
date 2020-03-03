package com.suonk.notepadplus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.suonk.notepadplus.utils.deleteNote
import com.suonk.notepadplus.utils.loadNotes
import com.suonk.notepadplus.utils.persistNote
import com.suonk.notepadplus.R

class NoteListActivity : AppCompatActivity(), View.OnClickListener {

    //region ========================================= Val or Var ===========================================

    var listOfNotes = arrayListOf<Note>()
    var noteListAdapter: NoteAdapter? = null
    lateinit var coordinatorLayout: CoordinatorLayout

    lateinit var noteListNavView: NavigationView
    lateinit var drawerLayout: DrawerLayout

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        //region ========================================= Toolbar ==========================================

        val toolbar = findViewById<Toolbar>(R.id.note_list_toolbar)
        toolbar.setTitleTextColor(getColor(R.color.colorWhite))
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        actionbar!!.title = ""

        val noteListToolbarOpenDrawer =
            findViewById<AppCompatImageView>(R.id.note_list_toolbar_open_drawer)

        //endregion

        //region ====================================== DrawerLayout ========================================

        drawerLayout = findViewById(R.id.note_list_drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.note_list_nav_view)
        val menu = navigationView.menu
        val navItem = menu.findItem(R.id.nav_home)
        navItem.isChecked = true
        navigationView!!.menu.getItem(0).isChecked = true

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_home -> startActivity(
                    Intent(
                        this@NoteListActivity,
                        NoteListActivity::class.java
                    )
                )
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        //endregion

        //region ====================================== FindViewById ========================================

        coordinatorLayout = findViewById(R.id.note_list_coordinator_layout)
        noteListNavView = findViewById(R.id.note_list_nav_view)
        val noteListRecyclerView = findViewById<RecyclerView>(R.id.note_list_recyclerview)
        val floatingButtonCreate =
            findViewById<FloatingActionButton>(R.id.note_list_floating_create)

        //endregion

        //region ======================================= ListOfNotes ========================================

        listOfNotes = loadNotes(this)
        noteListAdapter = NoteAdapter(listOfNotes, this)
        noteListRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        noteListRecyclerView.adapter = noteListAdapter

        //endregion

        //region ======================================= ListOfNotes ========================================

        floatingButtonCreate.setOnClickListener(this)

        noteListToolbarOpenDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        //endregion
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_note_list_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.note_list_toolbar_sort_by_date -> {
                return true
            }
            R.id.note_list_toolbar_sort_by_alphabetical -> {
                return true
            }
            R.id.note_list_toolbar_sort_by_favorite -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(it: View) {
        if (it.tag != null) {
            showNoteDetail(it.tag as Int)
        } else {
            when (it.id) {
                R.id.note_list_floating_create -> showNoteDetail(-1)
            }
        }
    }

    private fun showNoteDetail(noteIndex: Int) {

        val note: Note = if (noteIndex < 0) Note() else listOfNotes[noteIndex]

        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE, note as Parcelable)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, noteIndex)
        startActivityForResult(intent, NoteDetailActivity.REQUEST_EDIT_NOTE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        when (requestCode) {
            NoteDetailActivity.REQUEST_EDIT_NOTE -> processEditNoteResult(data)
        }
    }

    private fun processEditNoteResult(data: Intent) {
        val noteIndex = data.getIntExtra(NoteDetailActivity.EXTRA_NOTE_INDEX, -1)

        when (data.action) {
            NoteDetailActivity.ACTION_SAVE_NOTE -> {
                val note = data.getParcelableExtra<Note>(NoteDetailActivity.EXTRA_NOTE)
                saveNote(note!!, noteIndex)
            }
            NoteDetailActivity.ACTION_DELETE_NOTE -> {
                deleteNote(noteIndex)
            }
        }
    }

    private fun deleteNote(noteIndex: Int) {
        if (noteIndex < 0) {
            return
        }
        listOfNotes.removeAt(noteIndex)
        deleteNote(this, listOfNotes[noteIndex].filename)
        noteListAdapter!!.notifyDataSetChanged()
        val title = listOfNotes[noteIndex].title

        Snackbar.make(
            coordinatorLayout,
            title + " " + getString(R.string.note_title_deleted),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun saveNote(note: Note, noteIndex: Int) {
        persistNote(this, note)
        if (noteIndex < 0) {
            listOfNotes.add(noteIndex + 1, note)
        } else {
            listOfNotes[noteIndex] = note
        }
        noteListAdapter!!.notifyDataSetChanged()
    }
}