package io.xnc.plugins.android_act_launcher.run;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.run.editor.AndroidDebugger;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import java.util.Timer;
import java.util.TimerTask;

class Debugger {

    private final Project project;
    private final IDevice device;
    private final String applicationId;
    private int count = 200;

    Debugger(Project project, IDevice device, String applicationId) {
        this.project = project;
        this.device = device;
        this.applicationId = applicationId;
    }

    void attach() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (DebugUtil.debuggerPrepared(device, applicationId)) {
                        AndroidDebugger debugger = DebugUtil.getDebugger(project);
                        if (debugger != null) {
                            timer.cancel();
                            ApplicationManager.getApplication().invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Client client = device.getClient(applicationId);
                                    DebugUtil.closeSessionsIfNecessary(project, client);
                                    debugger.attachToClient(project, client);
                                }
                            });
                        }
                    }
                    count--;
                    if (count == 0) {
                        timer.cancel();
                    }
                } catch (Exception e) {
                    timer.cancel();
                }
            }
        }, 0, 150);

    }
}
