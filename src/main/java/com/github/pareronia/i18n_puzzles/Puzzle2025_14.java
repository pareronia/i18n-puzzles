package com.github.pareronia.i18n_puzzles;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_14 extends SolutionBase<Long>{
    
    private static final Map<Character, Long> UNITS = Map.of(
        '毛', 1L, '厘', 10L, '分', 100L, '寸', 1_000L, '尺', 10_000L,
        '間', 60_000L, '丈', 100_000L, '町', 3_600_000L, '里', 129_600_000L
    );
    private static final Map<Character, Long> DIGITS = Map.of(
        '一', 1L, '二', 2L, '三', 3L, '四', 4L, '五', 5L,
        '六', 6L, '七', 7L, '八', 8L, '九', 9L
    );
    private static final Map<Character, Long> MYRIADS = Map.of(
        '万', 10_000L, '億', 100_000_000L
    );
    private static final Map<Character, Long> TENS = Map.of(
        '十', 10L, '百', 100L, '千', 1_000L
    );

    private Puzzle2025_14(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_14 create() {
        return new Puzzle2025_14(false);
    }

    public static Puzzle2025_14 createDebug() {
        return new Puzzle2025_14(true);
    }

    private long to_mos(final String jap) {
        long ans = 0L;
        Long digit = null;
        long factor = 0;
        for (final char ch : jap.toCharArray()) {
            if (UNITS.containsKey(ch)) {
                ans += factor + (digit != null ? digit.longValue() : 0L);
            } else if (MYRIADS.containsKey(ch)) {
                final long val = digit != null ? digit.longValue() : 0L;
                ans += (factor + val) * MYRIADS.get(ch);
                factor = 0;
                digit = null;
            } else if (TENS.containsKey(ch)) {
                final long val = digit != null ? digit.longValue() : 1L;
                factor += val * TENS.get(ch);
                digit = null;
            } else if (DIGITS.containsKey(ch)) {
                digit = DIGITS.get(ch);
            } else {
                throw new IllegalStateException();
            }
        }
        return ans * UNITS.get(jap.charAt(jap.length() - 1));
    }

    @Override
    public Long solve(final List<String> input) {
        return input.stream()
                .mapToLong(line -> Arrays.stream(line.split(" × "))
                            .mapToLong(this::to_mos)
                            .reduce(1L, (a, b) -> a * b))
                .map(s -> s / 1_089_000_000L)
                .sum();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "2177741195")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_14.create().run();
    }

    private static final String TEST = """
            二百四十二町 × 三百五十一丈
            七十八寸 × 二十一万七千八百厘
            七万二千三百五十八町 × 六百十二分
            六寸 × 三十万七千九十八尺
            九間 × 三万三千百五十四里
            六百毛 × 七百四十四万千五百厘
            七十八億二千八十三万五千毛 × 二十八万八千六百毛
            三百七十四万二千五百三十厘 × 六百七十一万七千厘
            """;
}
