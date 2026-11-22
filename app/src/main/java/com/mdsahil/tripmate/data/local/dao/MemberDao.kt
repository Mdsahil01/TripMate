package com.mdsahil.tripmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdsahil.tripmate.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<Member>): List<Long>

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("SELECT * FROM members WHERE tripId = :tripId")
    fun getMembersForTrip(tripId: Long): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE tripId = :tripId")
    suspend fun getMembersForTripSync(tripId: Long): List<Member>
}
