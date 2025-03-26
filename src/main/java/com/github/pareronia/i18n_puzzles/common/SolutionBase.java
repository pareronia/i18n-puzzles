package com.github.pareronia.i18n_puzzles.common;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public abstract class SolutionBase<Output> implements LoggerEnabled {
   
    protected final int year;
    protected final int day;
    protected final String title;
    protected final boolean debug;
    protected final String charset;
    protected final Logger logger;
    protected final SystemUtils systemUtils;
    protected boolean trace;

    protected SolutionBase(final boolean debug) {
        this(debug, "UTF-8");
    }

    protected SolutionBase(final boolean debug, final String charset) {
        final String name = this.getClass().getSimpleName();
        final String[] split = name.substring(name.length() - 7).split("_");
        this.year = Integer.parseInt(split[0]);
        this.day = Integer.parseInt(split[1]);
        this.title = "i18n-puzzles.com %d Day %-2d".formatted(this.year, this.day);
        this.debug = debug;
        this.charset = charset;
        this.logger = new Logger(debug);
        this.systemUtils = new SystemUtils();
    }

    public String getTitle() {
        return title;
    }

    protected void samples() {}
    
    public abstract Output solve(final List<String> inputs);

    public void run() throws Exception {
        System.out.println(ANSIColors.yellow(this.title));
        System.out.println();
        SolutionUtils.runSamples(this.getClass());
        this.samples();
        final List<String> inputData = this.getInputData();
        final Timed<Output> timed = Timed.timed(
               () -> this.solve(inputData),
               () -> systemUtils.getSystemNanoTime());
        System.out.println("Answer : %s".formatted(
                ANSIColors.bold(timed.result().toString())));
        System.out.println("Took: %s".formatted(
                SolutionUtils.printDuration(timed.duration())));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection("%s".formatted(timed.result())), null);
    }

    public List<String> getInputData() throws IOException {
        final String file = "%d_%02d_input.txt".formatted(this.year, this.day);
        return Files.readAllLines(
                systemUtils.getMemoDir().resolve(file),
                Charset.forName(this.charset));
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }
	
	protected void setTrace(final boolean trace) {
	    this.trace = true;
	    this.logger.setTrace(trace);
	}
}
