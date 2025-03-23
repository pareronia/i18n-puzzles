package com.github.pareronia.i18n_puzzles;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.i18n_puzzles.common.ANSIColors;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.SolutionUtils;
import com.github.pareronia.i18n_puzzles.common.SystemUtils;
import com.github.pareronia.i18n_puzzles.common.Timed;

public class PuzzleRunner {

    private final SystemUtils systemUtils = new SystemUtils();
    
    public static void main(final String[] args) throws Exception {
        if (args.length > 0) {
            final int year = Integer.parseInt(args[0]);
            final int day = Integer.parseInt(args[1]);
            new PuzzleRunner().run(year, day);
        } else {
            new PuzzleRunner().runAll();
        }
    }

    public void run(final int year, final int day) throws Exception {
        final String packageName = this.getClass().getPackageName();
        runPuzzle("%s.Puzzle%d_%02d".formatted(packageName, year, day));
    }
    
    public void runAll() throws Exception {
        final Stream<String> allSolutions
                = systemUtils.getAllSolutions(this.getClass().getPackageName());
        for (final String className : allSolutions.toList()) {
            runPuzzle(className);
            System.out.println();
        }
    }

    private void runPuzzle(final String className) throws Exception {
        final Method create = Class.forName(className).getDeclaredMethod("create");
        final Object puzzle = create.invoke(null);
        final Method getInputData
                = SolutionBase.class.getDeclaredMethod("getInputData");
        final Object inputData = getInputData.invoke(puzzle);
        final Method getTitle = SolutionBase.class.getDeclaredMethod("getTitle");
        final Method solve
                = SolutionBase.class.getDeclaredMethod("solve", List.class);
        final Timed<Object> timed = Timed.timed(
                () -> solve.invoke(puzzle, inputData),
                () -> systemUtils.getSystemNanoTime());
        System.out.println("%s: %s, took %s".formatted(
                ANSIColors.yellow((String) getTitle.invoke(puzzle)),
                ANSIColors.bold(String.valueOf(timed.result())),
                SolutionUtils.printDuration(timed.duration())));
    }
}
