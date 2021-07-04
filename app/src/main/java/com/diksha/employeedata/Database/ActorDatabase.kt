package com.diksha.employeedata.Database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.diksha.employeedata.Dao.ActorDao
import com.diksha.employeedata.Modal.Employee

@Database(entities = [Employee::class], version = 8)
abstract class ActorDatabase : RoomDatabase() {
    abstract fun actorDao(): ActorDao
    internal class PopulateAsynTask(actorDatabase: ActorDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val actorDao: ActorDao


        init {
            actorDao = actorDatabase!!.actorDao()
        }

       protected override fun doInBackground(vararg params: Void?): Void? {
            actorDao.deleteAll()
            return null        }
    }

    companion object {
        private const val DATABASE_NAME = "EmployeeDatabase"

        @Volatile
        private var INSTANCE: ActorDatabase? = null
        fun getInstance(context: Context?): ActorDatabase? {
            if (INSTANCE == null) {
                synchronized(ActorDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context!!, ActorDatabase::class.java,
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