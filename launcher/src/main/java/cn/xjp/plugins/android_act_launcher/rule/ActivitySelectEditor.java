package cn.xjp.plugins.android_act_launcher.rule;

import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ActionListener;

public class ActivitySelectEditor extends ComponentWithBrowseButton<EditorTextField> {
    public ActivitySelectEditor(@NotNull EditorTextField component, @Nullable ActionListener browseActionListener) {
        super(component, browseActionListener);
    }
}
