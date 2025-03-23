package com.github.pareronia.i18n_puzzles;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;

public class Puzzle2025_06 extends SolutionBase<Integer>{

    private Puzzle2025_06(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_06 create() {
        return new Puzzle2025_06(false);
    }

    public static Puzzle2025_06 createDebug() {
        return new Puzzle2025_06(true);
    }

    private String decode(final String line) {
        try {
            return new String(line.getBytes("ISO-8859-1"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getWord(final String line, final int idx) {
        if (idx % 3 == 0 || idx % 5 == 0) {
            if (idx % 15 == 0) {
                return decode(decode(line));
            }
            return decode(line);
        }
        return line;
    }

    private int matchWord(final String line, final List<String> words) {
        final int size = (int) line.chars().count();
        for (int i = 0; i < size; i++) {
            final char ch = Character.toLowerCase(line.charAt(i));
            if (ch == '.') {
                continue;
            }
            for (int j = 0; j < words.size(); j++) {
                if (words.get(j).chars().count() == size
                        && words.get(j).toLowerCase().charAt(i) == ch) {
                    return j + 1;
                }
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public Integer solve(final List<String> input) {
        final List<List<String>> blocks = Utils.toBlocks(input);
        final List<String> words = IntStream.rangeClosed(1, blocks.get(0).size())
                .mapToObj(i -> getWord(blocks.get(0).get(i - 1), i))
                .toList();
        return blocks.get(1).stream()
                .mapToInt(line -> matchWord(line.strip(), words))
                .sum();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "50")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_06.create().run();
    }

    private static final String TEST = """
            geléet
            träffs
            religiÃ«n
            tancées
            kÃ¼rst
            roekoeÃ«n
            skälen
            böige
            fÃ¤gnar
            dardÃ©es
            amènent
            orquestrÃ¡
            imputarão
            molières
            pugilarÃÂ£o
            azeitámos
            dagcrème
            zÃ¶ger
            ondulât
            blÃ¶kt
            
               ...d...
                ..e.....
                 .l...
              ....f.
            ......t..
            """;
}
