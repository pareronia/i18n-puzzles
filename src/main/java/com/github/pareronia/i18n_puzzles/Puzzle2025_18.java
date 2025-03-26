package com.github.pareronia.i18n_puzzles;

import static com.github.pareronia.i18n_puzzles.Puzzle2025_18.Evaluator.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_18 extends SolutionBase<Integer>{

    private Puzzle2025_18(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_18 create() {
        return new Puzzle2025_18(false);
    }

    public static Puzzle2025_18 createDebug() {
        return new Puzzle2025_18(true);
    }

    @Override
    public Integer solve(final List<String> input) {
        return (int) input.stream()
                .map(BiDi::fromInput)
                .mapToLong(bidi -> {
                    final Double e1 = Double.valueOf(eval(bidi.logicalOrder));
                    final Double e2 = Double.valueOf(eval(bidi.visualOrder));
                    return Math.abs(Math.round(e1) - Math.round(e2));
                })
                .sum();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "19282")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_18.create().run();
    }

    private static final String TEST = """
            \u2067(1 * ((\u2066(66 / 2)\u2069 - 15) - 4)) * (1 + (1 + 1))\u2069
            \u2067(8 / (\u2066(1 * 3)\u2069 + 1)) * 130\u2069
            47 * ((3 + 1) * (\u2067(40 * (24 - 8))\u2069 / (\u2067(72 / 6)\u2069 - \u2067(\u2066(2 * 1)\u2069 + 2)\u2069)))
            90 * \u2067(((810 / (\u2066(3 + 5)\u2069 + 1)) + ((169 - 79) / 2)) - \u2066(93 - 28)\u2069)\u2069
            92 * (\u2067(92 / ((54 / 3) / (5 + 4)))\u2069 - \u2067(2 * (64 / 8))\u2069)
            73 + (3 * (1 * \u2067(((3 + (6 - 2)) * 6) + \u2066((52 * 6) / \u2067(13 - (7 - 2))\u2069)\u2069)\u2069))
            """;

    private record BiDi(String logicalOrder, String visualOrder) {
        private static final char LRI = '\u2066';
        private static final char RLI = '\u2067';
        private static final char PDI = '\u2069';
        private static final Pattern PATTERN
                = Pattern.compile("(%s|%s|%s)".formatted(LRI, RLI, PDI));

        record Range(int start, int end) {}

        public static BiDi fromInput(final String line) {
            final int size = (int) line.codePoints().count();
            final String rex = PATTERN.matcher(line).replaceAll(" ");
            final int[] levels = new int[size];
            int level = 0;
            for (int i = 0; i < size; i++) {
                final Integer cp = Integer.valueOf(line.codePointAt(i));
                switch (cp) {
                    case final Integer n when n == LRI -> {
                        levels[i] = level;
                        level++;
                        assert level % 2 == 0;
                    }
                    case final Integer n when n == RLI -> {
                        levels[i] = level;
                        level++;
                        assert level % 2 == 1;
                    }
                    case final Integer n when n == PDI -> {
                        level = Math.max(0, level - 1);
                        levels[i] = level;
                    }
                    case final Integer n when '0' <= n && n <= '9' -> {
                        levels[i] = level + (level % 2 == 0 ? 0 : 1);
                    }
                    default -> levels[i] = level;
                }
            }
            int max = Arrays.stream(levels).max().getAsInt();
            String flip = rex;
            while (max > 0) {
                final List<Range> ranges = new ArrayList<>();
                for (int i = 0; i < levels.length - 1; i++) {
                    if (levels[i] == max && levels[i + 1] == max) {
                        int j = i;
                        while (levels[j] == max) {
                            j++;
                        }
                        ranges.add(new Range(i, j));
                        i = j;
                    }
                }
                for (final Range rng : ranges) {
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < rng.start; i++) {
                        sb.appendCodePoint(flip.codePointAt(i));
                    }
                    for (int i = rng.end - 1; i >= rng.start; i--) {
                        final int cp = flip.codePointAt(i);
                        if (cp == ')') {
                            sb.appendCodePoint('(');
                        } else if (cp == '(') {
                            sb.appendCodePoint(')');
                        } else {
                            sb.appendCodePoint(cp);
                        }
                    }
                    for (int i = rng.end; i < size; i++) {
                        sb.appendCodePoint(flip.codePointAt(i));
                    }
                    flip = sb.toString();
                }
                for (int i = 0; i < levels.length - 1; i++) {
                    if (levels[i] == max) {
                        levels[i]--;
                    }
                }
                max--;
            }
            return new BiDi(rex, flip);
        }
    }

    static final class Evaluator {
        
        /**
         * @author https://stackoverflow.com/a/26227947
         */
        public static double eval(final String str) {
            return new Object() {
                int pos = -1, ch;
                
                void nextChar() {
                    ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                }
                
                boolean eat(final int charToEat) {
                    while (ch == ' ') {
                        nextChar();
                    }
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }
                
                double parse() {
                    nextChar();
                    final double x = parseExpression();
                    if (pos < str.length()) {
                        throw new RuntimeException("Unexpected: " + (char)ch);
                    }
                    return x;
                }
                
                // Grammar:
                // expression = term | expression `+` term | expression `-` term
                // term = factor | term `*` factor | term `/` factor
                // factor = `+` factor | `-` factor | `(` expression `)` | number
                //        | functionName `(` expression `)` | functionName factor
                //        | factor `^` factor
                
                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if      (eat('+')) {
                            x += parseTerm(); // addition
                        } else if (eat('-')) {
                            x -= parseTerm(); // subtraction
                        } else { // subtraction
                        	return x;
                        }
                    }
                }
                
                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if (eat('*')) {
                            x *= parseFactor(); // multiplication
                        } else if (eat('/')) {
                            x /= parseFactor(); // division
                        } else { // division
                        	return x;
                        }
                    }
                }
                
                double parseFactor() {
                    if (eat('+')) {
                        return +parseFactor(); // unary plus
                    }
                    if (eat('-'))
                    {
                        return -parseFactor(); // unary minus
                    }
                    
                    double x;
                    final int startPos = this.pos;
                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        if (!eat(')')) {
                            throw new RuntimeException("Missing ')'");
                        }
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                        while ((ch >= '0' && ch <= '9') || ch == '.') {
                            nextChar();
                        }
                        x = Double.parseDouble(str.substring(startPos, this.pos));
                    } else if (ch >= 'a' && ch <= 'z') { // functions
                        while (ch >= 'a' && ch <= 'z') {
                            nextChar();
                        }
                        final String func = str.substring(startPos, this.pos);
                        if (eat('(')) {
                            x = parseExpression();
                            if (!eat(')')) {
                                throw new RuntimeException("Missing ')' after argument to " + func);
                            }
                        } else {
                            x = parseFactor();
                        }
                        if (func.equals("sqrt")) {
                            x = Math.sqrt(x);
                        } else if (func.equals("sin")) {
                            x = Math.sin(Math.toRadians(x));
                        } else if (func.equals("cos")) {
                            x = Math.cos(Math.toRadians(x));
                        } else if (func.equals("tan")) {
                            x = Math.tan(Math.toRadians(x));
                        } else {
                            throw new RuntimeException("Unknown function: " + func);
                        }
                    } else {
                        throw new RuntimeException("Unexpected: " + (char)ch);
                    }
                    
                    if (eat('^'))
                    {
                        x = Math.pow(x, parseFactor()); // exponentiation
                    }
                    
                    return x;
                }
            }.parse();
        }
    }
}
