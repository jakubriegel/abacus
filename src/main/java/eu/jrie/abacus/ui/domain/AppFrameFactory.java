package eu.jrie.abacus.ui.domain;

import eu.jrie.abacus.core.domain.cell.CellManager;
import eu.jrie.abacus.core.domain.cell.style.CellStyleManager;
import eu.jrie.abacus.core.domain.formula.FormulaManager;
import eu.jrie.abacus.core.domain.workbench.Workbench;
import eu.jrie.abacus.core.domain.workbench.WorkbenchContext;
import eu.jrie.abacus.ui.domain.components.factory.ComponentFactory;
import eu.jrie.abacus.ui.domain.components.space.Space;
import eu.jrie.abacus.ui.domain.components.space.UtilsMenu;
import eu.jrie.abacus.ui.domain.components.space.workbench.CellStyleRenderer;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchScroll;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchTable;
import eu.jrie.abacus.ui.domain.components.space.workbench.WorkbenchTableModel;
import eu.jrie.abacus.ui.domain.components.toolbar.LogoLabel;
import eu.jrie.abacus.ui.domain.components.toolbar.TextTools;
import eu.jrie.abacus.ui.domain.components.toolbar.Toolbar;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellAddress;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditor;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.CellEditorField;
import eu.jrie.abacus.ui.domain.components.toolbar.editor.Symbol;
import eu.jrie.abacus.ui.domain.workbench.WorkbenchFacade;
import eu.jrie.abacus.ui.infra.ResourcesProvider;
import eu.jrie.abacus.ui.infra.event.EventBus;

import static eu.jrie.abacus.core.domain.formula.Formulas.buildFormulas;
import static eu.jrie.abacus.lang.domain.parser.ParserFactory.buildParser;

public abstract class AppFrameFactory {

    private static final CellStyleManager cellStyleManager = new CellStyleManager();
    private static final Workbench workbench = buildWorkbench();
    private static final EventBus eventBus = new EventBus();
    private static final WorkbenchFacade workbenchFacade = new WorkbenchFacade(workbench, eventBus);
    private static final ResourcesProvider resourcesProvider = new ResourcesProvider();

    private AppFrameFactory() {}

    public static AppFrame buildAppFrame() {
        var toolbar = buildToolbar();
        var space = buildSpace();
        return new AppFrame(resourcesProvider, toolbar, space);
    }

    private static Workbench buildWorkbench() {
        var cellManager = new CellManager();
        var formulas = buildFormulas();
        var formulaManager = new FormulaManager(formulas);
        var parser = buildParser(new WorkbenchContext(cellManager, formulaManager));
        return new Workbench(cellManager, cellStyleManager, parser);
    }

    private static Toolbar buildToolbar() {
        var logoLabel = new LogoLabel();
        var cellEditor = buildCellEditor();
        var componentFactory = new ComponentFactory();
        var textTools = new TextTools(eventBus, componentFactory);
        return new Toolbar(logoLabel, cellEditor, textTools);
    }

    private static CellEditor buildCellEditor() {
        var symbol = new Symbol();
        var address = new CellAddress();
        var cellEditorField = new CellEditorField();
        return new CellEditor(resourcesProvider, symbol, address, cellEditorField, eventBus, workbenchFacade);
    }

    private static Space buildSpace() {
        var menu = new UtilsMenu();
        var workbenchTableModel = new WorkbenchTableModel();
        var cellStyleRenderer = new CellStyleRenderer(cellStyleManager);
        var workbenchTable = new WorkbenchTable(workbenchFacade, workbenchTableModel, eventBus, cellStyleRenderer);
        var workbenchScroll = new WorkbenchScroll(workbenchTable);
        return new Space(menu, workbenchScroll);
    }
}
