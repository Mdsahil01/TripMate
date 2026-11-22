package com.mdsahil.tripmate

import com.mdsahil.tripmate.data.local.entity.Expense
import com.mdsahil.tripmate.data.local.entity.ExpenseSplit
import com.mdsahil.tripmate.data.local.entity.Member
import com.mdsahil.tripmate.data.local.entity.Settlement
import com.mdsahil.tripmate.util.BalanceCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BalanceCalculatorTest {

    @Test
    fun testEqualSplitBalancesAndTransfers() {
        val sahil = Member(id = 1L, tripId = 100L, name = "Sahil", isOrganizer = true)
        val neha = Member(id = 2L, tripId = 100L, name = "Neha", isOrganizer = false)
        val rahul = Member(id = 3L, tripId = 100L, name = "Rahul", isOrganizer = false)

        val members = listOf(sahil, neha, rahul)

        // Expense: Sahil pays ₹300 for Food, split equally (₹100 each)
        val expense = Expense(
            id = 10L,
            tripId = 100L,
            title = "Dinner",
            amount = 300.0,
            category = "Food",
            paidByMemberId = sahil.id
        )

        val splits = listOf(
            ExpenseSplit(id = 1L, expenseId = 10L, memberId = sahil.id, amountOwed = 100.0),
            ExpenseSplit(id = 2L, expenseId = 10L, memberId = neha.id, amountOwed = 100.0),
            ExpenseSplit(id = 3L, expenseId = 10L, memberId = rahul.id, amountOwed = 100.0)
        )

        val summary = BalanceCalculator.calculateTripSummary(members, listOf(expense), splits, emptyList())

        assertEquals(300.0, summary.totalSpent, 0.01)

        val sahilBal = summary.balances.first { it.memberId == sahil.id }
        val nehaBal = summary.balances.first { it.memberId == neha.id }
        val rahulBal = summary.balances.first { it.memberId == rahul.id }

        assertEquals(200.0, sahilBal.netBalance, 0.01)
        assertEquals(-100.0, nehaBal.netBalance, 0.01)
        assertEquals(-100.0, rahulBal.netBalance, 0.01)

        val transfers = summary.suggestedTransfers
        assertEquals(2, transfers.size)

        val nehaTransfer = transfers.first { it.fromMemberId == neha.id }
        assertEquals(sahil.id, nehaTransfer.toMemberId)
        assertEquals(100.0, nehaTransfer.amount, 0.01)

        val rahulTransfer = transfers.first { it.fromMemberId == rahul.id }
        assertEquals(sahil.id, rahulTransfer.toMemberId)
        assertEquals(100.0, rahulTransfer.amount, 0.01)
    }

    @Test
    fun testSettlementUpdatesBalances() {
        val sahil = Member(id = 1L, tripId = 100L, name = "Sahil", isOrganizer = true)
        val neha = Member(id = 2L, tripId = 100L, name = "Neha", isOrganizer = false)
        val rahul = Member(id = 3L, tripId = 100L, name = "Rahul", isOrganizer = false)

        val members = listOf(sahil, neha, rahul)

        val expense = Expense(
            id = 10L,
            tripId = 100L,
            title = "Taxi",
            amount = 300.0,
            category = "Transport",
            paidByMemberId = sahil.id
        )

        val splits = listOf(
            ExpenseSplit(id = 1L, expenseId = 10L, memberId = sahil.id, amountOwed = 100.0),
            ExpenseSplit(id = 2L, expenseId = 10L, memberId = neha.id, amountOwed = 100.0),
            ExpenseSplit(id = 3L, expenseId = 10L, memberId = rahul.id, amountOwed = 100.0)
        )

        // Neha settles ₹100 to Sahil
        val settlement = Settlement(
            id = 1L,
            tripId = 100L,
            fromMemberId = neha.id,
            toMemberId = sahil.id,
            amount = 100.0
        )

        val summary = BalanceCalculator.calculateTripSummary(members, listOf(expense), splits, listOf(settlement))

        val sahilBal = summary.balances.first { it.memberId == sahil.id }
        val nehaBal = summary.balances.first { it.memberId == neha.id }
        val rahulBal = summary.balances.first { it.memberId == rahul.id }

        assertEquals(100.0, sahilBal.netBalance, 0.01)
        assertEquals(0.0, nehaBal.netBalance, 0.01)
        assertEquals(-100.0, rahulBal.netBalance, 0.01)

        val transfers = summary.suggestedTransfers
        assertEquals(1, transfers.size)
        assertEquals(rahul.id, transfers[0].fromMemberId)
        assertEquals(sahil.id, transfers[0].toMemberId)
        assertEquals(100.0, transfers[0].amount, 0.01)
    }

    @Test
    fun testFullySettledTrip() {
        val sahil = Member(id = 1L, tripId = 100L, name = "Sahil", isOrganizer = true)
        val neha = Member(id = 2L, tripId = 100L, name = "Neha", isOrganizer = false)

        val members = listOf(sahil, neha)

        val expense = Expense(
            id = 10L,
            tripId = 100L,
            title = "Snacks",
            amount = 150.0,
            category = "Snacks",
            paidByMemberId = sahil.id
        )

        val splits = listOf(
            ExpenseSplit(id = 1L, expenseId = 10L, memberId = sahil.id, amountOwed = 75.0),
            ExpenseSplit(id = 2L, expenseId = 10L, memberId = neha.id, amountOwed = 75.0)
        )

        val settlement = Settlement(
            id = 1L,
            tripId = 100L,
            fromMemberId = neha.id,
            toMemberId = sahil.id,
            amount = 75.0
        )

        val summary = BalanceCalculator.calculateTripSummary(members, listOf(expense), splits, listOf(settlement))

        summary.balances.forEach { balance ->
            assertEquals(0.0, balance.netBalance, 0.01)
        }
        assertTrue(summary.suggestedTransfers.isEmpty())
    }
}
