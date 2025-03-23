package com.github.pareronia.i18n_puzzles.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class SystemUtils {

	public Path getMemoDir() {
		final String env = System.getenv("I18N_PUZZLES_DIR");
		if (env != null && !env.isBlank()) {
			return Paths.get(env);
		} else if (isOsWindows()) {
			return Paths.get(System.getenv("APPDATA"), "i18n-puzzles");
		} else if (isOsLinux()) {
			final Path userHome = getUserHome().toPath();
			return userHome.resolve(".config").resolve("i18n-puzzles");
		} else {
			throw new UnsupportedOperationException("OS not supported");
		}
	}
	
	public long getSystemNanoTime() {
	    return System.nanoTime();
	}
	
	private boolean isOsWindows() {
	    return getOsName().startsWith("Windows");
	}
	
	private boolean isOsLinux() {
	    return getOsName().toLowerCase().startsWith("linux");
	}
    
	private File getUserHome() {
        return new File(getSystemProperty("user.home"));
    }

    private String getOsName() {
        return getSystemProperty("os.name");
    }
	
	private String getSystemProperty(final String property) {
	    return System.getProperty(property);
	}

	public Stream<String> getAllSolutions(final String packageName) {
	    final Path packagePath = Path.of(packageName.replace('.', '/'));
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(
                Path.of("src", "main", "java").resolve(packagePath),
                "Puzzle????_??.java")) {
	        final Builder<String> builder = Stream.builder();
	        ds.iterator().forEachRemaining(path ->
	            builder.add(packageName + "." + path.getFileName().toString()
	                            .substring(0, "Puzzleyyyy_dd".length())));
	        return builder.build();
        } catch (final IOException e) {
			throw new RuntimeException(e);
        }
	}
}
