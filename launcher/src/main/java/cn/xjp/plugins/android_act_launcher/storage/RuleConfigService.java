package cn.xjp.plugins.android_act_launcher.storage;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import cn.xjp.plugins.android_act_launcher.bean.Rule;
import com.google.gson.Gson;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(name = "ActivityLauncherRules",
        storages = {@Storage("ActivityLauncherRules.xml")})
public class RuleConfigService implements PersistentStateComponent<RuleConfigService> {
    public List<Rule> rules = new ArrayList<>();
    public boolean stopApp = true;
    private Logger logger = Logger.getInstance(ActivityLauncher.class);

    @Nullable
    @Override
    public RuleConfigService getState() {
        logger.error("getState");
        return this;
    }

    @Override
    public void loadState(@NotNull RuleConfigService state) {
        logger.error(new Gson().toJson(state));
        XmlSerializerUtil.copyBean(state, this);
    }


    public static RuleConfigService getInstance(Project project) {
        return ServiceManager.getService(project, RuleConfigService.class);
    }

    public void addRule(int i, Rule rule) {
        rules.add(i, rule);
    }

    public void removeRule(int selectedValue) {
        rules.remove(selectedValue);
    }
}
