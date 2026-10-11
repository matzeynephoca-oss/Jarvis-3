package com.example.jarvis2

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class JarvisNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != "com.whatsapp") return

        val e = sbn.notification.extras
        val sender = e.getString("android.title") ?: return
        val text = e.getCharSequence("android.text")?.toString() ?: return

        getSharedPreferences("jarvis2", MODE_PRIVATE).edit()
            .putString("sender", sender)
            .putString("text", text)
            .apply()
    }
}
