set windows-shell := ["powershell.exe", "-c"]
set dotenv-load
set dotenv-required

alias r := run

java_src := `$files = Get-ChildItem -Path . -Filter "*.java" -Recurse -File | Select-Object -ExpandProperty FullName; $files -join " "`
java_dst := justfile_directory() + "/build/java"
java_package := "com.github.pareronia.i18n_puzzles"
javac := env("JAVA_HOME") + "/bin/javac"
java := env("JAVA_HOME") + "/bin/java -ea"

default:
    @just --choose

build-java:
    @{{javac}} -d {{java_dst}} {{java_src}}

run year day:
    @{{java}} -cp {{java_dst}} {{java_package}}.PuzzleRunner {{year}} {{day}}

run-all:
    @{{java}} -cp {{java_dst}} {{java_package}}.PuzzleRunner

[windows]
clean-java:
    @Remove-Item -Force -Recurse {{java_dst}}