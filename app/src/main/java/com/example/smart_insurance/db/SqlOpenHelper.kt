package com.example.smart_insurance.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.smart_insurance.model.Category
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

        private const val TABLE_NAME_CATEGORIES = "CATEGORIES"
        const val SQL_CREATE_ENTRIES_CATEGORIES =
            "CREATE TABLE $TABLE_NAME_CATEGORIES (" +
                    "ID INTEGER PRIMARY KEY," +
                    "NAME TEXT," +
                    "IMAGE TEXT," +
                    "COLOR TEXT," +
                    "CATEGORY_NAME TEXT)"

        const val SQL_DELETE_ENTRIES_CATEGORIES = "DROP TABLE IF EXISTS $TABLE_NAME_CATEGORIES"


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
                    "STATE TEXT," +
                    "FOREIGN KEY (CATEGORY) REFERENCES $TABLE_NAME_CATEGORIES(ID))"

        const val SQL_DELETE_ENTRIES_INSURANCES = "DROP TABLE IF EXISTS $TABLE_NAME_INSURANCES"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES_CATEGORIES)
        db.execSQL(SQL_CREATE_ENTRIES_INSURANCES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES_CATEGORIES)
        db.execSQL(SQL_DELETE_ENTRIES_INSURANCES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun queryToTable(query: String) {
        val db = writableDatabase
        db.execSQL(query)
    }

    fun insert(category: Category) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("ID", category.id)
            put("NAME", category.name)
            put("IMAGE", category.image)
            put("COLOR", category.color)
            put("CATEGORY_NAME", category.categoryName)
        }
        db.insert(TABLE_NAME_CATEGORIES, null, values)
        db.close()
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
            put("STATE", insurance.state)
        }
        db.insert(TABLE_NAME_INSURANCES, null, values)
        db.close()
    }


    fun getAllCategories(): ArrayList<Category> {
        val categories = ArrayList<Category>()
        val db = readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_CATEGORIES", null)
        } catch (e: Exception) {
            return categories
        }


        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val image = cursor.getString(2)
                val color = cursor.getString(3)
                val categoryName = cursor.getString(4)
                categories.add(Category(id, name, image, color, categoryName))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return categories
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
            do {
                val id = cursor.getString(0)
                val name = cursor.getString(1)
                val initDate = cursor.getString(2)
                val endDate = cursor.getString(3)
                val price = cursor.getString(4)
                val description = cursor.getString(5)
                val category = cursor.getString(6)
                val state = cursor.getString(7)
                insurances.add(
                    Insurance(
                        id,
                        name,
                        initDate,
                        endDate,
                        price,
                        description,
                        category,
                        state
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return insurances
    }

    fun getCategoryOfInsurances(): ArrayList<String> {
        val images = ArrayList<String>()
        val db = readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(
                "SELECT C.IMAGE, C.COLOR FROM $TABLE_NAME_CATEGORIES AS C, $TABLE_NAME_INSURANCES AS I WHERE C.ID == I.CATEGORY",
                null
            )
        } catch (e: Exception) {
            return images
        }

        if (cursor.moveToFirst()) {
            do {
                val image = cursor.getString(0)
                val color = cursor.getString(1)
                images.add("$image,$color")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return images
    }

    fun <T> delete(data: T) {
        val db = writableDatabase

        when (data) {
            is Category -> {
                db.delete(TABLE_NAME_CATEGORIES, "ID = ?", arrayOf(data.id))
            }
            is Insurance -> {
                db.delete(TABLE_NAME_INSURANCES, "ID = ?", arrayOf(data.id))
            }
        }

        db.close()

    }

    fun <T> update(data: T) {
        val db = writableDatabase

        when (data) {
            is Category -> {
                val values = ContentValues().apply {
                    put("ID", data.id)
                    put("NAME", data.name)
                    put("IMAGE", data.image)
                    put("COLOR", data.color)
                    put("CATEGORY_NAME", data.categoryName)
                }

                db.update(TABLE_NAME_CATEGORIES, values, "ID = ?", arrayOf(data.id))
            }

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

                db.update(TABLE_NAME_INSURANCES, values, "ID = ?", arrayOf(data.id))
            }
        }

        db.close()

    }
}

