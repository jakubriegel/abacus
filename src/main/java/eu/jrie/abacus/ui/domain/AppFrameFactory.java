package eu.jrie.abacus.ui.domain;

import eu.jrie.abacus.core.domain.workbench.Workbench;
import eu.jrie.abacus.ui.domain.components.space.Space;
import eu.jrie.abacus.ui.domain.components.space.UtilsMenu;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchScroll;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchTable;
import eu.jrie.abacus.ui.domain.components.toolbar.CellEditor;
import eu.jrie.abacus.ui.domain.components.toolbar.LogoLabel;
import eu.jrie.abacus.ui.domain.components.toolbar.TextTools;
import eu.jrie.abacus.ui.domain.components.toolbar.Toolbar;
import eu.jrie.abacus.ui.infra.ResourcesProvider;

public abstract class AppFrameFactory {

    private AppFrameFactory() {}

    public static AppFrame buildAppFrame() {
        var resourcesProvider = new ResourcesProvider();
        var toolbar = buildToolbar();
        var space = buildSpace();
        return new AppFrame(resourcesProvider, toolbar, space);
    }

    private static Toolbar buildToolbar() {
        var logoLabel = new LogoLabel();
        var cellEditor = new CellEditor();
        var textTools = new TextTools();
        return new Toolbar(logoLabel, cellEditor, textTools);
    }

    private static Space buildSpace() {
        var menu = new UtilsMenu();
        var workbench = new Workbench();
        var workbenchTable = new WorkbenchTable(workbench);
        var workbenchScroll = new WorkbenchScroll(workbenchTable);
        return new Space(menu, workbenchScroll);
    }
}
