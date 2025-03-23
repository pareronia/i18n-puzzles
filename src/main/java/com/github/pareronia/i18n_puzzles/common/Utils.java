package com.github.pareronia.i18n_puzzles.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {

    public static Stream<Character> asCharacterStream(final String string) {
        Objects.requireNonNull(string, () -> "Expected string to be non-null");
        return IntStream.range(0, string.length())
                .mapToObj(i -> Character.valueOf(string.charAt(i)));
    }
    
	public static Collector<Character, StringBuilder, String> toAString() {
		return Collector.of(
				StringBuilder::new,
				StringBuilder::append,
				StringBuilder::append,
				StringBuilder::toString);
	}
	
	public static List<List<String>> toBlocks(final List<String> inputs) {
		if (inputs.isEmpty()) {
			return List.of();
		}
		final List<List<String>> blocks = new ArrayList<>();
		int i = 0;
		final int last = inputs.size() - 1;
		blocks.add(new ArrayList<>());
		for (int j = 0; j <= last; j++) {
			if (inputs.get(j).isEmpty()) {
				if (j != last) {
					blocks.add(new ArrayList<>());
					i++;
				}
			} else {
				blocks.get(i).add(inputs.get(j));
			}
		}
		return blocks;
	}
}
