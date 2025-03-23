package com.github.pareronia.i18n_puzzles;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_02 extends SolutionBase<String>{

    private Puzzle2025_02(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_02 create() {
        return new Puzzle2025_02(false);
    }

    public static Puzzle2025_02 createDebug() {
        return new Puzzle2025_02(true);
    }
    
    @Override
    public String solve(final List<String> input) {
        final Map<Instant, Long> hist = input.stream()
                .map(Instant::parse)
                .collect(groupingBy(o -> o, counting()));
        return hist.entrySet().stream()
                .max(comparing(Map.Entry<Instant, Long>::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx"));
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "2019-06-05T12:15:00+00:00")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_02.create().run();
    }

    private static final String TEST = """
            2019-06-05T08:15:00-04:00
            2019-06-05T14:15:00+02:00
            2019-06-05T17:45:00+05:30
            2019-06-05T05:15:00-07:00
            2011-02-01T09:15:00-03:00
            2011-02-01T09:15:00-05:00
            """;
}
