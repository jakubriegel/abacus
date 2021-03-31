package eu.jrie.abacus.lang.domain.parser.argument;

import eu.jrie.abacus.core.domain.cell.Position;
import eu.jrie.abacus.core.domain.formula.ArgumentValueSupplier;
import eu.jrie.abacus.core.infra.Alphabet;

import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class CellReferenceResolver extends ArgumentResolver {

    private static final Pattern columnPattern = Pattern.compile("^[A-Z]+");
    private static final Pattern rowPattern = Pattern.compile("[0-9]+$");
    private static final Alphabet alphabet = new Alphabet();

    @Override
    ArgumentValueSupplier resolve(String text) {
        int x = resolveX(text);
        int y = resolveY(text);
        var cellPosition = new Position(x, y);
        return context -> context.getCellValue(cellPosition);
    }

    private static int resolveX(String text) {
        var raw = find(columnPattern, text);
        return alphabet.getNumber(raw);
    }

    private static int resolveY(String text) {
        var raw = find(rowPattern, text);
        return parseInt(raw) - 1;
    }

    private static String find(Pattern pattern, String text) {
        final var matcher = pattern.matcher(text);
        if (!matcher.find()) {
            throw new IllegalStateException();
        }
        return matcher.group();
    }
}
