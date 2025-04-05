package com.github.pareronia.i18n_puzzles;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_16LE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.HexFormat;
import java.util.List;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;

public class Puzzle2025_13 extends SolutionBase<Integer>{

    private Puzzle2025_13(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_13 create() {
        return new Puzzle2025_13(false);
    }

    public static Puzzle2025_13 createDebug() {
        return new Puzzle2025_13(true);
    }

    private String getWord(final String line) {
        final byte[] bytes = HexFormat.of().parseHex(line);
        if (line.startsWith("efbbbf")) {
            return new String(bytes, 3, bytes.length - 3, UTF_8);
        } else if (line.startsWith("feff") || line.startsWith("fffe")) {
            return new String(bytes, UTF_16);
        }
        for (final Charset cs : List.of(UTF_8, ISO_8859_1, UTF_16LE, UTF_16BE)) {
            try {
                final String s = cs.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes))
                    .toString();
                if (Utils.asCharacterStream(s).allMatch(Character::isAlphabetic)) {
                    return s;
                }
            } catch (final CharacterCodingException e) {
            }
        }
        throw new IllegalStateException();
    }

    private int matchWord(final String line, final List<String> words) {
        final int size = (int) line.codePoints().count();
        for (int i = 0; i < size; i++) {
            final int ch = line.codePointAt(i);
            if (ch == '.') {
                continue;
            }
            for (int j = 0; j < words.size(); j++) {
                if (words.get(j).codePoints().count() == size
                        && words.get(j).codePointAt(i) == ch) {
                    return j + 1;
                }
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public Integer solve(final List<String> input) {
        final List<List<String>> blocks = Utils.toBlocks(input);
        final List<String> words = blocks.get(0).stream()
                .map(this::getWord)
                .toList();
        return blocks.get(1).stream()
                .mapToInt(line -> matchWord(line.strip(), words))
                .sum();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "47")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_13.create().run();
    }

    private static final String TEST = """
            616e77c3a4686c65
            796c74e46de47373e4
            efbbbf73796b6b696dc3a46bc3b6
            0070006f00eb0065006d
            feff0069007400e4007000e400e4006800e4006e
            61757373e46774
            626c6173c3a9
            637261776cc3a9
            6c00e20063006800e2007400
            64657370656e68e1
            6c6964e172656973
            fffe6700e20063006800e9006500
            6500700069007400e100660069006f00
            feff007300fc006e006400650072006e
            fffe7200f600730074006900

               ..s....
              ...w..
             ....i
            .....f..
                .t.......
            """;
}
