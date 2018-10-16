package cn.xjp.plugins.android_act_launcher.run;

import cn.xjp.plugins.android_act_launcher.bean.Rule;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.gradle.project.model.AndroidModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

import java.util.concurrent.TimeUnit;

import static cn.xjp.plugins.android_act_launcher.util.NotificationUtil.*;

public class LaunchActivityCommand implements Command {
    private final Rule rule;
    private final boolean debug;
    private final boolean stopApp;

    public LaunchActivityCommand(Rule rule, boolean debug, boolean stopApp) {
        this.debug = debug;
        this.stopApp = stopApp;
        this.rule = rule;
    }


    @Override
    public boolean apply(Project project, IDevice device, Module module) {
        AndroidModuleModel model = AndroidModuleModel.get(module);
        if (model == null) {
            error("Not Android Module!");
            return false;
        }
        if (device == null) {
            error("can't connect Device");
            return false;
        }
        String applicationId = model.getApplicationId();
        try {
            ShellReceiver receiver = new ShellReceiver();
            device.executeShellCommand(ShellCmdFactory.createActivityLaunchShellCmd(applicationId, rule, debug, stopApp), receiver, 15, TimeUnit.SECONDS);
            if (debug) {
                DebugUtil.attachDebugger(project, device, applicationId);
            }
            if (receiver.isSuccess()) {
                info("Launch Success!");
            } else {
                error("Launch Error!" + receiver.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
