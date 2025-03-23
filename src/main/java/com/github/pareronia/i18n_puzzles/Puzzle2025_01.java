package com.github.pareronia.i18n_puzzles;

import java.util.List;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_01 extends SolutionBase<Integer>{

    private static final int SIZE_SMS = 160;
    private static final int SIZE_TWEET = 140;
    private static final int RATE_SMS = 11;
    private static final int RATE_TWEET = 7;
    private static final int RATE_BOTH = 13;
    
    private Puzzle2025_01(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_01 create() {
        return new Puzzle2025_01(false);
    }

    public static Puzzle2025_01 createDebug() {
        return new Puzzle2025_01(true);
    }
    
    private int rate(final String line) {
        final byte[] bytes = line.getBytes();
        final int chars = (int) new String(bytes).chars().count();
        if (chars <= SIZE_TWEET && bytes.length <= SIZE_SMS) {
            return RATE_BOTH;
        } else if (chars <= SIZE_TWEET) {
            return RATE_TWEET;
        } else if (bytes.length <= SIZE_SMS) {
            return RATE_SMS;
        } else {
            return 0;
        }
    }

    @Override
    public Integer solve(final List<String> input) {
        return input.stream().mapToInt(this::rate).sum();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "31")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_01.create().run();
    }

    private static final String TEST = """
            néztek bele az „ártatlan lapocskába“, mint ahogy belenézetlen mondták ki rá a halálos itéletet a sajtó csupa 20–30 éves birái s egyben hóhérai.
            livres, et la Columbiad Rodman ne dépense que cent soixante livres de poudre pour envoyer à six milles son boulet d'une demi-tonne.  Ces
            Люди должны были тамъ и сямъ жить въ палаткахъ, да и мы не были помѣщены въ посольскомъ дворѣ, который также сгорѣлъ, а въ двухъ деревянныхъ
            Han hade icke träffat Märta sedan Arvidsons middag, och det hade gått nära en vecka sedan dess. Han hade dagligen promenerat på de gator, där
            """;
}
