package com.github.pareronia.i18n_puzzles;

import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_05 extends SolutionBase<Integer>{

    private static final int POO = "💩".codePointAt(0);

    private Puzzle2025_05(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_05 create() {
        return new Puzzle2025_05(false);
    }

    public static Puzzle2025_05 createDebug() {
        return new Puzzle2025_05(true);
    }

    @Override
    public Integer solve(final List<String> input) {
        return (int) IntStream.range(0, input.size())
            .filter(i -> {
                final int[] cps = input.get(i).codePoints().toArray();
                return cps[(i * 2) % cps.length] == POO;
            })
            .count();
    }

    @Override
    public void samples() {
        final List<String> input = List.of(
                " ⚘   ⚘ ",
                "  ⸫   ⸫",
                "🌲   💩  ",
                "     ⸫⸫",
                " 🐇    💩",
                "⸫    ⸫ ",
                "⚘🌲 ⸫  🌲",
                "⸫    🐕 ",
                "  ⚘  ⸫ ",
                "⚘⸫⸫   ⸫",
                "  ⚘⸫   ",
                " 💩  ⸫  ",
                "     ⸫⸫"
        );
        assert Puzzle2025_05.createDebug().solve(input) == 2;
    }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_05.create().run();
    }
}
