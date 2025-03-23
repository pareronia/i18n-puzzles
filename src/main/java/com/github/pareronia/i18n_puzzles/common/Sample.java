package com.github.pareronia.i18n_puzzles.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Sample {

    String method() default "solve";
    String input();
    String charset() default "UTF-8";
    String expected();
    boolean debug() default true;
}
