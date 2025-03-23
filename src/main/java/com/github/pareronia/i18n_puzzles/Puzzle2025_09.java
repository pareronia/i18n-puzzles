package com.github.pareronia.i18n_puzzles;

import static java.util.stream.Collectors.joining;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_09 extends SolutionBase<String>{

    private final LocalDate NINE_ELEVEN = LocalDate.of(2001, Month.SEPTEMBER, 11);
    
    private Puzzle2025_09(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_09 create() {
        return new Puzzle2025_09(false);
    }

    public static Puzzle2025_09 createDebug() {
        return new Puzzle2025_09(true);
    }

    private boolean valid(final String date, final DateFormat df) {
        try {
            df.get().parse(date);
            return true;
        } catch (final DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String solve(final List<String> input) {
        record Entries(String date, String[] names) {
            public static Entries fromInput(final String line) {
                final String[] splits = line.split(": ");
                return new Entries(splits[0], splits[1].split(", "));
            }
        }
        
        final List<Entries> entrieses = input.stream()
                .map(Entries::fromInput)
                .toList();
        final Map<String, Integer> map = new HashMap<>();
        for (final Entries e : entrieses) {
            final int matches = Arrays.stream(DateFormat.values())
                .filter(df -> valid(e.date, df))
                .mapToInt(df -> df.mask)
                .reduce(0, (a, b) -> a | b);
            for (final String name : e.names) {
                map.merge(name, matches, (a, b) -> a & b);
            }
        }
        final Set<String> ans = new HashSet<>();
        for (final Entries e : entrieses) {
            for (final String name : e.names) {
                for (final DateFormat df : DateFormat.values()) {
                    if (((df.mask & map.get(name)) != 0)
                            && e.date.equals(df.get().format(NINE_ELEVEN))) {
                        ans.add(name);
                    }
                }
            }
        }
        return ans.stream().sorted().collect(joining(" "));
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "Margot Peter")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_09.create().run();
    }

    private static final String TEST = """
            16-05-18: Margot, Frank
            02-17-04: Peter, Elise
            06-02-29: Peter, Margot
            31-09-11: Elise, Frank
            09-11-01: Peter, Frank, Elise
            11-09-01: Margot, Frank
            """;

    private enum DateFormat {
        DMY("dd-MM-uu", 0b0001),
        MDY("MM-dd-uu", 0b0010),
        YMD("uu-MM-dd", 0b0100),
        YDM("uu-dd-MM", 0b1000);
        
        DateFormat(final String format, final int mask) {
            this.value = format;
            this.mask = mask;
        }

        private final String value;
        private final int mask;

        public DateTimeFormatter get() {
            return DateTimeFormatter
                    .ofPattern(this.value)
                    .withResolverStyle(ResolverStyle.STRICT);
        }
    }
}
