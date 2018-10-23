package cn.xjp.plugins.android_act_launcher.actions;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class RemoveRuleAction extends BaseAction {

    @Override
    protected void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent) {
        activityLauncher.removeRule();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        ActivityLauncher activityLauncher = getActivityLauncher(e);
        e.getPresentation().setEnabled(activityLauncher != null && activityLauncher.getSelectedRule() != null);
    }
}
