package com.github.pareronia.i18n_puzzles;

import java.util.List;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;

public class Puzzle2025_03 extends SolutionBase<Integer>{

    private Puzzle2025_03(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_03 create() {
        return new Puzzle2025_03(false);
    }

    public static Puzzle2025_03 createDebug() {
        return new Puzzle2025_03(true);
    }

    private boolean isValid(final String password) {
        final long count = password.chars().count();
        if (count < 4 || count > 12) {
            return false;
        }
        final int bits = Utils.asCharacterStream(password)
                .mapToInt(ch -> {
                    int ans = 0;
                    if (Character.isDigit(ch)) {
                        ans |= 0b0001;
                    }
                    if (Character.isUpperCase(ch)) {
                        ans |= 0b0010;
                    }
                    if (Character.isLowerCase(ch)) {
                        ans |= 0b0100;
                    }
                    if (ch.charValue() > 127) {
                        ans |= 0b1000;
                    }
                    return ans;
                })
                .reduce((a, b) -> a | b).getAsInt();
        return bits == 0b1111;
    }
    
    @Override
    public Integer solve(final List<String> input) {
        return (int) input.stream().filter(this::isValid).count();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "2")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_03.create().run();
    }

    private static final String TEST = """
            d9Ō
            uwI.E9GvrnWļbzO
            ž-2á
            Ģ952W*F4
            ?O6JQf
            xi~Rťfsa
            r_j4XcHŔB
            71äĜ3
            """;
}
