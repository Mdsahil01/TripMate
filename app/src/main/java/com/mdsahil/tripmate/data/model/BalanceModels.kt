package com.mdsahil.tripmate.data.model

data class MemberBalance(
    val memberId: Long,
    val memberName: String,
    val totalPaid: Double,
    val totalShare: Double,
    val netBalance: Double
)

data class SuggestedTransfer(
    val fromMemberId: Long,
    val fromMemberName: String,
    val toMemberId: Long,
    val toMemberName: String,
    val amount: Double
)

data class TripSummary(
    val totalSpent: Double,
    val balances: List<MemberBalance>,
    val suggestedTransfers: List<SuggestedTransfer>
)
