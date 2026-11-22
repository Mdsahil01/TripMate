package com.mdsahil.tripmate.data.repository

import com.mdsahil.tripmate.data.local.TripMateDatabase
import com.mdsahil.tripmate.data.local.entity.Expense
import com.mdsahil.tripmate.data.local.entity.ExpenseSplit
import com.mdsahil.tripmate.data.local.entity.Member
import com.mdsahil.tripmate.data.local.entity.Settlement
import com.mdsahil.tripmate.data.local.entity.Trip
import com.mdsahil.tripmate.data.model.TripSummary
import com.mdsahil.tripmate.util.BalanceCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class TripMateRepository(private val db: TripMateDatabase) {

    private val tripDao = db.tripDao()
    private val memberDao = db.memberDao()
    private val expenseDao = db.expenseDao()
    private val expenseSplitDao = db.expenseSplitDao()
    private val settlementDao = db.settlementDao()

    // Trips
    fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips()
    fun getTripById(tripId: Long): Flow<Trip?> = tripDao.getTripById(tripId)

    suspend fun createTripWithMembers(trip: Trip, memberNames: List<String>): Long {
        val tripId = tripDao.insertTrip(trip)
        val members = memberNames.mapIndexed { index, name ->
            Member(
                tripId = tripId,
                name = name,
                isOrganizer = index == 0
            )
        }
        memberDao.insertMembers(members)
        return tripId
    }

    suspend fun updateTrip(trip: Trip) = tripDao.updateTrip(trip)
    suspend fun deleteTrip(trip: Trip) = tripDao.deleteTrip(trip)

    // Members
    fun getMembersForTrip(tripId: Long): Flow<List<Member>> = memberDao.getMembersForTrip(tripId)
    suspend fun addMember(member: Member): Long = memberDao.insertMember(member)
    suspend fun deleteMember(member: Member) = memberDao.deleteMember(member)

    // Expenses & Splits
    fun getExpensesForTrip(tripId: Long): Flow<List<Expense>> = expenseDao.getExpensesForTrip(tripId)
    fun getExpenseById(expenseId: Long): Flow<Expense?> = expenseDao.getExpenseById(expenseId)

    suspend fun addExpenseWithSplits(expense: Expense, splits: List<ExpenseSplit>): Long {
        val expenseId = expenseDao.insertExpense(expense)
        val updatedSplits = splits.map { it.copy(expenseId = expenseId) }
        expenseSplitDao.insertSplits(updatedSplits)
        return expenseId
    }

    suspend fun updateExpenseWithSplits(expense: Expense, splits: List<ExpenseSplit>) {
        expenseDao.updateExpense(expense)
        expenseSplitDao.deleteSplitsForExpense(expense.id)
        expenseSplitDao.insertSplits(splits)
    }

    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)

    fun getSplitsForTrip(tripId: Long): Flow<List<ExpenseSplit>> = expenseSplitDao.getSplitsForTrip(tripId)

    // Settlements
    fun getSettlementsForTrip(tripId: Long): Flow<List<Settlement>> = settlementDao.getSettlementsForTrip(tripId)
    suspend fun recordSettlement(settlement: Settlement): Long = settlementDao.insertSettlement(settlement)

    // Trip Summary & Balance Math
    fun getTripSummary(tripId: Long): Flow<TripSummary> {
        return combine(
            memberDao.getMembersForTrip(tripId),
            expenseDao.getExpensesForTrip(tripId),
            expenseSplitDao.getSplitsForTrip(tripId),
            settlementDao.getSettlementsForTrip(tripId)
        ) { members, expenses, splits, settlements ->
            BalanceCalculator.calculateTripSummary(members, expenses, splits, settlements)
        }
    }
}
