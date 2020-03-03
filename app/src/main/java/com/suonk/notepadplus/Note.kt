package com.suonk.notepadplus

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notes",
    indices = [
    Index(value = ["title", "content"], unique = true)])
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,
    var content: String,
    var date: String
) {
}