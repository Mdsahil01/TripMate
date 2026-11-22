# TripMate — Product Discovery Proposal & Development Context

> **Purpose:** This document captures the important product-discovery decisions, reasoning, assumptions, unresolved questions, and direction discussed for TripMate so development can continue in another platform without losing context.
>
> **Current stage:** Product discovery / pre-development
>
> **Important:** This is a record of what we discussed and decided so far. It is **not** a final PRD, architecture specification, or technical implementation plan. Several decisions remain intentionally open.

---

## 1. Product Overview

**Product name:** TripMate

**Initial platform direction:** Android-first

**Target audience:** College students

**Primary problem:** Calculating and understanding who owes whom after group outings, trips, meetups, hackathons, and similar shared activities.

### Core idea

TripMate is intended to make shared expenses between friends simple, transparent, and trustworthy.

The key insight that emerged during discovery is:

> **TripMate should not primarily think of a trip as one giant bill. It should think of individual payments/expenses as the fundamental unit of expense tracking.**

A trip provides the context and group membership, but each payment can have its own payer, participants, allocation, and resulting obligations.

---

# 2. Why TripMate Exists

This idea came from a real personal experience rather than a hypothetical problem.

During a group outing/trip, expenses became scattered across payment histories and memory. The user and roommate had to:

1. Go through individual payment histories.
2. Find relevant transactions.
3. Put amounts into a calculator.
4. Determine who benefited from each expense.
5. Determine how much each person actually owed.
6. Resolve disagreements about individual fares.
7. Account for situations where one person transferred money to another.
8. Reconstruct the final balances.

This process was mentally exhausting after the trip.

### Example from the experience

A bus/transport fare became a point of disagreement — whether the fare was ₹33 or ₹30 per person. Even though payment histories existed, the group still had to reconstruct what the actual expense meant.

Another complication occurred when the user's phone was about to die, so money was transferred to a roommate. Later, the final balances were confusing and small amounts such as ₹15–₹20 appeared unexplained.

### Core pain

The problem is therefore bigger than arithmetic.

It is:

> **Reconstructing what actually happened financially during a group activity, then turning those events into fair, understandable obligations.**

---

# 3. Early Validation

The problem is not currently based only on an isolated assumption.

The user has personally experienced it and discussed the idea with:

- Roommate
- Friends
- Other group members

They responded positively and indicated that they would want/use something like this.

This is useful early evidence, but it is **not yet broad product validation**.

Still to validate:

- How common the problem is among college students.
- How frequently it occurs.
- Whether students consistently record expenses in a dedicated app.
- Whether all group members will participate.
- What minimum interaction is acceptable during a trip.
- What users consider trustworthy evidence.
- Whether the proposed workflow is actually easier than current alternatives.

---

# 4. Existing Alternatives / Current Behaviour

Without TripMate, students may:

- Open Google Pay or another payment app.
- Search payment history.
- Remember cash payments.
- Check who paid for what.
- Use a calculator.
- Manually calculate individual shares.
- Send messages to friends telling them what they owe.
- Explain calculations when someone questions the amount.
- Share payment receipts/screenshots when necessary.
- Make individual UPI payments to settle balances.

The process is fragmented.

The problem is not that calculators cannot calculate.

The problem is that **the underlying information is scattered and the relationships between payments, participants, obligations, and settlements are unclear.**

---

# 5. Initial Product Scope

The initial scope discussed was:

1. Trips
2. Members
3. Expenses/payments
4. Balances
5. Settlements

These are the core capabilities.

The scope is intentionally small. We do not want to add advanced features merely because they are technically possible.

---

# 6. Product Philosophy

Several principles emerged during discovery.

### 6.1 Problem before solution

Do not start by designing screens or writing code.

First understand:

- Who has the problem?
- What actually happens?
- Why is it painful?
- What do users currently do?
- What assumptions are we making?

### 6.2 Keep complexity under the hood

The target users are college students.

The product should not require users to understand accounting, settlement algorithms, payment infrastructure, or complicated workflows.

> **The system can be sophisticated internally, but the user experience should feel simple.**

### 6.3 Reduce mental effort

The product should remove the need to:

