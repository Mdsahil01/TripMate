package com.mdsahil.tripmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdsahil.tripmate.data.local.entity.Settlement
import kotlinx.coroutines.flow.Flow

@Dao
interface SettlementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettlement(settlement: Settlement): Long

    @Delete
    suspend fun deleteSettlement(settlement: Settlement)

    @Query("SELECT * FROM settlements WHERE tripId = :tripId ORDER BY date DESC")
    fun getSettlementsForTrip(tripId: Long): Flow<List<Settlement>>

    @Query("SELECT * FROM settlements WHERE tripId = :tripId ORDER BY date DESC")
    suspend fun getSettlementsForTripSync(tripId: Long): List<Settlement>
}
