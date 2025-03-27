package com.github.pareronia.i18n_puzzles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_07 extends SolutionBase<Integer>{

    private Puzzle2025_07(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_07 create() {
        return new Puzzle2025_07(false);
    }

    public static Puzzle2025_07 createDebug() {
        return new Puzzle2025_07(true);
    }

    @Override
    public Integer solve(final List<String> input) {
        int ans = 0;
        for (int i = 0; i < input.size(); i++) {
            final String[] split = input.get(i).split("\\s+");
            final LocalDateTime ldt = LocalDateTime.parse(split[0].substring(0, 23));
            final ZoneOffset offset = ZoneOffset.of(split[0].substring(23));
            final int correct = Integer.parseInt(split[1]);
            final int incorrect = Integer.parseInt(split[2]);
            final ZoneId zoneId;
            if (ZoneId.of("America/Halifax").getRules().isValidOffset(ldt, offset)) {
                zoneId = ZoneId.of("America/Halifax");
            } else {
                zoneId = ZoneId.of("America/Santiago");
            }
            ans += (i + 1) * ZonedDateTime.of(ldt, zoneId)
                .minusMinutes(incorrect).plusMinutes(correct).getHour();
        }
        return ans;
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "866")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_07.create().run();
    }

    private static final String TEST = """
            2012-11-05T09:39:00.000-04:00   969     3358
            2012-05-27T17:38:00.000-04:00   2771    246
            2001-01-15T22:27:00.000-03:00   2186    2222
            2017-05-15T07:23:00.000-04:00   2206    4169
            2005-09-02T06:15:00.000-04:00   1764    794
            2008-03-23T05:02:00.000-03:00   1139    491
            2016-03-11T00:31:00.000-04:00   4175    763
            2015-08-14T12:40:00.000-03:00   3697    568
            2013-11-03T07:56:00.000-04:00   402     3366
            2010-04-16T09:32:00.000-04:00   3344    2605
            """;
}
