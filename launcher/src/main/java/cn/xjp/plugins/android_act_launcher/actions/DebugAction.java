package cn.xjp.plugins.android_act_launcher.actions;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

public class DebugAction extends BaseAction {

    @Override
    protected void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent) {
        activityLauncher.submitRun(anActionEvent.getProject(), true);
    }

    @Override
    public void update(AnActionEvent e) {
        ActivityLauncher activityLauncher = getActivityLauncher(e);
        e.getPresentation().setEnabled(
                activityLauncher != null
                        && activityLauncher.getSelectedRule() != null
                        && activityLauncher.getSelectedDevice() != null
                        && activityLauncher.getSelectedModule() != null
                        && activityLauncher.getSelectedVariant() != null
        );
    }
}
