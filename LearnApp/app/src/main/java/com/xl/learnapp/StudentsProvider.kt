package com.xl.learnapp

import java.util.HashMap
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

import android.net.Uri
import android.text.TextUtils

class StudentsProvider : ContentProvider() {


    /**
     * 数据库特定常量声明
     */
    private var db: SQLiteDatabase? = null

    /**
     * 创建和管理提供者内部数据源的帮助类.
     */
    private class DatabaseHelper internal constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $STUDENTS_TABLE_NAME")
            onCreate(db)
        }
    }

    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)

        /**
         * 如果不存在，则创建一个可写的数据库。
         */
        db = dbHelper.writableDatabase
        return if (db == null) false else true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        /**
         * 添加新学生记录
         */
        val rowID = db!!.insert(STUDENTS_TABLE_NAME, "", values)

        /**
         * 如果记录添加成功
         */

        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        throw SQLException("Failed to add a record into $uri")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var sortOrder = sortOrder
        val qb = SQLiteQueryBuilder()
        qb.tables = STUDENTS_TABLE_NAME

        when (uriMatcher.match(uri)) {
            STUDENTS -> qb.setProjectionMap(STUDENTS_PROJECTION_MAP)

            STUDENT_ID -> qb.appendWhere(_ID + "=" + uri.pathSegments[1])

            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        if (sortOrder == null || sortOrder === "") {
            /**
             * 默认按照学生姓名排序
             */
            sortOrder = NAME
        }
        val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        /**
         * 注册内容URI变化的监听器
         */
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0

        when (uriMatcher.match(uri)) {
            STUDENTS -> count = db!!.delete(STUDENTS_TABLE_NAME, selection, selectionArgs)

            STUDENT_ID -> {
                val id = uri.pathSegments[1]
                count = db!!.delete(STUDENTS_TABLE_NAME, _ID + " = " + id +
                        if (!TextUtils.isEmpty(selection)) " AND (" + selection + ')'.toString() else "", selectionArgs)
            }

            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0

        when (uriMatcher.match(uri)) {
            STUDENTS -> count = db!!.update(STUDENTS_TABLE_NAME, values, selection, selectionArgs)

            STUDENT_ID -> count = db!!.update(STUDENTS_TABLE_NAME, values, _ID + " = " + uri.pathSegments[1] +
                    if (!TextUtils.isEmpty(selection)) " AND (" + selection + ')'.toString() else "", selectionArgs)

            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
        /**
         * 获取所有学生记录
         */
            STUDENTS -> return "vnd.android.cursor.dir/vnd.example.students"

        /**
         * 获取一个特定的学生
         */
            STUDENT_ID -> return "vnd.android.cursor.item/vnd.example.students"

            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    companion object {

        internal val PROVIDER_NAME = "com.example.provider.College"
        internal val URL = "content://$PROVIDER_NAME/students"
        internal val CONTENT_URI = Uri.parse(URL)

        internal val _ID = "_id"
        internal val NAME = "name"
        internal val GRADE = "grade"

        private val STUDENTS_PROJECTION_MAP: HashMap<String, String>? = null

        internal val STUDENTS = 1
        internal val STUDENT_ID = 2

        internal val uriMatcher: UriMatcher

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(PROVIDER_NAME, "students", STUDENTS)
            uriMatcher.addURI(PROVIDER_NAME, "students/#", STUDENT_ID)
        }

        internal val DATABASE_NAME = "College"
        internal val STUDENTS_TABLE_NAME = "students"
        internal val DATABASE_VERSION = 1
        internal val CREATE_DB_TABLE = " CREATE TABLE " + STUDENTS_TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " name TEXT NOT NULL, " +
                " grade TEXT NOT NULL);"
    }
}