package cn.xjp.plugins.android_act_launcher.actions;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

public class ReLoadAction extends BaseAction {

    @Override
    protected void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        activityLauncher.refreshData(project);
    }


}