- Reconstruct payment histories.
- Manually calculate every share.
- Remember every participant.
- Recalculate after changes.
- Explain unexplained balances.

### 6.4 Transparency creates trust

A result such as:

> "You owe ₹240"

is not enough.

A user should be able to understand:

- Which expenses created the amount.
- Who paid.
- Who participated.
- How the amount was divided.
- Whether a settlement has already happened.

### 6.5 Do not over-engineer the MVP

We discussed payment gateways, WhatsApp bots, web apps, KMP, automated payment verification, etc.

The conclusion was to avoid adding these merely because they sound advanced.

The core product should be validated first.

---

# 7. Fundamental Product Model: Payment/Expense First

This was one of the most important discoveries.

## Old/simple model

A trip is treated as one large bill:

> Total trip expenses → divide across everyone → calculate balances.

This does not reflect how real college trips work.

People do not necessarily share every expense.

## Proposed TripMate model

Each individual payment/expense is independently recorded.

For every expense, we need to know:

- Who paid.
- How much was paid.
- Who participated/benefited.
- How the amount is allocated.
- What each participant owes as a result.

### Example

Four friends:

- Sahil
- Pavan
- Aditya
- Sarthak

Sahil pays ₹180 for metro travel for Sahil and Pavan.

The expense can be represented as:

**Paid by:** Sahil  
**Amount:** ₹180  
**Participants:** Sahil, Pavan  
**Split:** Equal

Result:

- Sahil share = ₹90
- Pavan share = ₹90
- Pavan owes Sahil ₹90

Now imagine Sahil pays ₹120 for a cab shared by:

- Sahil
- Aditya
- Sarthak

Equal split:

- Sahil = ₹40
- Aditya = ₹40
- Sarthak = ₹40

Result:

- Aditya owes Sahil ₹40
- Sarthak owes Sahil ₹40
- Pavan owes nothing for this expense.

This demonstrates why trip-wide equal splitting is insufficient.

---

# 8. Expense Allocation

When recording a payment, the payer should be able to specify who participated.

Potential allocation approaches discussed:

### Equal split

Example:

₹180 shared by 2 people

→ ₹90 each

### Exact/custom allocation

If participants have different actual fares or agreed shares, the payer can specify the exact allocation.

For example:

- Sahil = ₹70
- Pavan = ₹110

The total allocation must match the expense amount.

### Important product principle

The app should not assume that everyone in the trip shares every payment.

Trip membership and expense participation are different concepts.

---

# 9. Recording Expenses: Capture Now, Complete Later

The preferred UX direction is:

> **Record important expenses immediately and add missing details later.**

Reason:

During a trip, users are busy.

They may be:

- Walking.
- Boarding a bus.
- Leaving a meetup.
- Traveling in an auto.
- Running low on battery.
- Talking to friends.

They should not have to complete a long form.

### In-the-moment goal

Capture the essential information quickly.

Potential essentials:

- Amount
- Payer
- Trip
- Participants / basic allocation

### Later

Users can add or correct:

- Description
- Missing participants
- Exact allocation
- Receipt/evidence
- Other details

### Important implication

An expense can be saved before all information is complete, but the product should distinguish between:

- Saved/recorded
- Complete/allocated
- Potentially disputed
- Settled

We have not finalized the exact states or UI.

---

# 10. Trust and Expense Ownership

We discussed who should have authority when an expense is recorded.

Initial position:

> **The person who paid is the strongest default source for the amount and payment event.**

Reason:

The payer knows:

- When they paid.
- How much they paid.
- What payment they made.

However, this does not necessarily mean the payer has unquestionable authority over every detail.

Example:

Sahil paid ₹180 for an auto.

Sahil is the strongest source for:

> "I paid ₹180."

But another participant may correctly dispute:

> "I wasn't in that auto."

Therefore:

- Payer is the default recorder/source for the payment.
- Participants should be able to question relevant details.
- Disputes should be based on evidence/reason rather than arguments for their own sake.

---

# 11. Evidence and Receipts

Evidence was discussed as a way to increase trust.

Possible evidence:

- Payment receipt
- Payment confirmation
- Other transaction information

However:

> **Receipts should support trust, not turn every small expense into administrative work.**

