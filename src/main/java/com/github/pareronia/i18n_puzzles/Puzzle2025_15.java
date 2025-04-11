package com.github.pareronia.i18n_puzzles;

import static java.util.stream.Collectors.toSet;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;

public class Puzzle2025_15 extends SolutionBase<Integer>{
    private static final DateTimeFormatter DF
            = DateTimeFormatter.ofPattern("d MMMM uuuu");
    private static final int START = (int) (
        ZonedDateTime.of(LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0),
                          ZoneId.of("UTC"))
                .toEpochSecond() / 60);
    private static final int END = (int) (
        ZonedDateTime.of(LocalDateTime.of(2022, Month.DECEMBER, 31, 23, 59),
                          ZoneId.of("UTC"))
                .toEpochSecond() / 60);
    private static final LocalTime START_OF_DAY = LocalTime.of(8, 30);
    private static final LocalTime END_OF_DAY = LocalTime.of(16, 59);
    
    private Puzzle2025_15(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_15 create() {
        return new Puzzle2025_15(false);
    }

    public static Puzzle2025_15 createDebug() {
        return new Puzzle2025_15(true);
    }

    @Override
    public Integer solve(final List<String> input) {
        record Entity(ZoneId zone, Set<LocalDate> holidays) {
            public static Entity fromInput(final String line) {
                final String[] split = line.split("\\t+");
                final ZoneId zone = ZoneId.of(split[1]);
                final var holidays = Arrays.stream(split[2].split(";"))
                    .map(t -> LocalDate.parse(t, DF))
                    .collect(toSet());
                return new Entity(zone, holidays);
            }

            public boolean dayOff(final LocalDate ld) {
                return ld.getDayOfWeek() == DayOfWeek.SATURDAY
                        || ld.getDayOfWeek() == DayOfWeek.SUNDAY
                        || holidays.contains(ld);
            }

            public boolean outsideWorkHours(final LocalTime lt) {
                return lt.isBefore(START_OF_DAY) || lt.isAfter(END_OF_DAY);
            }

            public IntStream minutes(final boolean is24) {
                return IntStream.range(START, END)
                    .filter(i -> {
                        final var ldt = LocalDateTime.ofInstant(
                                Instant.ofEpochSecond(i * 60), zone);
                        return !dayOff(ldt.toLocalDate())
                            && !(!is24 && outsideWorkHours(ldt.toLocalTime()));
                    });
            }
        }

        final var blocks = Utils.toBlocks(input);
        final var provided = new boolean[END - START];
        blocks.get(0).stream()
            .flatMapToInt(line -> Entity.fromInput(line).minutes(false))
            .forEach(m -> provided[m - START] = true);
        final int[] overtimes = blocks.get(1).stream()
            .mapToInt(line -> (int) Entity.fromInput(line).minutes(true)
                                .filter(m -> !provided[m - START]).count())
            .toArray();
        return Arrays.stream(overtimes).max().getAsInt()
                - Arrays.stream(overtimes).min().getAsInt();
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "3030")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_15.create().run();
    }

    private static final String TEST = """
            TOPlap office in Melbourne\tAustralia/Melbourne\t26 December 2022;15 April 2022;18 April 2022;26 January 2022
            TOPlap office in Delft\tEurope/Amsterdam\t6 June 2022;26 December 2022;26 May 2022;27 April 2022
            TOPlap office in Manchester\tEurope/London\t26 December 2022;15 April 2022;3 June 2022
            TOPlap office in São Paulo\tAmerica/Sao_Paulo\t28 February 2022;26 December 2022;15 April 2022;1 May 2022
            TOPlap office in Orlando\tAmerica/New_York\t17 January 2022;26 December 2022;4 July 2022;5 September 2022

            FaxSchool, Halifax\tAmerica/Halifax\t6 January 2022;3 January 2022
            El Universidad Libre de Santiago\tAmerica/Santiago\t3 January 2022;18 April 2022
            Tokyo Media Corp\tAsia/Tokyo\t15 April 2022;26 May 2022
            """;
}
