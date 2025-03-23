package com.github.pareronia.i18n_puzzles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.pareronia.i18n_puzzles.common.Sample;
import com.github.pareronia.i18n_puzzles.common.Samples;
import com.github.pareronia.i18n_puzzles.common.SolutionBase;
import com.github.pareronia.i18n_puzzles.common.Utils;
import com.github.pareronia.i18n_puzzles.graph.BFS;

public class WIP_Puzzle2025_16 extends SolutionBase<Integer>{
    private WIP_Puzzle2025_16(final boolean debug) {
        super(debug, "CP437");
    }

    public static WIP_Puzzle2025_16 create() {
        return new WIP_Puzzle2025_16(false);
    }

    public static WIP_Puzzle2025_16 createDebug() {
        return new WIP_Puzzle2025_16(true);
    }

    @Override
    public Integer solve(final List<String> input) {
        record State(Cell cell, int rotations) {
            @Override
            public boolean equals(final Object obj) {
                return switch (obj) {
                    case final Cell cell -> this.cell.equals(cell);
                    default -> false;
                };
            }

            @Override
            public int hashCode() {
                return Objects.hash(cell);
            }
        }
//        input.forEach(this::log);
        final List<String> normalized = input.stream()
            .map(s -> Utils.asCharacterStream(s)
                    .map(ch -> REPLACE.getOrDefault(ch, ch))
                    .map(ch -> REPLACE.containsValue(ch) || ch == ' ' ? ch : ' ')
                    .collect(Utils.toAString()))
            .toList();
        normalized.forEach(this::log);
        final Grid grid = new Grid(normalized.stream()
            .map(String::toCharArray)
            .toArray(char[][]::new));
        final Cell end = new Cell(grid.height() - 1, grid.width() - 1);
        final List<State> path = BFS.execute(
                new State(Cell.at(0, 0), 0),
                state -> state.cell.equals(end),
                state -> {
                    final Set<State> ans = new HashSet<>();
                    char ch = grid.valueAt(state.cell);
                    if (state.rotations != 0) {
                        ch = ROTATE.get(ch).stream()
                                .filter(r -> r.rotations == state.rotations)
                                .map(Rotated::ch)
                                .findFirst().orElseThrow();
                    }
                    for (final Direction d : OUTS.get(ch)) {
                        final Cell n = state.cell.at(d);
                        if (!grid.inBounds(n)) {
                            continue;
                        }
                        final char nch = grid.valueAt(n);
                        if (nch == ' ') {
                            continue;
                        }
                        if (INS.get(d).contains(nch)) {
                            ans.add(new State(n, 0));
                        }
                        for (final Rotated rtd : ROTATE.get(nch)) {
                            if (INS.get(d).contains(rtd.ch)) {
                                ans.add(new State(n, rtd.rotations));
                            }
                        }
                    }
                    return ans.stream();
                });
        log(path);
        return 0;
    }

    @Override
    @Samples({
        @Sample(input = TEST, expected = "34")
    })
    public void samples() { }

    public static void main(final String[] args) throws Exception {
        WIP_Puzzle2025_16.create().run();
    }

    enum Direction {
        UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1);

        Direction(final int dr, final int dc) {
            this.dr = dr;
            this.dc = dc;
        }

