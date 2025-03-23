package com.github.pareronia.i18n_puzzles;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;

public class Puzzle2025_08 extends SolutionBase<Integer> {

    private Puzzle2025_08(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_08 create() {
        return new Puzzle2025_08(false);
    }

    public static Puzzle2025_08 createDebug() {
        return new Puzzle2025_08(true);
    }

    private boolean isValid(final String password) {
        final long count = password.chars().count();
        if (count < 4 || count > 12) {
            return false;
        }
        final String normalized
                = Normalizer.normalize(password.toLowerCase(), Form.NFD);
        final int bits = Utils.asCharacterStream(normalized)
                .mapToInt(ch -> {
                    int ans = 0;
                    if (Character.isDigit(ch)) {
                        ans |= 0b001;
                    }
                    if (Character.isLetter(ch)) {
                        if (Set.of('a', 'e', 'i', 'o', 'u').contains(ch)) {
                            ans |= 0b010;
                        } else {
                            ans |= 0b100;
                        }
                    }
                    return ans;
                })
                .reduce((a, b) -> a | b).getAsInt();
        final Map<Character, Long> counter = Utils.asCharacterStream(normalized)
                .filter(Character::isLetter)
                .collect(groupingBy(o -> o, counting()));
        return bits == 0b111
                && counter.values().stream().noneMatch(v -> v > 1);
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
        Puzzle2025_08.create().run();
    }

    private static final String TEST = """
            iS0
            V8AeC1S7KhP4Ļu
            pD9Ĉ*jXh
            E1-0
            ĕnz2cymE
            tqd~üō
            IgwQúPtd9
            k2lp79ąqV
            """;
}
