package io.xnc.plugins.android_act_launcher.run;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.run.AndroidProcessHandler;
import com.android.tools.idea.run.editor.AndroidDebugger;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.project.Project;

class DebugUtil {
    static void attachDebugger(Project project, IDevice device, String applicationId) {
        new Debugger(project, device, applicationId).attach();
    }

    static boolean debuggerPrepared(IDevice device, String applicationId) {
        return AndroidDebugger.EP_NAME.getExtensions().length != 0 && device.getClient(applicationId) != null;
    }

    static AndroidDebugger getDebugger(Project project) {
        AndroidDebugger[] extensions = AndroidDebugger.EP_NAME.getExtensions();
        for (AndroidDebugger debugger : extensions) {
            if (debugger.supportsProject(project)) {
                return debugger;
            }
        }
        return null;
    }

    static void closeSessionsIfNecessary(Project project, Client client) {
        int pid = client.getClientData().getPid();
        for (ProcessHandler handler : ExecutionManager.getInstance(project).getRunningProcesses()) {
            if (handler instanceof AndroidProcessHandler) {
                Client handlerClient = ((AndroidProcessHandler) handler).getClient(client.getDevice());
                if (handlerClient != null && handlerClient.getClientData().getPid() == pid) {
                    ((AndroidProcessHandler) handler).setNoKill();
                    handler.detachProcess();
                    handler.notifyTextAvailable("Disconnecting run session: because a new debug session will be attached...", ProcessOutputTypes.STDOUT);
                    break;
                }
            }
        }
    }
}
