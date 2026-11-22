package com.mdsahil.tripmate.util

import com.mdsahil.tripmate.data.local.entity.Expense
import com.mdsahil.tripmate.data.local.entity.ExpenseSplit
import com.mdsahil.tripmate.data.local.entity.Member
import com.mdsahil.tripmate.data.local.entity.Settlement
import com.mdsahil.tripmate.data.model.MemberBalance
import com.mdsahil.tripmate.data.model.SuggestedTransfer
import com.mdsahil.tripmate.data.model.TripSummary
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToLong

object BalanceCalculator {

    fun calculateTripSummary(
        members: List<Member>,
        expenses: List<Expense>,
        splits: List<ExpenseSplit>,
        settlements: List<Settlement>
    ): TripSummary {
        val totalSpent = roundTwoDecimals(expenses.sumOf { it.amount })

        // Paid per member
        val paidMap = members.associate { it.id to 0.0 }.toMutableMap()
        for (expense in expenses) {
            paidMap[expense.paidByMemberId] = (paidMap[expense.paidByMemberId] ?: 0.0) + expense.amount
        }

        // Share owed per member
        val shareMap = members.associate { it.id to 0.0 }.toMutableMap()
        for (split in splits) {
            shareMap[split.memberId] = (shareMap[split.memberId] ?: 0.0) + split.amountOwed
        }

        // Settlement adjustments: fromMember paid toMember
        val settledPaidMap = members.associate { it.id to 0.0 }.toMutableMap()
        val settledReceivedMap = members.associate { it.id to 0.0 }.toMutableMap()
        for (settlement in settlements) {
            settledPaidMap[settlement.fromMemberId] = (settledPaidMap[settlement.fromMemberId] ?: 0.0) + settlement.amount
            settledReceivedMap[settlement.toMemberId] = (settledReceivedMap[settlement.toMemberId] ?: 0.0) + settlement.amount
        }

        // Net Balance = (Paid - Share) + SettledPaid - SettledReceived
        val balances = members.map { member ->
            val totalPaid = roundTwoDecimals(paidMap[member.id] ?: 0.0)
            val totalShare = roundTwoDecimals(shareMap[member.id] ?: 0.0)
            val sPaid = roundTwoDecimals(settledPaidMap[member.id] ?: 0.0)
            val sReceived = roundTwoDecimals(settledReceivedMap[member.id] ?: 0.0)

            val rawNet = (totalPaid - totalShare) + sPaid - sReceived
            val netBalance = roundTwoDecimals(rawNet)

            MemberBalance(
                memberId = member.id,
                memberName = member.name,
                totalPaid = totalPaid,
                totalShare = totalShare,
                netBalance = netBalance
            )
        }

        val transfers = calculateSuggestedTransfers(balances)

        return TripSummary(
            totalSpent = totalSpent,
            balances = balances,
            suggestedTransfers = transfers
        )
    }

    fun calculateSuggestedTransfers(
        balances: List<MemberBalance>
    ): List<SuggestedTransfer> {
        val debtors = mutableListOf<Triple<Long, String, Double>>()
        val creditors = mutableListOf<Triple<Long, String, Double>>()

        for (b in balances) {
            val roundedNet = roundTwoDecimals(b.netBalance)
            if (roundedNet < -0.01) {
                debtors.add(Triple(b.memberId, b.memberName, abs(roundedNet)))
            } else if (roundedNet > 0.01) {
                creditors.add(Triple(b.memberId, b.memberName, roundedNet))
            }
        }

        val transfers = mutableListOf<SuggestedTransfer>()
        var dIdx = 0
        var cIdx = 0

        while (dIdx < debtors.size && cIdx < creditors.size) {
            val (dId, dName, dOwes) = debtors[dIdx]
            val (cId, cName, cGets) = creditors[cIdx]

            val transferAmount = min(dOwes, cGets)
            val roundedTransfer = roundTwoDecimals(transferAmount)

            if (roundedTransfer > 0) {
                transfers.add(
                    SuggestedTransfer(
                        fromMemberId = dId,
                        fromMemberName = dName,
                        toMemberId = cId,
                        toMemberName = cName,
                        amount = roundedTransfer
                    )
                )
            }

            val remOwes = roundTwoDecimals(dOwes - transferAmount)
            val remGets = roundTwoDecimals(cGets - transferAmount)

            if (remOwes <= 0.01) {
                dIdx++
            } else {
                debtors[dIdx] = Triple(dId, dName, remOwes)
            }

            if (remGets <= 0.01) {
                cIdx++
            } else {
                creditors[cIdx] = Triple(cId, cName, remGets)
            }
        }

        return transfers
    }

    private fun roundTwoDecimals(value: Double): Double {
        return (value * 100.0).roundToLong() / 100.0
    }
}
