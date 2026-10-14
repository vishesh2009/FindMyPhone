# 📱 FindMyPhone

**FindMyPhone** is a native Android application built with Kotlin that helps users locate a misplaced phone by triggering an audible alarm using a predefined secret command.

The app supports traditional SMS messages and notification-based detection of RCS messages through Google Messages.

## ✨ Features

- **SMS Command Detection:** Recognizes a predefined command received through traditional SMS.
- **RCS Notification Detection:** Monitors Google Messages notifications for the configured command.
- **Remote Alarm Trigger:** Starts an audible alarm when a valid command is detected.
- **Locked-Screen Support:** Successfully tested with the device locked.
- **Manual Alarm Controls:** Start and stop the alarm directly from the application.
- **Command Validation:** Ignores messages that do not match the configured command.
- **Foreground Service:** Uses an Android foreground service for alarm playback.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Application development |
| Android SDK | Native Android functionality |
| Android Studio | Development environment |
| BroadcastReceiver | Incoming SMS detection |
| NotificationListenerService | Google Messages notification detection |
| Foreground Service | Alarm playback |
| Git & GitHub | Version control |

## ⚙️ How It Works

### SMS Detection

1. The device receives an SMS.
2. `SmsReceiver` processes the incoming message.
3. `CommandHandler` validates the message against the configured command.
4. If the command matches, `AlarmService` attempts to start the alarm.

### RCS Detection

1. Google Messages receives a message and posts a notification.
2. `RcsNotificationListener` examines the notification.
3. The extracted text is checked by `CommandHandler`.
4. If the command matches, the app attempts to trigger the alarm.

**Note:** RCS support relies on accessible Google Messages notifications. It is not direct access to the RCS protocol and may not work for muted conversations, hidden notification content, or messages that do not generate notifications.

## 📂 Project Structure

```text
app/src/main/
├── AndroidManifest.xml
└── java/com/example/findmyphone/
    ├── MainActivity.kt
    ├── SmsReceiver.kt
    ├── RcsNotificationListener.kt
    ├── CommandHandler.kt
    ├── AlarmService.kt
    └── AlarmNotification.kt
```

## 🚀 Getting Started

### Prerequisites

- Android Studio
- An Android device running Android 8.0 or later
- SMS capability for SMS-based detection
- Google Messages for the implemented RCS notification detection

### Installation

1. Clone the repository:

```bash
git clone https://github.com/vishesh2009/FindMyPhone.git
```

2. Open the project in Android Studio.
3. Configure a private command in `CommandHandler.kt` and the corresponding test functionality in `MainActivity.kt`.
4. Build and install the application.
5. Grant the required SMS permission.
6. For RCS notification detection, explicitly enable notification access for FindMyPhone in Android settings.

**Security warning:** Do not commit a real secret command to a public repository. The current implementation uses a hardcoded command for validation and is intended as a personal experimental project.

## 🔐 Permissions

| Permission or access | Purpose |
|---|---|
| `RECEIVE_SMS` | Detect incoming traditional SMS messages |
| `POST_NOTIFICATIONS` | Display application notifications |
| `FOREGROUND_SERVICE` | Support foreground alarm operation |
| `FOREGROUND_SERVICE_MEDIA_PLAYBACK` | Support media playback in the foreground service |
| Notification access | Examine Google Messages notifications for the configured command |

Notification access is sensitive because Android grants visibility into notifications beyond Google Messages. The listener filters for Google Messages in application code.

## 🧪 Testing

The application has been tested on a OnePlus 11R running Android 16.

Confirmed functionality during development:

- Manual alarm activation
- Manual alarm stopping
- Command validation using the built-in test button
- Incoming SMS-triggered alarm
- RCS notification-triggered alarm
- Alarm activation while the device is locked

Testing on one device does not guarantee identical behavior across Android versions or manufacturers.

## ⚠️ Limitations

- RCS detection requires Google Messages to generate an accessible notification.
- Android background execution restrictions and battery optimizations may affect reliability.
- Notification access must be granted manually by the user.
- The application is not a replacement for Google's official device-finding services.
- The hardcoded command approach is not suitable for high-security deployment.

## 🔮 Future Improvements

- Secure in-app command configuration
- Removal of hardcoded secrets
- Improved background reliability
- Better duplicate-message handling
- Expanded compatibility testing
- Enhanced application interface

## 👨‍💻 Author

**Vishesh Khanna**

GitHub: [@vishesh2009](https://github.com/vishesh2009)

## 📄 Project Status

**Version 1.0: Functional prototype**

Developed as a personal Android development project to explore SMS broadcasts, notification listeners, foreground services, and Android background execution.
