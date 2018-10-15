package cn.xjp.plugins.android_act_launcher.actions;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class EditRuleAction extends BaseAction {

    @Override
    protected void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent) {
        activityLauncher.openEditRuleDialog();
    }

    @Override
    public void update(AnActionEvent e) {
        ActivityLauncher activityLauncher = getActivityLauncher(e);
        e.getPresentation().setEnabled(activityLauncher != null && activityLauncher.getSelectedRule() != null);
    }
}
