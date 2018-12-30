package io.xnc.plugins.android_act_launcher.run;

import io.xnc.plugins.android_act_launcher.rule.Rule;
import io.xnc.plugins.android_act_launcher.util.Base64Util;
import com.google.gson.Gson;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


class ShellCmdFactory {
    private static final String ANCHOR_ACTIVITY = "io.xnc.intellij.plugin.activityanchor.Anchor";
    private static final String TARGET_ACTIVITY = "target_activity";
    private static final String PARAMS_ARRAY = "params_array";

    @NotNull
    static String createActivityLaunchShellCmd(String applicationId, @NotNull Rule rule, boolean debug, boolean stopApp) {
        return "am start " + (debug ? " -D " : "") + (stopApp ? " -S " : "") + " -n " + applicationId + "/" + ANCHOR_ACTIVITY
//                + " -a android.intent.action.MAIN -c android.intent.category.LAUNCHER "
                + " --es " + TARGET_ACTIVITY + " " + rule.getTarget()
                + " --es " + PARAMS_ARRAY + " " + Base64Util.encode(new Gson().toJson(rule.getActiveParams()));
    }

    @NotNull
    @Contract(pure = true)
    static String createClearDataCmd(String applicationId) {
        return "pm clear " + applicationId;
    }

}
