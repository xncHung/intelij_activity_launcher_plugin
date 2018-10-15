package cn.xjp.plugins.android_act_launcher.actions;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import cn.xjp.plugins.android_act_launcher.util.ToolWindowUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.Nullable;

public class AddNewRuleAction extends BaseAction {

    @Override
    protected void handleEvent(ActivityLauncher activityLauncher, AnActionEvent anActionEvent) {
        activityLauncher.openAddRuleDialog();
    }

}
