package cn.xjp.plugins.android_act_launcher.run;

import cn.xjp.plugins.android_act_launcher.bean.Rule;
import cn.xjp.plugins.android_act_launcher.util.Base64Util;
import com.google.gson.Gson;

class ShellCmdFactory {
    private static final String ANCHOR_ACTIVITY = "cn.xnc.intellij.plugin.activityanchor.AnchorActivity";
    private static final String TARGET_ACTIVITY = "target_activity";
    private static final String PARAMS_ARRAY = "params_array";

    static String createActivityLaunchShellCmd(String applicationId, Rule rule, boolean debug, boolean stopApp) {
        return "am start " + (debug ? " -D " : "") + (stopApp ? " -S " : "") + " -n " + applicationId + "/" + ANCHOR_ACTIVITY +
                " -a android.intent.action.MAIN -c android.intent.category.LAUNCHER "
                + " --es " + TARGET_ACTIVITY + " " + rule.getTarget()
                + " --es " + PARAMS_ARRAY + " " + Base64Util.encode(new Gson().toJson(rule.getParams()));
    }

}