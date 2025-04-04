package com.github.pareronia.i18n_puzzles;

import static com.github.pareronia.i18n_puzzles.common.Utils.toAString;

import java.util.List;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;

public class Puzzle2025_11 extends SolutionBase<Integer>{

    private static final List<String> VARIANTS = List.of(
            "Οδυσσευς".toUpperCase(),
            "Οδυσσεως".toUpperCase(),
            "Οδυσσει".toUpperCase(),
            "Οδυσσεα".toUpperCase(),
            "Οδυσσευ".toUpperCase()
    );
    private static final String UPPER = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";

    private Puzzle2025_11(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_11 create() {
        return new Puzzle2025_11(false);
    }

    public static Puzzle2025_11 createDebug() {
        return new Puzzle2025_11(true);
    }

    @Override
    public Integer solve(final List<String> input) {
        final int length = UPPER.length();
        int ans = 0;
        for (final String line : input) {
            String test = line.toUpperCase();
            for (int i = 0; i < length; i++) {
                if (VARIANTS.stream().anyMatch(test::contains)) {
                    ans += i;
                    break;
                }
                test = Utils.asCharacterStream(test)
                    .map(ch -> UPPER.charAt((UPPER.indexOf(ch) + 1) % length))
                    .collect(toAString());
            }
        }
        return ans;
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "19")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_11.create().run();
    }

    private static final String TEST = """
            σζμ γ' ωοωλδθαξλδμξρ οπξρδυζ οξκτλζσθρ Ξγτρρδτρ.
            αφτ κ' λαλψφτ ωπφχλρφτ δξησηρζαλψφτ φελο, Φκβωωλβ.
            γ βρφαγζ ωνψν ωγφ πγχρρφ δρδαθωραγζ ρφανφ.
            """;
}
