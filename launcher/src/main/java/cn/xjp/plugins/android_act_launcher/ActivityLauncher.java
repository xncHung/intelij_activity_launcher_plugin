package cn.xjp.plugins.android_act_launcher;

import cn.xjp.plugins.android_act_launcher.adb.Bridge;
import cn.xjp.plugins.android_act_launcher.bean.Rule;
import cn.xjp.plugins.android_act_launcher.rule.AddOrModifyRuleDialog;
import cn.xjp.plugins.android_act_launcher.run.LaunchActivityCommand;
import cn.xjp.plugins.android_act_launcher.storage.RuleConfigService;
import com.android.builder.model.AndroidProject;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.gradle.project.model.AndroidModuleModel;
import com.android.tools.idea.gradle.project.sync.GradleSyncListener;
import com.android.tools.idea.gradle.project.sync.GradleSyncState;
import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ActivityLauncher extends JPanel implements ToolWindowFactory, GradleSyncListener {

    public static final String ID = "ActivityLauncher";
    private JPanel launcherWindowContent;
    private JComboBox<IDevice> devicesBox;
    private JComboBox<Module> moduleBox;
    private JComboBox<String> variantBox;
    private JList<Rule> rulesList;
    private JPanel runActionContainer;
    private JPanel ruleActionContainer;
    private JLabel errorTip;
    private JCheckBox cbStopApp;
    private ToolWindow toolWindow;
    private ModuleManager moduleManager;
    private DefaultListModel<Rule> listModel;
    private Logger logger = Logger.getInstance(ActivityLauncher.class);
    private RuleConfigService configService;

    public ActivityLauncher() {
        super(new BorderLayout());
    }


    // Create the tool window content.
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        moduleManager = ModuleManager.getInstance(project);
        configService = RuleConfigService.getInstance(project);
        logger.error(configService.stopApp);
        add(launcherWindowContent, BorderLayout.CENTER);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(this, "", false);
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
        errorTip.setForeground(JBColor.RED);
        initDeviceBox(project, toolWindow);
        initModuleBox(project, toolWindow);
        initVariantBox(project, toolWindow);
        initRouterList();

        initListeners(project);

        initRuleActionBar();
        initRunActionBar();

        GradleSyncState.subscribe(project, this);
        refreshData(project);
    }

    private void initRunActionBar() {
        DefaultActionGroup group = (DefaultActionGroup) ActionManager.getInstance().getAction("ActivityLauncher.Run.ToolBar");
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, true);
        actionToolbar.setTargetComponent(runActionContainer);
        actionToolbar.setLayoutPolicy(ActionToolbar.WRAP_LAYOUT_POLICY);
        JComponent component = actionToolbar.getComponent();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(5);
        runActionContainer.add(component, flowLayout);
    }

    private void initRuleActionBar() {
        DefaultActionGroup group = (DefaultActionGroup) ActionManager.getInstance().getAction("ActivityLauncher.Rule.ToolBar");
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, true);
        actionToolbar.setTargetComponent(ruleActionContainer);
        actionToolbar.setLayoutPolicy(ActionToolbar.WRAP_LAYOUT_POLICY);
        JComponent component = actionToolbar.getComponent();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(5);
        ruleActionContainer.add(component, flowLayout);
    }

    private void initRouterList() {
        rulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rulesList.setLayoutOrientation(JList.VERTICAL);
        rulesList.setVisibleRowCount(-1);
        listModel = new DefaultListModel<>();
        rulesList.setModel(listModel);
        rulesList.setCellRenderer(new ListCellRendererWrapper<Rule>() {
            @Override
            public void customize(JList jList, Rule rule, int i, boolean b, boolean b1) {
                setText(rule.toString());
            }

        });
        for (Rule item : configService.rules) {
            listModel.addElement(item);
        }
        cbStopApp.setSelected(configService.stopApp);
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

        cbStopApp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                configService.stopApp=cbStopApp.isSelected();
            }
        });
    }

    public void openAddRuleDialog() {
        openRuleDialog(null);
    }

    private void openRuleDialog(Rule selectedValue) {
        AddOrModifyRuleDialog addNewRuleDialog = new AddOrModifyRuleDialog(this, selectedValue);
        addNewRuleDialog.setSize(1000, 500);
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

    public void refreshData(@NotNull Project project) {
        logger.error(configService.stopApp);
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
        refreshData(project);
    }

    @Override
    public void syncFailed(@NotNull Project project, @NotNull String s) {
    }

    @Override
    public void syncSkipped(@NotNull Project project) {

    }

    public void addRule(Rule rule) {
        listModel.add(0, rule);
        configService.addRule(0, rule);
    }

    public void refreshRules(Rule selectedRule) {
        rulesList.updateUI();
    }

    public void removeRule() {
        int selectedValue = rulesList.getSelectedIndex();
        if (selectedValue >= 0) {
            listModel.remove(selectedValue);
            configService.removeRule(selectedValue);
        }
    }

    public void openEditRuleDialog() {
        Rule selectedValue = rulesList.getSelectedValue();
        if (selectedValue == null) {
            return;
        }
        openRuleDialog(selectedValue);
    }

    public void submitRun(Project project, boolean debug) {
        Object selectedItem = devicesBox.getSelectedItem();
        if (!(selectedItem instanceof IDevice)) {
            showError("no Device selected!");
            return;
        }
        Object boxSelectedItem = moduleBox.getSelectedItem();
        if (!(boxSelectedItem instanceof Module)) {
            showError("no Module selected!");
            return;
        }
        Rule selectedRule = getSelectedRule();
        if (selectedRule == null) {
            showError("select a target to launch");
        }
        IDevice device = (IDevice) selectedItem;
        Module module = (Module) boxSelectedItem;

        new LaunchActivityCommand(selectedRule, debug, cbStopApp.isSelected()).apply(project, device, module);
    }

    private void showError(String msg) {
        errorTip.setText(msg);
        new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorTip.setText("");
            }
        }).start();
    }

    public Rule getSelectedRule() {
        return rulesList.getSelectedValue();
    }
}