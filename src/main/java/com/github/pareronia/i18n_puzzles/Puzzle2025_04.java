package com.github.pareronia.i18n_puzzles;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_04 extends SolutionBase<Long>{

    private static final Map<String, Month> MONTHS = new HashMap<>();

    {
        MONTHS.put("Jan", Month.JANUARY);
        MONTHS.put("Feb", Month.FEBRUARY);
        MONTHS.put("Mar", Month.MARCH);
        MONTHS.put("Apr", Month.APRIL);
        MONTHS.put("May", Month.MAY);
        MONTHS.put("Jun", Month.JUNE);
        MONTHS.put("Jul", Month.JULY);
        MONTHS.put("Aug", Month.AUGUST);
        MONTHS.put("Sep", Month.SEPTEMBER);
        MONTHS.put("Oct", Month.OCTOBER);
        MONTHS.put("Nov", Month.NOVEMBER);
        MONTHS.put("Dec", Month.DECEMBER);
    }

    private Puzzle2025_04(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_04 create() {
        return new Puzzle2025_04(false);
    }

    public static Puzzle2025_04 createDebug() {
        return new Puzzle2025_04(true);
    }

    private long toEpochMinutes(final String line) {
        final String[] splits
                = line.replace(",", "").replace(":", " ").split("\\s+");
        final ZonedDateTime zoned = ZonedDateTime.of(
                LocalDateTime.of(
                    Integer.parseInt(splits[4]),
                    MONTHS.get(splits[2]),
                    Integer.parseInt(splits[3]),
                    Integer.parseInt(splits[5]),
                    Integer.parseInt(splits[6])),
                ZoneId.of(splits[1]));
        return zoned.toEpochSecond() / 60;
    }
    
    @Override
    public Long solve(final List<String> input) {
        final List<Long> epochMins = input.stream()
                .filter(s -> !s.isBlank())
                .map(this::toEpochMinutes)
                .toList();
        return IntStream.range(0, epochMins.size())
                .mapToLong(i -> i % 2 == 1 ? epochMins.get(i) : -epochMins.get(i))
                .sum();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "3143")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_04.createDebug().run();
    }

    private static final String TEST = """
            Departure: Europe/London                  Mar 04, 2020, 10:00
            Arrival:   Europe/Paris                   Mar 04, 2020, 11:59
            
            Departure: Europe/Paris                   Mar 05, 2020, 10:42
            Arrival:   Australia/Adelaide             Mar 06, 2020, 16:09
            
            Departure: Australia/Adelaide             Mar 06, 2020, 19:54
            Arrival:   America/Argentina/Buenos_Aires Mar 06, 2020, 19:10
            
            Departure: America/Argentina/Buenos_Aires Mar 07, 2020, 06:06
            Arrival:   America/Toronto                Mar 07, 2020, 14:43
            
            Departure: America/Toronto                Mar 08, 2020, 04:48
            Arrival:   Europe/London                  Mar 08, 2020, 16:52
            """;
}
