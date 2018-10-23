package cn.xjp.plugins.android_act_launcher.actions;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import cn.xjp.plugins.android_act_launcher.util.ToolWindowUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public abstract class BaseAction extends DumbAwareAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        ActivityLauncher activityLauncher = getActivityLauncher(anActionEvent);
        if (activityLauncher == null) return;
        handleEvent(activityLauncher,anActionEvent);
    }

    protected abstract void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent);

    @Nullable
    ActivityLauncher getActivityLauncher(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return null;
        }
        ActivityLauncher activityLauncher = ToolWindowUtil.getLauncherToolWindow(project);
        if (activityLauncher == null) {
            return null;
        }
        return activityLauncher;
    }
}
