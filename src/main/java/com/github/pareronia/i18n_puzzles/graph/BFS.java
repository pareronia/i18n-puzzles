package com.github.pareronia.i18n_puzzles.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BFS {
    
    public static <T> List<T> execute(
            final T start,
            final Predicate<T> isEnd,
            final Function<T, Stream<T>> adjacent
    ) {
        record State<T>(T node, int distance) {}

        final Deque<State<T>> q = new ArrayDeque<>(Set.of(new State<>(start, 0)));
        final Set<T> seen = new HashSet<>(Set.of(start));
        final Map<T, T> parent = new HashMap<>();
        while (!q.isEmpty()) {
            final State<T> state = q.poll();
            if (isEnd.test(state.node)) {
                final List<T> path = new ArrayList<>();
                path.add(state.node);
                T curr = state.node;
                while (parent.containsKey(curr)) {
                    curr = parent.get(curr);
                    path.add(curr);
                }
                return path;
            }
            adjacent.apply(state.node)
                .filter(n -> !seen.contains(n))
                .forEach(n -> {
                    seen.add(n);
                    parent.put(n, state.node);
                    q.add(new State<>(n, state.distance + 1));
                });
        }
        throw new IllegalStateException("Unsolvable");
    }
}
