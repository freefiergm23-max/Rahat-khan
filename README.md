# Kitti — Native Android AI Voice Assistant

Kitti is a Kotlin + Jetpack Compose Android assistant starter project designed around official Android APIs.

## Build

Open this folder in Android Studio and let Gradle sync. Then run the `app` configuration on an Android 8.0+ device.

No AI/API/configuration key is required by this project.

## What is implemented

- Bengali/English/Arabic/Hindi speech recognition attempt
- Natural-ish command routing for common app/settings actions
- App launching for YouTube, WhatsApp, Instagram, Facebook, Maps, Camera, Settings
- Google/YouTube search intents
- Android AlarmClock timer/alarm intents
- Calling by explicit phone number using official CALL_PHONE permission
- Text-to-Speech responses
- Local command history with clear button
- Dark/light Material 3 UI
- Permission requests through Android
- Basic incoming SMS receiver notification without exposing SMS body

## Important Android limitations

Kitti cannot legitimately become an unrestricted controller of the phone. Android intentionally blocks normal apps from silently reading WhatsApp/Gmail private data, bypassing permissions, or changing protected settings.

Direct WhatsApp message sending, Gmail inbox access, contact-based calling, full background hotword ("Hey Kitti" while the app is closed), and automatic reading of private notification/message content require additional official APIs, default-app roles, notification access, foreground-service rules, or user interaction. This project does not bypass those protections.

For a production release, add a backend/AI provider only if desired. The current project uses local routing, so it does not ask for an API key.

## Security

- No hidden screen recording
- No permission bypass
- No private WhatsApp/Gmail database access
- No secret microphone recording
- No remote-control server
- No embedded secret/configuration key

## Low-end devices

The UI uses Jetpack Compose and avoids heavyweight ML models, so it is designed to remain relatively lightweight. Actual speech recognition is provided by Android/device speech services.

## Download website

A simple deploy-ready landing/download page can be placed in `website/`. A public website cannot be hosted by this local project itself; publish that folder to a static hosting service if you want a public download page.
