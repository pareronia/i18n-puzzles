set dotenv-load
set dotenv-required

alias r := run

java_src_dir := join(".", "src", "main", "java")
java_srcs := shell('find . -name "*.java" | tr "\n" " "')
java_dst := join(".", "build", "java")
java_package := "com.github.pareronia.i18n_puzzles"
java_run_class := java_package + ".PuzzleRunner"
javac := env("JAVAC_EXE")
java := env("JAVA_EXE")
rmdir := "rm --recursive --force"

default:
    @just --choose

build-java:
    @{{javac}} -d {{java_dst}} {{java_srcs}}

run year day:
    @{{java}} -cp {{java_dst}} {{java_run_class}} {{year}} {{day}}

run-all:
    @{{java}} -cp {{java_dst}} {{java_run_class}}

clean-java:
    @{{rmdir}} {{java_dst}}
