package cn.xjp.plugins.android_act_launcher.util;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;

public class NotificationUtil {
    private static final NotificationGroup INFO = NotificationGroup.logOnlyGroup("ActivityLauncherInfo");
    private static final NotificationGroup ERRORS = NotificationGroup.balloonGroup("ActivityLauncherError");

    public static void info(String message) {
        sendNotification(message, NotificationType.INFORMATION, INFO);
    }

    public static void error(String message) {
        sendNotification(message, NotificationType.ERROR, ERRORS);
    }

    private static void sendNotification(String message, NotificationType notificationType, NotificationGroup notificationGroup) {
        notificationGroup.createNotification("ActivityLauncher", message, notificationType, null).notify(null);
    }



}
