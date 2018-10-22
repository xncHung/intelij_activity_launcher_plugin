package cn.xjp.plugins.android_act_launcher.actions;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class StartAction extends BaseAction {

    @Override
    protected void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent) {
        activityLauncher.submitRun(anActionEvent.getProject(), false);
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
