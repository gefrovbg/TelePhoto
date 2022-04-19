package com.example.telephoto.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.telephoto.domain.models.ChatId


class DataBaseHelperUseCase (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)

    }

    fun addClient(chatId: ChatId ): Boolean{

        return try {
            val values = ContentValues()
            values.put(CHAT_ID_COL, chatId.chatId)
            values.put(FIRST_NAME_COl, chatId.firstName)
            values.put(LAST_NAME_COL, chatId.lastName)
            values.put(NICKNAME_COL, chatId.nickname)
            values.put(ADD_STATUS_COL, if (chatId.addStatus) 1 else 0)
            val db = this.writableDatabase
            db.insert(TABLE_NAME, null, values)
            db.close()
            true
        }catch (e: Exception){
            false
        }

    }

    fun getClientByNickname(nickname: String): ChatId? {

        val query = "SELECT * FROM $TABLE_NAME WHERE $NICKNAME_COL =  \"$nickname\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        var chatId: ChatId? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(1)).toLong()
            val firstName = cursor.getString(2)
            val lastName = cursor.getString(3)
            val addStatus = Integer.parseInt(cursor.getString(5)) == 1
            chatId = ChatId(id, firstName, lastName,nickname, addStatus)
            cursor.close()
        }

        db.close()
        return chatId

    }

    fun deleteClientByNickname(nickname: String): Boolean {

        return try {
            val db = this.writableDatabase
            val whereClause = "$NICKNAME_COL=?"
            val whereArgs = arrayOf(nickname)
            db.delete("$TABLE_NAME", whereClause, whereArgs)
            db.close()
            true
        }catch (e: Exception){
            false
        }

    }
    fun getAll(): ArrayList<ChatId> {

        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        val arrayList = arrayListOf<ChatId>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
            val id = Integer.parseInt(cursor.getString(1)).toLong()
            val firstName = cursor.getString(2)
            val lastName = cursor.getString(3)
            val nickname = cursor.getString(4)
            val addStatus = Integer.parseInt(cursor.getString(5)) == 1
            arrayList.add(ChatId(id, firstName, lastName,nickname, addStatus)) //add the item
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