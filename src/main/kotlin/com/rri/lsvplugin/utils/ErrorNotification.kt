package com.rri.lsvplugin.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project


class ErrorNotification {
    companion object {
        fun notifyError(project: Project?, content: String) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Error Notification Group")
                .createNotification(content, NotificationType.ERROR)
                .notify(project)
        }
    }
}