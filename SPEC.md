# TripMate v1 — App Blueprint

## Core Mental Model
- A **Trip** is the container (e.g., "Bangalore Android Event", "College Hackathon").
- Individual **Expenses** are the primary events—not everyone in a trip shares every expense.
- **Obligations** (who owes whom) are calculated strictly from expense splits.
- **Settlements** (repayments) are recorded separately from expenses so repaying a friend never counts as a new trip expense.

## v1 Screens & Features
1. **Onboarding & Trip Home:** Welcome screen (`Get Started`) + **Your Trips** list showing trip name, date, member count, and total spent.
2. **Create Trip:** Enter Trip Name, optional Description, Trip Date, and add group members (with `You (Sahil)` as Organizer).
3. **Trip Details (3 Tabs: `Expenses`, `Summary`, `Members`):**
    - **Expenses Tab:** Shows Total Spent and chronological list of expenses (Title, Paid by, participant count, category icon, amount in ₹).
    - **Members Tab:** Manage trip members, `+ Add Member`, and shareable invite code.
    - **Summary Tab:** Shows Total Spent, Your Share, per-person net balances (`-₹140`, `+₹290`) with tap-to-see-why expense breakdown, plus a **Category Breakdown** view (Food, Transport, Snacks, Other).
4. **Add Expense (Fast & Flexible):**
    - **Quick Save ("Capture Now, Complete Later"):** Save with just **Amount + Who Paid** when in a rush (e.g., boarding a bus); auto-tags the expense with a **"Needs Details"** badge until participants/splits are completed.
    - **Full Mode:** Title, Amount (₹), Category (`Food`, `Transport`, `Snacks`, `Other`), Paid By, selected participants, and **Equal Split** or **Custom Split** (exact rupee amounts per participant).
5. **Settle Up & Completed Trip:** Suggested transfers (*"Sahil pays Neha ₹140"*) with a **"Mark as Settled"** button that saves a Settlement record and updates balances to ₹0, unlocking the **Trip Settled!** celebration screen.

## Current Milestone: Issue #1 (Foundation)
Set up the TripMate Material 3 color theme and the complete local Room Database (`Trip`, `Member`, `Expense`, `ExpenseSplit`, `Settlement`), DAOs, and `TripMateRepository` with clean unit tests for split/balance math.