We should not require proof for every ₹20/₹30 expense unless there is a strong product reason.

Also, a screenshot should not automatically be treated as unquestionable truth. Screenshots can potentially be manipulated.

Therefore, the product should distinguish between:

- User-provided evidence
- User claims
- Confirmed information
- Independently verified payment status

The exact evidence model is not yet finalized.

---

# 12. Expense vs Obligation vs Settlement

This distinction became extremely important.

## Expense

What actually happened.

Example:

Sahil paid ₹180 for metro for Sahil + Pavan.

## Obligation

The resulting amount another participant owes.

Example:

Pavan owes Sahil ₹90.

## Settlement

Money actually transferred to reduce/clear an obligation.

Example:

Pavan sends Sahil ₹90.

These should not be treated as the same thing.

### Why?

A repayment is not another expense.

If Pavan sends Sahil ₹90, TripMate should not interpret that as a new ₹90 shared expense.

This separation is fundamental to the future data model and business logic.

---

# 13. Settlement Is the Hardest Product Problem

The most difficult unresolved area is:

> **How does TripMate know that someone actually paid another person?**

TripMate can calculate an obligation.

Example:

Pavan owes Sahil ₹90.

But TripMate cannot automatically assume that the money moved merely because Sahil says:

> "I asked Pavan to pay."

Or because a UPI app was opened.

---

# 14. Understanding Payment Verification

We explored how services such as ticket booking, ride booking, and other merchant systems know that a payment succeeded.

The key concept:

### Merchant payment

A platform such as a booking service creates an order and integrates with a payment provider.

The general flow is:

1. Application creates an order.
2. Payment provider processes payment.
3. Payment system determines transaction status.
4. Provider communicates the status to the merchant/backend.
5. Merchant/backend validates the information.
6. Order is marked paid/confirmed.

Possible mechanisms include:

- Payment-provider APIs
- Server-to-server webhooks
- Transaction/status queries
- Verification/authentication mechanisms
- Transaction identifiers and order references

### Important distinction

Opening a payment app is not equivalent to verifying a successful payment.

A platform can reliably confirm a payment when it has an appropriate trusted integration with the payment system.

TripMate's peer-to-peer scenario is different because the actual money movement can happen outside TripMate.

---

# 15. Initial Settlement Direction

We discussed three possible levels:

### User reports payment

> "I paid."

TripMate knows the user claims payment occurred.

### Recipient confirms

The recipient acknowledges receiving the money.

### Trusted payment integration

A supported payment integration independently provides trustworthy transaction status.

For an early TripMate version, we leaned toward **not building a payment gateway** and not pretending that TripMate can universally verify every UPI transaction.

A practical early model could distinguish states such as:

- Pending
- Reported as paid
- Confirmed

Exact state names and rules remain open.

---

# 16. Redirected / Direct Settlement

A more interesting settlement idea emerged.

Suppose:

- Aditya owes Sahil ₹50.
- Sahil owes Pavan ₹50.

Instead of:

Aditya → Sahil → Pavan

TripMate could potentially suggest:

Aditya → Pavan

This can reduce the number of actual transfers.

However, this requires:

- Agreement/authorization among the relevant people.
- Correct mapping of which obligations are being settled.
- Clear records showing that the redirected payment discharged the appropriate obligations.

This is essentially a form of settlement/netting.

It is promising, but it should not be implemented casually because incorrect bookkeeping could create serious trust problems.

---

# 17. Settlement UI Concept Discussed

At a high level, an individual person's balance could show:

> **You owe ₹X to Pavan**  
> **You should receive ₹Y from Aditya**

The product could then help users initiate individual payments.

We recognized that UPI does not generally mean one user can magically send one payment to multiple recipients simultaneously. Individual transfers still exist.

Therefore, TripMate's job is primarily to:

1. Calculate obligations.
2. Reduce confusion.
3. Help users make the appropriate transfers.
4. Track/report settlement state.
5. Eventually reduce unnecessary transfers through intelligent settlement/netting.

---

# 18. Trip Creation and Membership

The proposed model:

A user creates a trip.

They invite other people.

Example:

Sahil creates a trip and invites:

- Pavan
- Aditya
- Sarthak

