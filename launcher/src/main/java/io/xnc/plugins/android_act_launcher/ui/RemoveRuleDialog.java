package io.xnc.plugins.android_act_launcher.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RemoveRuleDialog extends DialogWrapper {
    private final CallBack callBack;
    private JPanel rootContent;

    public RemoveRuleDialog(@Nullable Project project, CallBack callBack) {
        super(project, false);
        this.callBack = callBack;
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return rootContent;
    }

    @Override
    protected void doOKAction() {
        if (callBack != null) {
            callBack.onOKAction();
        }
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        if (callBack != null) {
            callBack.onCancelAction();
        }
        super.doCancelAction();
    }

    public interface CallBack {

        void onCancelAction();

        void onOKAction();
    }

}
