package cn.xjp.plugins.android_act_launcher.util;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ToolWindowUtil {
    @Nullable
    public static ActivityLauncher getLauncherToolWindow(@NotNull Project project) {
        ActivityLauncher result = null;
        ToolWindow toolWindow = getToolWindow(project);
        if (toolWindow != null) {
            result = getLauncherToolWindow(toolWindow);
        }
        return result;
    }

    @Nullable
    private static ActivityLauncher getLauncherToolWindow(@NotNull ToolWindow toolWindow) {
        ActivityLauncher result = null;
        Content[] contents = toolWindow.getContentManager().getContents();
        if (contents.length > 0) {
            JComponent component = contents[0].getComponent();
            if (component instanceof ActivityLauncher) {
                result = (ActivityLauncher) component;
            }
        }
        return result;
    }


    @Nullable
    private static ToolWindow getToolWindow(@NotNull Project project) {
        ToolWindowManager instance = ToolWindowManager.getInstance(project);
        if (instance == null) {
            return null;
        }
        return instance.getToolWindow(ActivityLauncher.ID);
    }
}
