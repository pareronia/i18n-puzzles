package com.github.pareronia.i18n_puzzles;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.ZoneInfo;

public class Puzzle2025_19 extends SolutionBase<String>{

    final DateTimeFormatter DF = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    private Puzzle2025_19(final boolean debug) {
        super(debug);
    }

    public static Puzzle2025_19 create() {
        return new Puzzle2025_19(false);
    }

    public static Puzzle2025_19 createDebug() {
        return new Puzzle2025_19(true);
    }

    private TimeZone loadZoneInfo(final String ver, final String zone) {
        try {
            final URL resource = this.getClass().getClassLoader()
                    .getResource("19/%s/%s".formatted(ver, zone));
            return new ZoneInfo(new File(resource.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Instant getInstant(final String date, final TimeZone tz) {
        final LocalDateTime ldt = LocalDateTime.parse(date, DF);
        final long epochMillis = ldt.toEpochSecond(ZoneOffset.UTC) * 1000;
        return OffsetDateTime.of(
                ldt,
                ZoneOffset.ofTotalSeconds(tz.getOffset(epochMillis) / 1000))
            .toInstant();
    }

    @Override
    public String solve(final List<String> input) {
        final Map<String, Set<String>> d = input.stream()
                .map(line -> line.split("; "))
                .collect(groupingBy(sp -> sp[1], mapping(sp -> sp[0], toSet())));
        final Map<Instant, Set<String>> c = new HashMap<>();
        for (final String ver : Set.of("2018c", "2018g", "2021b", "2023d")) {
            for (final String zone : d.keySet()) {
                final TimeZone tz = loadZoneInfo(ver, zone);
                for (final String date : d.get(zone)) {
                    final Instant instant = getInstant(date, tz);
                    c.computeIfAbsent(instant, k -> new HashSet<>()).add(zone);
                }
            }
        }
        final Instant ans = c.entrySet().stream()
                .filter(e -> d.keySet().equals(e.getValue()))
                .findFirst()
                .map(Entry::getKey).orElseThrow();
        return DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxxx")
                .withZone(ZoneId.of("UTC"))
                .format(ans);
    }

    @Samples({
        @Sample(input = TEST, expected = "2024-04-09T17:49:00+00:00")
    })
    @Override
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        Puzzle2025_19.create().run();
    }

    private static final String TEST = """
            2024-04-09 18:49:00; Africa/Casablanca
            2024-04-10 02:19:00; Asia/Pyongyang
            2024-04-10 04:49:00; Antarctica/Casey
            2024-04-12 12:13:00; Asia/Pyongyang
            2024-04-12 15:54:00; Africa/Casablanca
            2024-04-12 16:43:00; Africa/Casablanca
            2024-04-13 00:24:00; Asia/Pyongyang
            2024-04-13 01:54:00; Antarctica/Casey
            2024-04-13 07:43:00; Antarctica/Casey
            """;
}
