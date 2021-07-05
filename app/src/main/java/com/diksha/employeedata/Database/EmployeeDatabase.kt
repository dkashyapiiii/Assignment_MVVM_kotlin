package com.diksha.employeedata.Database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.diksha.employeedata.Dao.EmployeeDao
import com.diksha.employeedata.Modal.Employee

@Database(entities = [Employee::class], version = 8)
abstract class EmployeeDatabase : RoomDatabase() {
    abstract fun actorDao(): EmployeeDao
    internal class PopulateAsynTask(actorDatabase: EmployeeDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val actorDao: EmployeeDao

        init {
            actorDao = actorDatabase!!.actorDao()
        }

        protected override fun doInBackground(vararg params: Void?): Void? {
            actorDao.deleteAll()
            return null
        }
    }

    companion object {
        private const val DATABASE_NAME = "EmployeeDatabase"

        @Volatile
        private var INSTANCE: EmployeeDatabase? = null
        fun getInstance(context: Context?): EmployeeDatabase? {
            if (INSTANCE == null) {
                synchronized(EmployeeDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context!!, EmployeeDatabase::class.java,
                            DATABASE_NAME
                        )
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        var callback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateAsynTask(INSTANCE)
            }
        }
    }
}