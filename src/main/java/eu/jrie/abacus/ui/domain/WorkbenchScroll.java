package eu.jrie.abacus.ui.domain;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class WorkbenchScroll extends JScrollPane {

    private final WorkbenchTable workbenchTable;

    WorkbenchScroll(WorkbenchTable workbenchTable) {
        super(workbenchTable, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        this.workbenchTable = workbenchTable;
        createHorizontalScrollBar();
        getViewport().addChangeListener(new ScrollEndListener(this));
    }

    private class ScrollEndListener implements ChangeListener {

        private final JScrollPane scroll;

        ScrollEndListener(JScrollPane scroll) {
            this.scroll = scroll;
        }

        public void stateChanged(ChangeEvent e){
            handleScrollEnd(scroll.getVerticalScrollBar(), workbenchTable::newRow);
            handleScrollEnd(scroll.getHorizontalScrollBar(), workbenchTable::newColumn);
        }

        private void handleScrollEnd(JScrollBar bar, Runnable action) {
            int value = bar.getValue();
            int extent = bar.getModel().getExtent();
            int maximum = bar.getMaximum();
            if (value + extent == maximum) {
                action.run();
            }
        }
    }
}
