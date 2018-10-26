package io.xnc.plugins.android_act_launcher.run;

import com.android.ddmlib.IDevice;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;

public interface Command {
    boolean apply(Project project, IDevice device, Module module, String s);
}
