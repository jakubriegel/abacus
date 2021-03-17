package eu.jrie.abacus.lang.domain.parser;

import eu.jrie.abacus.core.domain.expression.Formula;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.lang.domain.exception.InvalidInputException;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ParserTest {

    private final FormulaParser formulaParser = mock(FormulaParser.class);
    private final ValueParser valueParser = mock(ValueParser.class);

    private final Parser parser = new Parser(valueParser, formulaParser);

    @Test
    void shouldParseFormula() throws InvalidInputException {
        // given
        var rawCellText = "=text ";
        var cellText = "=text";
        var formulaText = "text";
        var expectedFormula = new Formula("name", emptyList(), () -> null);

        // and
        when(formulaParser.parse(formulaText)).thenReturn(expectedFormula);

        // when
        var result = parser.parse(rawCellText);

        // then
        verify(formulaParser).parse(formulaText);
        verifyNoInteractions(valueParser);
        assertEquals(expectedFormula, result);

    }

    @Test
    void shouldParseValue() throws InvalidInputException {
        // given
        var rawCellText = " text";
        var cellText = "text";
        var expectedValue = new TextValue(cellText);

        // and
        when(valueParser.parse(cellText)).thenReturn(expectedValue);

        // when
        var result = parser.parse(rawCellText);

        // then
        verify(valueParser).parse(cellText);
        verifyNoInteractions(formulaParser);
        assertEquals(expectedValue, result);

    }
}
