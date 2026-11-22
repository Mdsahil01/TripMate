# TripMate — AI Coding Rules

You are building **TripMate**, an Android app for college students to track shared trip expenses and see who owes whom.

## Build & Verify Commands
- Build check: `./gradlew assembleDebug`
- Unit tests: `./gradlew testDebugUnitTest`

## Tech & Simplicity Rules
- **100% Kotlin & Jetpack Compose (Material 3):** Never create XML layouts.
- **Offline-First (v1):** Store all data locally on the phone using **Room Database**. Do NOT add Firebase, Supabase, Retrofit, login/signup screens, or internet permissions in v1.
- **Clean & Simple Structure:** Organize code cleanly by feature/layer: `ui/theme`, `data/local` (Room entities, DAOs, database), `data/repository`, and `ui/screens`.
- **Visual Style:** Match the TripMate brand palette—emerald green primary (`#1B7F5A` / `#10B981`), soft mint/white backgrounds (`#F6FAF8`), dark slate cards (`#131A17` for completed/dark banners), and category badge colors (Food = green, Transport = blue, Snacks = amber, Other = purple).
- **Small Safe Steps:** Always run a Gradle build check after creating or editing files and fix any compile errors before finishing.