Everyone who joins can participate in the shared trip.

### Important distinction

A person being an expense participant does not necessarily mean they are an active TripMate account user.

We eventually discussed whether everyone should be forced to install the app.

The current direction is:

> **TripMate remains an Android app. Installation is the normal first step for full participation, but the creator should not be blocked from recording expenses just because every invited person has not installed/joined yet.**

We do not need to build a separate web experience simply to avoid installation.

A person can be represented in a trip before becoming an active app user, with appropriate access controls.

Exact invitation/join/claim-account behaviour remains to be designed.

---

# 19. Android-First Direction

The product direction was simplified to:

> **Android-first.**

We briefly explored:

- WhatsApp bot
- WhatsApp notifications
- Web app
- KMP
- Compose Multiplatform

But we intentionally decided **not to complicate the product right now**.

WhatsApp is not a core integration.

Invitations can simply use normal sharing mechanisms:

- WhatsApp
- SMS
- Email
- Other messaging platforms
- Android share functionality

The product itself remains the focus.

---

# 20. WhatsApp Decision

We considered a WhatsApp-first bot because students already use WhatsApp.

Possible bot interactions could have included:

- Recording expenses.
- Checking balances.
- Invitations.
- Notifications.

But we decided against making WhatsApp part of the core product.

Reason:

- It adds external platform dependency.
- It adds messaging/backend complexity.
- It distracts from the core Android product.
- Normal sharing is sufficient for invitations.

**Current decision:**

> No dedicated WhatsApp integration in the initial TripMate product.

---

# 21. KMP / Web Discussion

We briefly discussed KMP because TripMate could eventually exist on web and mobile.

The user considered:

- Android app
- Web
- KMP

But then explicitly decided:

> **Let's not complicate it.**

Therefore, KMP and web should currently be treated as **future possibilities, not current architecture decisions**.

Do not design the MVP around them unless a future product decision makes them necessary.

---

# 22. What TripMate Is NOT Yet

At this stage, we have NOT finalized:

- Exact screens
- Navigation structure
- Database schema
- Backend technology
- API design
- Authentication mechanism
- Android architecture
- MVVM implementation
- Room schema
- Retrofit APIs
- Hilt setup
- Cloud infrastructure
- Payment gateway
- UPI integration
- Automatic payment verification
- Settlement algorithm
- Dispute resolution algorithm
- Notification system
- Web application
- KMP
- iOS support

These should be decided later based on product requirements.

---

# 23. Product Discovery Before Development

The original goal was explicitly:

> **Do not jump straight into screens/code. Understand how a real production team thinks before implementation.**

The intended progression is:

1. Product discovery
2. Problem validation
3. Requirements
4. MVP definition
5. User journeys
6. UX flows
7. Business rules
8. Technical architecture
9. Data/system design
10. Visual design
11. Development planning
12. Implementation
13. Testing
14. Release

These phases may iterate in a real product team.

---

# 24. Proposed Future Product Discovery Topics

Before coding, the remaining important discovery work should include:

### A. MVP definition

Determine exactly what TripMate V1 must do.

### B. User journeys

For example:

- Create trip
- Invite member
- Join trip
- Record expense
- Edit expense
- Add participant
- Complete allocation
- View balance
- Report settlement
- Confirm settlement
- Handle disagreement

### C. Expense rules

Define:

- Equal split
- Exact/custom split
- Who can edit
- Who can delete
- What happens when participants change
- What happens when an expense is incomplete
- What happens when an expense is disputed

### D. Settlement rules

Define:

- Pending
- Reported
- Confirmed
- Cancelled
- Partial settlement
- Redirected settlement
- Multiple obligations between two users
- Netting

### E. Trust model

Determine:

- What is considered authoritative?
- What evidence can be attached?
- Who can dispute?
- Who can resolve?
- What happens when members disagree?

### F. Group membership

Determine:

- Invited user
- Joined user
- Unregistered participant
- Removed participant
- Ownership
- Permissions

### G. Product quality goals

Examples:

- Fast expense entry
- Low cognitive load
- Accurate calculations
- Transparent balances
- Lightweight Android experience
- Reliable offline/poor-network behaviour
- Clear error states

Only after these are reasonably clear should we move confidently into architecture.

