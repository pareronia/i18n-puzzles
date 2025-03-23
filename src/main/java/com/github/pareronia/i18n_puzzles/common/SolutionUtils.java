package com.github.pareronia.i18n_puzzles.common;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SolutionUtils {

    public static List<String> splitLines(final String input) {
        return asList((Objects.requireNonNull(input) + "\n").split("\\r?\\n"));
    }
    
    public static String printDuration(final Duration duration) {
        final double timeSpent = duration.toNanos() / 1_000_000.0;
        String time;
        if (timeSpent <= 1000) {
            time = String.format("%.3f", timeSpent);
        } else if (timeSpent <= 5_000) {
            time = ANSIColors.yellow(String.format("%.0f", timeSpent));
        } else {
            time = ANSIColors.red(String.format("%.0f", timeSpent));
        }
        return String.format("%s ms", time);
    }

    public static void runSamples(final Class<?> klass)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final List<Sample> samples = Stream.of(klass.getMethods())
                .filter(m -> m.isAnnotationPresent(Samples.class))
                .map(m -> m.getAnnotation(Samples.class))
                .flatMap(ann -> Stream.of(ann.value()))
                .collect(toList());
        for (final Sample sample : samples) {
            SolutionUtils.runSample(klass, sample);
        }
    }
    
    private static void runSample(final Class<?> klass, final Sample sample)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Object puzzle = klass
                .getDeclaredMethod("create" + (sample.debug() ? "Debug" : ""))
                .invoke(null);
        final List<String> input = splitLines(new String(
                sample.input().getBytes(Charset.forName(sample.charset())),
                Charset.forName("UTF-8")));
        final Object answer = puzzle.getClass()
                .getMethod(sample.method(), List.class)
                .invoke(puzzle, input);
        assert Objects.equals(sample.expected(), String.valueOf(answer))
            : String.format("FAIL '%s(%s)'. Expected: '%s', got '%s'",
                    sample.method(),
                    input,
                    sample.expected(),
                    String.valueOf(answer));
    }
}
