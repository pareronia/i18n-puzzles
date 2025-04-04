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
java_lib_dir := env("JAVA_LIB_DIR")
java_libs := join(java_lib_dir, "spring-security-crypto-6.4.4.jar")
rmdir := "rm --recursive --force"
maven_repo := "https://repo1.maven.org/maven2"
wget := "wget --no-verbose --no-clobber"

default:
    @just --choose

get-java-libs:
    @{{wget}} --directory-prefix="{{java_lib_dir}}" \
        {{maven_repo}}/org/springframework/security/spring-security-crypto/6.4.4/spring-security-crypto-6.4.4.jar

build-java: get-java-libs
    @{{javac}} -cp "{{java_libs}}" -d "{{java_dst}}" {{java_srcs}}

run year day:
    @{{java}} -cp "{{java_libs}}:{{java_dst}}" {{java_run_class}} {{year}} {{day}}

run-all:
    @{{java}} -cp "{{java_libs}}:{{java_dst}}" {{java_run_class}}

clean-java:
    @{{rmdir}} {{java_dst}} {{java_lib_dir}}
