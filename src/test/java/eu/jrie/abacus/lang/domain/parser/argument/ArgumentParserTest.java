package eu.jrie.abacus.lang.domain.parser.argument;

import eu.jrie.abacus.core.domain.expression.LogicValue;
import eu.jrie.abacus.core.domain.expression.NumberValue;
import eu.jrie.abacus.core.domain.expression.TextValue;
import eu.jrie.abacus.core.domain.expression.Value;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.domain.formula.FormulaImplementation;
import eu.jrie.abacus.lang.domain.exception.InvalidArgumentNumberException;
import eu.jrie.abacus.lang.domain.exception.InvalidArgumentTypeException;
import eu.jrie.abacus.lang.domain.grammar.TokenMatch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static eu.jrie.abacus.lang.domain.grammar.Token.CELL_REFERENCE;
import static eu.jrie.abacus.lang.domain.grammar.Token.LOGIC_TRUE_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.NUMBER_VALUE;
import static eu.jrie.abacus.lang.domain.grammar.Token.TEXT_VALUE;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ArgumentParserTest {

    private final CellReferenceResolver cellReferenceResolver = mock(CellReferenceResolver.class);
    private final TextValueResolver textValueResolver = mock(TextValueResolver.class);
    private final NumberValueResolver numberValueResolver = mock(NumberValueResolver.class);
    private final LogicValueResolver logicValueResolver = mock(LogicValueResolver.class);

    private final ArgumentParser argumentParser = new ArgumentParser( cellReferenceResolver, textValueResolver, numberValueResolver, logicValueResolver);

    @Test
    void shouldParseCellReference() throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        // given
        var impl = mock(FormulaImplementation.class);
        when(impl.isVararg()).thenReturn(false);
        when(impl.getArgumentTypes()).thenReturn(singletonList(TextValue.class));

        // and
        var argText = "A1";
        var cellText = "abc";
        var args = singletonList(new TokenMatch(CELL_REFERENCE, argText, argText));
        var expectedArgValue = new TextValue(cellText);
        when(cellReferenceResolver.resolve(argText)).thenReturn(c -> expectedArgValue);

        // when
        var result = argumentParser.parseArgs(impl, args);

        // then
        verify(cellReferenceResolver).resolve(argText);
        verifyNoInteractions(textValueResolver, numberValueResolver, logicValueResolver);
        assertIterableEquals(singletonList(expectedArgValue), getValues(result));
    }

    @Test
    void shouldParseTextValue() throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        // given
        var impl = mock(FormulaImplementation.class);
        when(impl.isVararg()).thenReturn(false);
        when(impl.getArgumentTypes()).thenReturn(singletonList(TextValue.class));

        // and
        var argText = "abc";
        var args = singletonList(new TokenMatch(TEXT_VALUE, argText, argText));
        var expectedArgValue = new TextValue(argText);
        when(textValueResolver.resolve(argText)).thenReturn(c -> expectedArgValue);

        // when
        var result = argumentParser.parseArgs(impl, args);

        // then
        verify(textValueResolver).resolve(argText);
        verifyNoInteractions(cellReferenceResolver, numberValueResolver, logicValueResolver);
        assertIterableEquals(singletonList(expectedArgValue), getValues(result));
    }

    @Test
    void shouldParseNumberValue() throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        // given
        var impl = mock(FormulaImplementation.class);
        when(impl.isVararg()).thenReturn(false);
        when(impl.getArgumentTypes()).thenReturn(singletonList(NumberValue.class));

        // and
        var argText = "1";
        var args = singletonList(new TokenMatch(NUMBER_VALUE, argText, argText));
        var expectedArgValue = new NumberValue(argText);
        when(numberValueResolver.resolve(argText)).thenReturn(c -> expectedArgValue);

        // when
        var result = argumentParser.parseArgs(impl, args);

        // then
        verify(numberValueResolver).resolve(argText);
        verifyNoInteractions(cellReferenceResolver, textValueResolver, logicValueResolver);
        assertIterableEquals(singletonList(expectedArgValue), getValues(result));
    }

    @Test
    void shouldParseLogicalTrueValue() throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        // given
        var impl = mock(FormulaImplementation.class);
        when(impl.isVararg()).thenReturn(false);
        when(impl.getArgumentTypes()).thenReturn(singletonList(LogicValue.class));

        // and
        var argText = "true";
        var args = singletonList(new TokenMatch(LOGIC_TRUE_VALUE, argText, argText));
        var expectedArgValue = new LogicValue(true);
        when(logicValueResolver.resolve(argText)).thenReturn(c -> expectedArgValue);

        // when
        var result = argumentParser.parseArgs(impl, args);

        // then
        verify(logicValueResolver).resolve(argText);
        verifyNoInteractions(cellReferenceResolver, numberValueResolver, textValueResolver);
        assertIterableEquals(singletonList(expectedArgValue), getValues(result));
    }

    @Test
    void shouldParseLogicalFalseValue() throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        // given
        var impl = mock(FormulaImplementation.class);
        when(impl.isVararg()).thenReturn(false);
        when(impl.getArgumentTypes()).thenReturn(singletonList(LogicValue.class));

        // and
        var argText = "false";
        var args = singletonList(new TokenMatch(LOGIC_TRUE_VALUE, argText, argText));
        var expectedArgValue = new LogicValue(false);
        when(logicValueResolver.resolve(argText)).thenReturn(c -> expectedArgValue);

        // when
        var result = argumentParser.parseArgs(impl, args);

        // then
        verify(logicValueResolver).resolve(argText);
        verifyNoInteractions(cellReferenceResolver, numberValueResolver, textValueResolver);
        assertIterableEquals(singletonList(expectedArgValue), getValues(result));
    }

    @Test
    void shouldResolveVararg() throws InvalidArgumentNumberException, InvalidArgumentTypeException {
        // given
        var impl = mock(FormulaImplementation.class);
        when(impl.isVararg()).thenReturn(true);
        doReturn(NumberValue.class).when(impl).getVarargType();

        // and
        var arg1Text = "1";
        var arg2Text = "2";
        var args = List.of(new TokenMatch(NUMBER_VALUE, arg1Text, arg1Text), new TokenMatch(NUMBER_VALUE, arg2Text, arg2Text));
        var expectedArg1Value = new NumberValue(arg1Text);
        var expectedArg2Value = new NumberValue(arg2Text);
        when(numberValueResolver.resolve(arg1Text)).thenReturn(c -> expectedArg1Value);
        when(numberValueResolver.resolve(arg2Text)).thenReturn(c -> expectedArg2Value);

        // when
        var result = argumentParser.parseArgs(impl, args);

        // then
        verify(numberValueResolver).resolve(arg1Text);
        verify(numberValueResolver).resolve(arg2Text);
        verifyNoInteractions(cellReferenceResolver, textValueResolver);
        assertIterableEquals(List.of(expectedArg1Value, expectedArg2Value), getValues(result));
    }

    private static List<Value> getValues(List<ArgumentValueSupplier> args) {
        return args.stream()
                .map(arg -> arg.get(null))
                .collect(toUnmodifiableList());
    }
}
