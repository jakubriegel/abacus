package eu.jrie.abacus.core.domain.workbench;

import eu.jrie.abacus.core.domain.cell.Cell;
import eu.jrie.abacus.core.domain.cell.CellManager;
import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.cell.style.CellStyle;
import eu.jrie.abacus.core.domain.cell.style.CellStyleManager;
import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import eu.jrie.abacus.lang.domain.exception.formula.UnknownSyntaxException;
import eu.jrie.abacus.lang.domain.parser.Parser;
import org.junit.jupiter.api.Test;

import static eu.jrie.abacus.core.domain.cell.style.CellTextAlignment.LEFT;
import static eu.jrie.abacus.core.domain.cell.style.CellTextPosition.MIDDLE;
import static java.awt.Color.red;
import static java.awt.Color.white;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class WorkbenchTest {

    private static final Position POSITION = new Position(1, 2);
    private static final Cell CELL = new Cell(POSITION);
    private static final CellStyle STYLE = new CellStyle(10f, true, false, false, red, white, LEFT, MIDDLE);

    private final CellManager cellManager = mock(CellManager.class);
    private final CellStyleManager cellStyleManager = mock(CellStyleManager.class);
    private final Parser parser = mock(Parser.class);

    private final Workbench workbench = new Workbench(cellManager, cellStyleManager, parser);

    @Test
    void shouldGetCell() {
        // given
        when(cellManager.getCell(POSITION)).thenReturn(CELL);

        // when
        var result = workbench.getCell(POSITION);

        // then
        verify(cellManager, only()).getCell(POSITION);
        verifyNoInteractions(cellStyleManager, parser);
        assertEquals(CELL, result);
    }

    @Test
    void shouldSetCellTextAndUpdateValue() throws CellReadException, FormulaExecutionException, InvalidInputException {
        // given
        var text = "newText";
        var value = new TextValue(text);

        // and
        when(cellManager.getCell(POSITION)).thenReturn(CELL);
        when(parser.parse(text)).thenReturn(value);

        // when
        workbench.setTextAt(POSITION, text);

        // then
        verify(cellManager, only()).getCell(POSITION);
        verify(parser, only()).parse(text);
        verifyNoInteractions(cellStyleManager);

        // and
        assertEquals(CELL, new Cell(POSITION, null, text, value));
    }

    @Test
    void shouldSetCellTextAndUpdateFormula() throws CellReadException, FormulaExecutionException, InvalidInputException {
        // given
        var formulaText = "formulaText";
        var text = "newText";
        var value = new TextValue(text);
        var formula = new Formula("formula", emptyList(), () -> value);

        // and
        when(cellManager.getCell(POSITION)).thenReturn(CELL);
        when(parser.parse(formulaText)).thenReturn(formula);

        // when
        workbench.setTextAt(POSITION, formulaText);

        // then
        verify(cellManager, only()).getCell(POSITION);
        verify(parser, only()).parse(formulaText);
        verifyNoInteractions(cellStyleManager);

        // and
        assertEquals(CELL, new Cell(POSITION, formula, text, value));
    }

    @Test
    void shouldSetPlaceholderForParsingException() throws InvalidInputException {
        // given
        var formulaText = "formulaText";
        var placeholder = "Invalid syntax.";
        var value = new TextValue(placeholder);
        var formula = new Formula("formula", emptyList(), () -> value);

        // and
        when(cellManager.getCell(POSITION)).thenReturn(CELL);
        when(parser.parse(formulaText)).thenThrow(new UnknownSyntaxException());

        // when
        assertThrows(CellReadException.class, () -> workbench.setTextAt(POSITION, formulaText));

        // then
        verify(cellManager, only()).getCell(POSITION);
        verify(parser, only()).parse(formulaText);
        verifyNoInteractions(cellStyleManager);

        // and
        assertEquals(CELL, new Cell(POSITION, formula, placeholder, value));
    }

    @Test
    void shouldSetPlaceholderForGeneralException() throws InvalidInputException {
        // given
        var formulaText = "formulaText";
        var placeholder = "ERROR";
        var value = new TextValue(placeholder);
        var formula = new Formula("formula", emptyList(), () -> value);

        // and
        when(cellManager.getCell(POSITION)).thenReturn(CELL);
        when(parser.parse(formulaText)).thenThrow(new IllegalStateException());

        // when
        assertThrows(CellReadException.class, () -> workbench.setTextAt(POSITION, formulaText));

        // then
        verify(cellManager, only()).getCell(POSITION);
        verify(parser, only()).parse(formulaText);
        verifyNoInteractions(cellStyleManager);

        // and
        assertEquals(CELL, new Cell(POSITION, formula, placeholder, value));
    }

    @Test
    void shouldSetPlaceholderForFormulaException() throws InvalidInputException {
        // given
        var formulaText = "formulaText";
        var placeholder = "Formula Error";
        var value = new TextValue(placeholder);
        var formula = new Formula("formula", emptyList(), () -> {
            throw new IllegalStateException();
        });

        // and
        when(cellManager.getCell(POSITION)).thenReturn(CELL);
        when(parser.parse(formulaText)).thenReturn(formula);

        // when
        assertThrows(FormulaExecutionException.class, () -> workbench.setTextAt(POSITION, formulaText));

        // then
        verify(cellManager, only()).getCell(POSITION);
        verify(parser, only()).parse(formulaText);
        verifyNoInteractions(cellStyleManager);

        // and
        assertEquals(CELL, new Cell(POSITION, formula, placeholder, value));
    }

    @Test
    void shouldGetCellStyle() {
        // given
        when(cellStyleManager.getStyle(POSITION)).thenReturn(STYLE);

        // when
        var result = workbench.getCellStyle(POSITION);

        // then
        verify(cellStyleManager, only()).getStyle(POSITION);
        verifyNoInteractions(cellManager, parser);
        assertEquals(STYLE, result);
    }

    @Test
    void shouldSetCellStyle() {
        // given

        doNothing().when(cellStyleManager).setStyle(POSITION, STYLE);

        // when
        workbench.setCellStyle(POSITION, STYLE);

        // then
        verify(cellStyleManager, only()).setStyle(POSITION, STYLE);
        verifyNoInteractions(cellManager, parser);
    }

    @Test
    void shouldSetDefaultCellStyle() {
        // given

        doNothing().when(cellStyleManager).setDefaultStyle(POSITION);

        // when
        workbench.setDefaultCellStyle(POSITION);

        // then
        verify(cellStyleManager, only()).setDefaultStyle(POSITION);
        verifyNoInteractions(cellManager, parser);
    }
}