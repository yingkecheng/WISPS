package com.example.wisps.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warning_info_table")
data class WarningInfo(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "time", typeAffinity = ColumnInfo.TEXT)
        val time: String
)