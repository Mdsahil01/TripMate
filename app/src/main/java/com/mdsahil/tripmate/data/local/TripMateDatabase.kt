package com.mdsahil.tripmate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mdsahil.tripmate.data.local.dao.ExpenseDao
import com.mdsahil.tripmate.data.local.dao.ExpenseSplitDao
import com.mdsahil.tripmate.data.local.dao.MemberDao
import com.mdsahil.tripmate.data.local.dao.SettlementDao
import com.mdsahil.tripmate.data.local.dao.TripDao
import com.mdsahil.tripmate.data.local.entity.Expense
import com.mdsahil.tripmate.data.local.entity.ExpenseSplit
import com.mdsahil.tripmate.data.local.entity.Member
import com.mdsahil.tripmate.data.local.entity.Settlement
import com.mdsahil.tripmate.data.local.entity.Trip

@Database(
    entities = [
        Trip::class,
        Member::class,
        Expense::class,
        ExpenseSplit::class,
        Settlement::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TripMateDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun memberDao(): MemberDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun expenseSplitDao(): ExpenseSplitDao
    abstract fun settlementDao(): SettlementDao

    companion object {
        @Volatile
        private var INSTANCE: TripMateDatabase? = null

        fun getDatabase(context: Context): TripMateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripMateDatabase::class.java,
                    "tripmate_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
