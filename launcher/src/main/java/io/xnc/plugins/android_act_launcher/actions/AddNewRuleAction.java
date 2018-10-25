package io.xnc.plugins.android_act_launcher.actions;

import io.xnc.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class AddNewRuleAction extends BaseAction {

    @Override
    protected void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent) {
        activityLauncher.openAddRuleDialog(anActionEvent.getProject());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        ActivityLauncher activityLauncher = getActivityLauncher(e);
        e.getPresentation().setEnabled(
                activityLauncher != null
                        && activityLauncher.getSelectedModule() != null
        );
    }
}
