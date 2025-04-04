package com.github.pareronia.i18n_puzzles;

import static java.util.stream.Collectors.joining;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;

public class Puzzle2025_12 extends SolutionBase<Long>{
    private static final Locale ENGLISH = Locale.ENGLISH;
    private static final Locale SWEDISH = Locale.of("sv");
    private static final Locale DUTCH = Locale.of("nl", "NL");

    private Puzzle2025_12(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_12 create() {
        return new Puzzle2025_12(false);
    }

    public static Puzzle2025_12 createDebug() {
        return new Puzzle2025_12(true);
    }

    @Override
    public Long solve(final List<String> input) {
        final List<Entry> entries
                = input.stream().map(Entry::fromInput).toList();
        return Set.of(ENGLISH, SWEDISH, DUTCH).stream()
            .map(locale -> entries.stream()
                    .sorted(Entry.comparator(locale))
                    .skip(entries.size() / 2)
            .findFirst().orElseThrow())
            .mapToLong(Entry::phone)
            .reduce(1L, (a, b) -> a * b);
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "1885816494308838")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_12.createDebug().run();
    }

    private static final String TEST = """
            Ñíguez Peña, María de los Ángeles: 0151605
            Åberg, Rosa-Maria: 0110966
            Özaydın, Zeynep: 0185292
            van den Heyden, Harm: 0168131
            Ämtler, Lorena: 0112717
            Olofsson, Mikael: 0103652
            van Leeuwen, Joke: 0172199
            Vandersteen, Willy: 0120659
            Østergård, Magnus: 0113959
            van Leeuw, Floor: 0144158
            Navarrete Ortiz, Dolores: 0119411
            Aalto, Alvar: 0192872
            Zondervan, Jan Peter: 0103008
            Æbelø, Aurora: 0113267
            O'Neill, Cara: 0109551
            """;

    record Entry(String last, String infixes, String first, long phone) {
        public static Entry fromInput(final String input) {
            final String[] split = input.split(": ");
            final String[] nameSplit = split[0].split(", ");
            final String infixes = Arrays.stream(nameSplit[0].split(" "))
                .takeWhile(sp -> Character.isLowerCase(sp.charAt(0)))
                .collect(joining(" "));
            final String last = Arrays.stream(nameSplit[0].split(" "))
                .dropWhile(sp -> Character.isLowerCase(sp.charAt(0)))
                .collect(joining(" "));
            return new Entry(
                    last, infixes, nameSplit[1], Long.parseLong(split[1]));
        }

        private static String n(final String s, final Locale locale) {
            final String t = s.replaceAll("\\s+", "").replaceAll("\\'", "")
                .replace('ı', 'i').toUpperCase();
            if (locale == SWEDISH) {
                return t.replace('Ø', 'Ö').replace('Æ', 'Ä');
            }
            return t.replace(String.valueOf('Æ'), "ae").replace('Ø', 'O');
        }
        
        private String lastName(final Locale locale) {
            return switch(locale) {
                case final Locale l
                        when l.equals(DUTCH) -> n(last, l) + n(infixes, l);
                case final Locale l -> n(infixes, l) + n(last, l);
            };
        }

        public static Comparator<Entry> comparator(final Locale locale) {
            final Collator collator = Collator.getInstance(locale);
            final Comparator<Entry> byLastName = (o1, o2)
                -> collator.compare(o1.lastName(locale), o2.lastName(locale));
            final Comparator<Entry> byFirstName = (o1, o2)
                -> collator.compare(n(o1.first, locale), n(o2.first, locale));
            return byLastName.thenComparing(byFirstName);
        }
    }
}
