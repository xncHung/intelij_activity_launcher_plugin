package io.xnc.plugins.android_act_launcher.util;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;

public class NotificationUtil {
    private static final NotificationGroup INFO = NotificationGroup.logOnlyGroup("ActivityLauncherInfo");
    private static final NotificationGroup WARNING = NotificationGroup.logOnlyGroup("ActivityLauncherWaring");
    private static final NotificationGroup ERRORS = NotificationGroup.balloonGroup("ActivityLauncherError");

    public static void info(String message) {
        sendNotification(message, NotificationType.INFORMATION, INFO);
    }

    public static void error(String message) {
        sendNotification(message, NotificationType.ERROR, ERRORS);
    }

    public static void warning(String message) {
        sendNotification(message, NotificationType.WARNING, ERRORS);
    }

    private static void sendNotification(String message, NotificationType notificationType, NotificationGroup notificationGroup) {
        notificationGroup.createNotification("ActivityLauncher", message, notificationType, null).notify(null);
    }


}
