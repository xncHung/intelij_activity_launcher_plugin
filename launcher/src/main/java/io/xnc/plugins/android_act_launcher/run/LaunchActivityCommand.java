package io.xnc.plugins.android_act_launcher.run;

import com.android.builder.model.AndroidArtifact;
import com.android.builder.model.Variant;
import io.xnc.plugins.android_act_launcher.rule.Rule;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.gradle.project.model.AndroidModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

import java.util.concurrent.TimeUnit;

import static io.xnc.plugins.android_act_launcher.util.NotificationUtil.*;

public class LaunchActivityCommand implements Command {
    private final Rule rule;
    private final boolean debug;
    private final boolean clearData;
    private final boolean stopApp;

    public LaunchActivityCommand(Rule rule, boolean debug, boolean clearData, boolean stopApp) {
        this.debug = debug;
        this.clearData = clearData;
        this.rule = rule;
        this.stopApp = stopApp;
    }


    @Override
    public boolean apply(Project project, IDevice device, Module module, String variantName) {
        AndroidModuleModel model = AndroidModuleModel.get(module);
        if (model == null) {
            error("Not Android Module!");
            return false;
        }
        if (device == null) {
            error("can't connect Device");
            return false;
        }
        Variant variant = model.findVariantByName(variantName);
        if (variant == null || variant.getMainArtifact() == null) {
            error("error Variant");
            return false;
        }
        AndroidArtifact artifact = variant.getMainArtifact();
        String applicationId = artifact.getApplicationId();
        try {
            ShellReceiver receiver = new ShellReceiver();
            if (clearData) {
                ShellReceiver clearShellOutputReceiver = new ShellReceiver();
                device.executeShellCommand(ShellCmdFactory.createClearDataCmd(applicationId), clearShellOutputReceiver, 15, TimeUnit.SECONDS);
                if (clearShellOutputReceiver.isSuccess()) {
                    info(" Clear Data Success!");
                } else {
                    error(rule.getName() + " Clear Data Error!" + clearShellOutputReceiver.getErrorMsg());
                }
            }
            device.executeShellCommand(ShellCmdFactory.createActivityLaunchShellCmd(applicationId, rule, debug, stopApp), receiver, 15, TimeUnit.SECONDS);
            if (debug) {
                DebugUtil.attachDebugger(project, device, applicationId);
            }
            if (receiver.isSuccess()) {
                info(rule.getName() + " Launch Success!");
                return true;
            } else {
                error(rule.getName() + " Launch Error!" + receiver.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
