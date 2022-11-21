package com.example.smart_insurance.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.smart_insurance.model.Insurance

class SqlOpenHelper(
    context: Context

) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION

) {

    companion object {
        const val DATABASE_NAME = "smart_insurance.db"
        const val DATABASE_VERSION = 1

        private const val TABLE_NAME_INSURANCES = "INSURANCES"
        const val SQL_CREATE_ENTRIES_INSURANCES =
            "CREATE TABLE $TABLE_NAME_INSURANCES (" +
                    "ID INTEGER PRIMARY KEY," +
                    "NAME TEXT," +
                    "INIT_DATE TEXT," +
                    "END_DATE TEXT," +
                    "PRICE TEXT," +
                    "DESCRIPTION TEXT," +
                    "CATEGORY INTEGER," +
                    "CATEGORY_IMAGE TEXT," +
                    "CATEGORY_COLOR TEXT," +
                    "IMAGES TEXT," +
                    "STATE TEXT)"

        const val SQL_DELETE_ENTRIES_INSURANCES = "DROP TABLE IF EXISTS $TABLE_NAME_INSURANCES"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES_INSURANCES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES_INSURANCES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun queryToTable(query: String) {
        val db = writableDatabase
        db.execSQL(query)
    }

    fun insert(insurance: Insurance) {

        val db = writableDatabase
        val values = ContentValues().apply {
            put("ID", insurance.id)
            put("NAME", insurance.name)
            put("INIT_DATE", insurance.initDate)
            put("END_DATE", insurance.endDate)
            put("PRICE", insurance.price)
            put("DESCRIPTION", insurance.description)
            put("CATEGORY", insurance.category)
            put("CATEGORY_IMAGE", insurance.category_image)
            put("CATEGORY_COLOR", insurance.category_color)
            put("IMAGES", insurance.images.split(",")[0])
            put("STATE", insurance.state)
        }
        db.insert(TABLE_NAME_INSURANCES, null, values)
        db.close()
    }

    fun getAllInsurances(): ArrayList<Insurance> {
        val insurances = ArrayList<Insurance>()
        val db = readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_INSURANCES", null)
        } catch (e: Exception) {
            return insurances
        }

        if (cursor.moveToFirst()) {
            do{
                val insurance = Insurance(
                    cursor.getString(0).toInt(),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6).toInt(),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10)
                )

                insurances.add(insurance)
            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return insurances
    }

    fun <T> delete(data: T) {
        val db = writableDatabase

        when (data) {
            is Insurance -> {
                db.delete(TABLE_NAME_INSURANCES, "ID = ?", arrayOf(data.id.toString()))
            }
        }

        db.close()

    }

    fun <T> update(data: T) {
        val db = writableDatabase

        when (data) {
            is Insurance -> {
                val values = ContentValues().apply {
                    put("ID", data.id)
                    put("NAME", data.name)
                    put("INIT_DATE", data.initDate)
                    put("END_DATE", data.endDate)
                    put("PRICE", data.price)
                    put("DESCRIPTION", data.description)
                    put("CATEGORY", data.category)
                    put("STATE", data.state)
                }

                db.update(TABLE_NAME_INSURANCES, values, "ID = ?", arrayOf(data.id.toString()))
            }
        }

        db.close()

    }
}