---

# 25. The Core Product Mental Model

If the entire TripMate idea had to be reduced to one mental model:

```text
TRIP
  |
  +-- MEMBERS
  |
  +-- EXPENSES
  |     |
  |     +-- payer
  |     +-- amount
  |     +-- participants
  |     +-- allocation
  |     |
  |     +--> OBLIGATIONS
  |
  +-- BALANCES
  |
  +-- SETTLEMENTS
        |
        +-- reported
        +-- confirmed
        +-- potentially redirected
```

The **trip is context**.

The **expense/payment is the fundamental event**.

The **obligation is the financial consequence**.

The **settlement is the repayment event**.

The **balance is the current summary of those events**.

---

# 26. Product Vision — Current Version

> **TripMate aims to make shared expenses among college friends simple, transparent, and stress-free by recording individual payments, identifying exactly who benefited from them, calculating the resulting obligations, and making settlement easy to understand and complete.**

The product should hide financial complexity rather than expose it.

---

# 27. Current Product Principles

1. **Real problem first.**
2. **Payment-level accuracy over trip-wide assumptions.**
3. **Every expense should have clear ownership and allocation.**
4. **Do not assume everyone shares every expense.**
5. **Capture quickly; complete later.**
6. **Payer is the default source for the payment event.**
7. **Participants can question relevant details.**
8. **Evidence supports trust but should not create unnecessary friction.**
9. **Expense, obligation, and settlement are separate concepts.**
10. **Never pretend a payment is verified when the system cannot verify it.**
11. **Keep complex calculations under the hood.**
12. **Make the result understandable to ordinary college students.**
13. **Don't over-engineer the MVP.**
14. **Android-first.**
15. **Don't let unvalidated technology choices drive the product.**

---

# 28. Current Status

**Product discovery:** In progress

**Problem clarity:** Strong initial evidence

**Target audience:** Defined

**Core problem:** Defined

**Core product model:** Emerging and relatively clear

**Expense model:** Direction established

**Settlement model:** Important unresolved area

**Trust model:** Partially defined

**MVP:** Not finalized

**UX:** Not started intentionally

**Architecture:** Not started intentionally

**Development:** Not started intentionally

---

# 29. Important Open Questions

These are the questions that should be answered before architecture:

1. What exactly is the TripMate MVP?
2. What is the minimum information required to record an expense?
3. What happens when an expense is incomplete?
4. Can expenses be edited after other balances have been calculated?
5. Who can edit an expense?
6. What happens when someone disputes an expense?
7. How should partial payments work?
8. How should settlement confirmation work?
9. How should redirected settlements work?
10. How should multiple debts between the same people be displayed?
11. How should balances be calculated after settlements?
12. What happens when a member joins late?
13. What happens when a participant has not installed the app?
14. What permissions does a trip owner have?
15. What happens if the same person accidentally records the same expense twice?
16. What happens when the device is offline?
17. What happens when two people modify related data?
18. What information must be persisted locally?
19. What information requires a backend?
20. What level of payment integration, if any, is justified for V1?

---

# 30. Development Principle

When development eventually starts, the goal should not be:

> "Let's build all the screens."

The goal should be:

> **Build the smallest complete slice of the real product.**

For example, eventually we might build one complete flow:

```text
Create Trip
    ↓
Add Members
    ↓
Record One Expense
    ↓
Select Participants
    ↓
Calculate Allocation
    ↓
Generate Obligation
    ↓
Show Balance
```

Then test whether that entire loop actually works before expanding the product.

---

# 31. Final Direction for the Next Platform

If another AI/development platform is going to continue this project, it should understand:

**Do not immediately generate Android screens or architecture.**

First continue product discovery from the unresolved questions above.

The most important conceptual decision already made is:

> **TripMate is not an "equal trip bill splitter." It is a shared expense and settlement system where individual payments are the primary financial events.**

The product should make it extremely easy for a college student to say:

> "I paid this much, these people were involved, and this is how the amount should be split."

Then TripMate should handle the complicated work of determining:

> "Therefore, this person owes this person this amount."

And finally help the group understand and settle those obligations without requiring them to manually reconstruct the entire trip from payment histories.
