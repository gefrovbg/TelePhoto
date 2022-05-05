package com.example.telephoto.data.storage.client.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.telephoto.data.storage.client.models.ClientSQLite

class SQLiteRepositoryImpl(context: Context, factory: SQLiteDatabase.CursorFactory?): SQLiteRepository, SQLiteOpenHelper(context,
    DATABASE_NAME, factory,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                CHAT_ID_COL + " INTEGER," +
                FIRST_NAME_COl + " TEXT," +
                LAST_NAME_COL + " TEXT," +
                NICKNAME_COL + " TEXT," +
                ADD_STATUS_COL + " INTEGER" +")")
        db.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

    }

    override fun addClient(clientSQLite: ClientSQLite): Boolean{

        return try {
            val values = ContentValues()
            values.put(CHAT_ID_COL, clientSQLite.chatId)
            values.put(FIRST_NAME_COl, clientSQLite.firstName)
            values.put(LAST_NAME_COL, clientSQLite.lastName)
            values.put(NICKNAME_COL, clientSQLite.nickname)
            values.put(ADD_STATUS_COL, if (clientSQLite.addStatus == true) 1 else 0)
            val db = this.writableDatabase
            db.insert(TABLE_NAME, null, values)
            db.close()
            true
        }catch (e: Exception){
            false
        }

    }

    override fun getClientByNickname(clientSQLite: ClientSQLite): ClientSQLite? {

        val query = "SELECT * FROM ${TABLE_NAME} WHERE ${NICKNAME_COL} =  \"${clientSQLite.nickname}\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        return if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(1)).toLong()
            val firstName = cursor.getString(2)
            val lastName = cursor.getString(3)
            val addStatus = Integer.parseInt(cursor.getString(5)) == 1
            val returnClient = ClientSQLite(id, firstName, lastName, clientSQLite.nickname, addStatus)
            cursor.close()
            db.close()
            returnClient
        }else null


    }

    override fun deleteClientByNickname(clientSQLite: ClientSQLite): Boolean {

        return try {
            val db = this.writableDatabase
            val whereClause = "${NICKNAME_COL}=?"
            val whereArgs = arrayOf(clientSQLite.nickname)
            db.delete("${TABLE_NAME}", whereClause, whereArgs)
            db.close()
            true
        }catch (e: Exception){
            false
        }

    }
    override fun getAll(): ArrayList<ClientSQLite> {

        val query = "SELECT * FROM ${TABLE_NAME}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        val arrayList = arrayListOf<ClientSQLite>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
            val id = Integer.parseInt(cursor.getString(1)).toLong()
            val firstName = cursor.getString(2)
            val lastName = cursor.getString(3)
            val nickname = cursor.getString(4)
            val addStatus = Integer.parseInt(cursor.getString(5)) == 1
            arrayList.add(ClientSQLite(id, firstName, lastName,nickname, addStatus)) //add the item
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return arrayList

    }

    companion object{

        private val DATABASE_NAME = "TELEGRAM_BOT"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "client_table"

        val ID_COL = "id"

        val CHAT_ID_COL = "chatId"

        val FIRST_NAME_COl = "firstName"

        val LAST_NAME_COL = "lastName"

        val NICKNAME_COL = "nickname"

        val ADD_STATUS_COL = "addStatus"

    }

}