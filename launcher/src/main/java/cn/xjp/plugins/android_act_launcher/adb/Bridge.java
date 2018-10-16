package cn.xjp.plugins.android_act_launcher.adb;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.intellij.openapi.project.Project;
import org.jetbrains.android.sdk.AndroidSdkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bridge {


    public static IDevice[] getDeviceList(Project project) {
        AndroidDebugBridge debugBridge = AndroidSdkUtils.getDebugBridge(project);
        if (debugBridge != null) {
            IDevice[] devices = debugBridge.getDevices();
            return devices ==null?new IDevice[0]: devices;
        }
        return new IDevice[0];
    }
}