        private final int dr;
        private final int dc;
    }

    record Cell(int row, int col) {
        public static final Cell at(final int row, final int col) {
            return new Cell(row, col);
        }

        public Cell at(final Direction d) {
            return Cell.at(row + d.dr, col + d.dc);
        }
    }

    record Grid(char[][] values) {
        public boolean inBounds(final Cell cell) {
            return 0 <= cell.row && cell.row < height()
                    && 0 <= cell.col && cell.col < width();
        }

        public char valueAt(final Cell cell) {
            assert inBounds(cell);
            return values[cell.row][cell.col];
        }

        public int height() {
            return values.length;
        }
        
        public int width() {
            return values[0].length;
        }
    }

    private static final String TEST = """
            └──┐     ┘┬┐
            └──┘     ─││
            └─│││──┘τ┘┘│
            ■┌╞──│─┐  ┘┐
             ─═╔╔┌│─│─┴┐
             │═╗╠│½    │
            ┌┘╗║╝─°    │
            └──││┐     │
            """;

    private static final Map<Character, Character> REPLACE = new HashMap<>();
    {
        REPLACE.put('║', '│');
        REPLACE.put('╚', '└');
        REPLACE.put('╝', '┘');
        REPLACE.put('╗', '┐');
        REPLACE.put('╔', '┌');
        REPLACE.put('═', '─');
        REPLACE.put('╞', '├');
        REPLACE.put('╠', '├');
        REPLACE.put('╡', '┤');
        REPLACE.put('╤', '┬');
        REPLACE.put('╥', '┬');
        REPLACE.put('╦', '┬');
        REPLACE.put('╧', '┴');
        REPLACE.put('╩', '┴');
        REPLACE.put('╪', '┼');
        REPLACE.put('╫', '┼');
    }

    record Rotated(char ch, int rotations) {}
   
    private static final Map<Character, Set<Rotated>> ROTATE = new HashMap<>();
    {
        ROTATE.put('─', Set.of(new Rotated('│', 1)));
        ROTATE.put('│', Set.of(new Rotated('─', 1)));
        ROTATE.put('┌', Set.of(new Rotated('┐', 1), new Rotated('┘', 2), new Rotated('└', 3)));
        ROTATE.put('┐', Set.of(new Rotated('┘', 1), new Rotated('└', 2), new Rotated('┐', 3)));
        ROTATE.put('└', Set.of(new Rotated('┌', 1), new Rotated('┐', 2), new Rotated('┘', 3)));
        ROTATE.put('┘', Set.of(new Rotated('└', 1), new Rotated('┌', 2), new Rotated('┐', 3)));
        ROTATE.put('├', Set.of(new Rotated('┬', 1), new Rotated('┤', 2), new Rotated('┴', 3)));
        ROTATE.put('┤', Set.of(new Rotated('┴', 1), new Rotated('├', 2), new Rotated('┬', 3)));
        ROTATE.put('┬', Set.of(new Rotated('┤', 1), new Rotated('┴', 2), new Rotated('├', 3)));
        ROTATE.put('┴', Set.of(new Rotated('├', 1), new Rotated('┬', 2), new Rotated('┤', 3)));
        ROTATE.put('┼', Set.of());
    }

    private static final Map<Character, Set<Direction>> OUTS = new HashMap<>();
    {
        OUTS.put('─', Set.of(Direction.RIGHT, Direction.LEFT));
        OUTS.put('│', Set.of(Direction.UP, Direction.DOWN));
        OUTS.put('┌', Set.of(Direction.RIGHT, Direction.DOWN));
        OUTS.put('┐', Set.of(Direction.LEFT, Direction.DOWN));
        OUTS.put('└', Set.of(Direction.UP, Direction.RIGHT));
        OUTS.put('┘', Set.of(Direction.UP, Direction.LEFT));
        OUTS.put('├', Set.of(Direction.UP, Direction.RIGHT, Direction.DOWN));
        OUTS.put('┤', Set.of(Direction.UP, Direction.LEFT, Direction.DOWN));
        OUTS.put('┬', Set.of(Direction.RIGHT, Direction.DOWN, Direction.LEFT));
        OUTS.put('┴', Set.of(Direction.UP, Direction.RIGHT, Direction.LEFT));
        OUTS.put('┼', Set.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT));
    }
    
    private static final Map<Direction, Set<Character>> INS = Map.of(
        Direction.UP, Set.of('|', '┌', '┐', '├', '┤', '┬', '┼'),
        Direction.RIGHT, Set.of('-', '┐', '┘', '┤', '┬', '┴', '┼'),
        Direction.DOWN, Set.of('|', '└', '┘', '├', '┤', '┴', '┼'),
        Direction.LEFT, Set.of('-', '┌', '└', '├', '┬', '┴', '┼')
    );
}
