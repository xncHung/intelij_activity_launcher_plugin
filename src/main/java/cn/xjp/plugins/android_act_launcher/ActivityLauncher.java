package cn.xjp.plugins.android_act_launcher;

import cn.xjp.plugins.android_act_launcher.adb.Bridge;
import com.android.builder.model.AndroidProject;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.gradle.project.model.AndroidModuleModel;
import com.android.tools.idea.gradle.project.sync.GradleSyncListener;
import com.android.tools.idea.gradle.project.sync.GradleSyncState;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class ActivityLauncher implements ToolWindowFactory, GradleSyncListener {

    private final Logger logger;
    private JPanel launcherWindowContent;
    private JComboBox<IDevice> devicesBox;
    private JComboBox<Module> moduleBox;
    private JComboBox<String> variantBox;
    private JList<String> rulesList;
    private JCheckBox reassembleCheckBox;
    private JLabel addNewRule;
    private JLabel removeRule;
    private JLabel debug;
    private JLabel run;
    private JLabel refresh;
    private JPanel start;
    private JLabel editRule;
    private ToolWindow toolWindow;
    private ModuleManager moduleManager;
    private DefaultListModel<String> listModel;


    public ActivityLauncher() {
        logger = Logger.getInstance(ActivityLauncher.class);
    }

    // Create the tool window content.
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        moduleManager = ModuleManager.getInstance(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(launcherWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
//        new FocusWatcher() {
//            @Override
//            protected void focusedComponentChanged(Component component, @Nullable AWTEvent cause) {
//                super.focusedComponentChanged(component, cause);
//                if (component != null && SwingUtilities.isDescendingFrom(component, toolWindow.getComponent())) {
//                    refreshData(project);
//                }
//
//            }
//        }.install(toolWindow.getComponent());

        initDeviceBox(project, toolWindow);
        initModuleBox(project, toolWindow);
        initVariantBox(project, toolWindow);
        initRouterList();

        initListeners(project);
        addNewRule.setIcon(AllIcons.General.Add);
        removeRule.setIcon(AllIcons.General.Remove);
        editRule.setIcon(AllIcons.Actions.Edit);
        refresh.setIcon(AllIcons.Actions.Refresh);
        debug.setIcon(AllIcons.Actions.StartDebugger);
        run.setIcon(AllIcons.Actions.Execute);


        GradleSyncState.subscribe(project, this);
        refreshData(project);
    }

    private void initRouterList() {
        rulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rulesList.setLayoutOrientation(JList.VERTICAL);
        rulesList.setVisibleRowCount(-1);
        listModel = new DefaultListModel<>();
        rulesList.setModel(listModel);
        listModel.addElement("GuideActivity1");
        listModel.addElement("GuideActivity2");
        listModel.addElement("GuideActivity3");
    }

    private void initVariantBox(Project project, ToolWindow toolWindow) {
        variantBox.setRenderer(new ListCellRendererWrapper<String>() {
            @Override
            public void customize(JList list, String value, int index, boolean selected, boolean hasFocus) {
                setText(value);
            }

        });
    }

    private void initListeners(Project project) {
        moduleBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object item = e.getItem();
                if (item instanceof Module) {
                    Module module = (Module) item;
                    refreshVariantBox(module);
                }
            }
        });


        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshData(project);
            }
        });


        addNewRule.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openAddRuleDialog();
            }
        });
    }

    private void openAddRuleDialog() {
        AddNewRuleDialog addNewRuleDialog = new AddNewRuleDialog();
        addNewRuleDialog.setSize(600, 360);
        addNewRuleDialog.setLocationRelativeTo(null);
        addNewRuleDialog.setResizable(false);
        addNewRuleDialog.setVisible(true);
    }

    private void refreshVariantBox(Module module) {
        AndroidModuleModel androidModuleModel = AndroidModuleModel.get(module);
        if (androidModuleModel != null) {
            Collection<String> variantNames = androidModuleModel.getVariantNames();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new Vector<>(variantNames));
            variantBox.setModel(model);
            if (model.getSize() > 0) {
                variantBox.setSelectedIndex(0);
            }
        }
    }

    private void refreshData(@NotNull Project project) {
        IDevice[] deviceList = Bridge.getDeviceList(project);
        devicesBox.setModel(new DefaultComboBoxModel<>(deviceList));

        Module[] modules = moduleManager.getModules();
        Vector<Module> apps = new Vector<>();
        for (Module module : modules) {
            AndroidModuleModel androidModuleModel = AndroidModuleModel.get(module);
            if (androidModuleModel != null && androidModuleModel.getAndroidProject().getProjectType() == AndroidProject.PROJECT_TYPE_APP) {
                apps.add(module);
            }
        }
        DefaultComboBoxModel<Module> aModel = new DefaultComboBoxModel<>(apps);
        moduleBox.setModel(aModel);
        if (aModel.getSize() >= 1) {
            moduleBox.setSelectedIndex(0);
            refreshVariantBox(aModel.getElementAt(0));
        }
    }

    private void initModuleBox(Project project, ToolWindow toolWindow) {
        moduleBox.setRenderer(new ListCellRendererWrapper<Module>() {
            @Override
            public void customize(JList list, Module value, int index, boolean selected, boolean hasFocus) {
                setText(value == null ? "No App Module Found" : value.getName());
            }
        });
    }

    private void initDeviceBox(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        devicesBox.setRenderer(new ListCellRendererWrapper<IDevice>() {
            @Override
            public void customize(JList list, IDevice value, int index, boolean selected, boolean hasFocus) {
                setText(value == null ? "No Device Found" : value.getName());
            }
        });
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public void syncStarted(@NotNull Project project, boolean b, boolean b1) {

    }

    @Override
    public void setupStarted(@NotNull Project project) {

    }

    @Override
    public void syncSucceeded(@NotNull Project project) {
        logger.info("syncSucceeded");
        refreshData(project);
    }

    @Override
    public void syncFailed(@NotNull Project project, @NotNull String s) {
        logger.info("syncFailed");
    }

    @Override
    public void syncSkipped(@NotNull Project project) {

    }
}
