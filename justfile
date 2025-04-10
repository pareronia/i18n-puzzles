set dotenv-load
set dotenv-required

alias l := lint

java_src_dir := join(".", "src", "main", "java")
java_srcs := shell('find . -name "*.java" | tr "\n" " "')
java_dst := join(".", "build", "java")
java_package := "com.github.pareronia.i18n_puzzles"
java_run_class := java_package + ".PuzzleRunner"
javac := env("JAVAC_EXE")
java := env("JAVA_EXE")
java_lib_dir := env("JAVA_LIB_DIR")
java_libs := join(java_lib_dir, "spring-security-crypto-6.4.4.jar")

python_src_dir := join(".", "src", "main", "python")
export PYTHONPATH := python_src_dir
python := if os_family() == "windows" { "python -O" } else { "python3 -O" }
python_dev := if os_family() == "windows" { "python" } else { "python3" }

resources_dir := join(".", "src", "main", "resources")

rmdir := "rm --recursive --force"
maven_repo := "https://repo1.maven.org/maven2"
wget := "wget --no-verbose --no-clobber"

default:
    @just --choose

[group("java")]
get-java-libs:
    @{{wget}} --directory-prefix="{{java_lib_dir}}" \
        {{maven_repo}}/org/springframework/security/spring-security-crypto/6.4.4/spring-security-crypto-6.4.4.jar

[group("java")]
build-java: get-java-libs
    @{{javac}} -cp "{{java_libs}}" -d "{{java_dst}}" {{java_srcs}}
    @cp --recursive "{{resources_dir}}"/* "{{java_dst}}"

[group("java")]
run-java year day:
    @{{java}} -cp "{{java_libs}}:{{java_dst}}" {{java_run_class}} {{year}} {{day}}

# Run all Puzzles
[group("java")]
run-all-java:
    @{{java}} -cp "{{java_libs}}:{{java_dst}}" {{java_run_class}}

[group("java")]
clean-java:
    @{{rmdir}} {{java_dst}} {{java_lib_dir}}

# Run Puzzle by number
[group("python")]
run-py puzzle:
    @{{python}} -m i18n_puzzles.runner --puzzle {{puzzle}}

# Run all Puzzles
[group("python")]
run-all-py:
    @{{python}} -m i18n_puzzles.runner --all

# Regenerate table
table:
    @{{python}} -m i18n_puzzles.table README.md && git diff README.md

[group("vim")]
vim-file-run-dev file $LOGLEVEL="DEBUG":
    @echo {{CLEAR}}
    @{{python_dev}} "{{file}}"

[group("vim")]
vim-file-run file:
    @echo {{CLEAR}}
    @{{python}} "{{file}}"

[group("vim")]
vim-file-debug file:
    @echo {{CLEAR}}
    @{{python_dev}} -m pdb "{{file}}"

# Linting: flake8
[group("linting")]
flake8:
    @echo "Running flake8"
    @flake8 "{{python_src_dir}}"

# Linting: bandit - security analyzer
[group("linting")]
bandit:
    @echo "Running bandit"
    @bandit --configfile pyproject.toml --quiet --recursive "{{python_src_dir}}"

# Linting: vulture - unused code
[group("linting")]
vulture:
    @echo "Running vulture"
    @vulture "{{python_src_dir}}"

# Linting: black - code formating
[group("linting")]
black-check:
    @echo "Running black check"
    @black --quiet --diff --color --check "{{python_src_dir}}"

# Linting: mypy - type formating
[group("linting")]
mypy:
    @echo "Running mypy"
    @mypy --no-error-summary "{{python_src_dir}}"

# Linting: all
[group("linting")]
lint: flake8 vulture bandit black-check mypy

# git hook
pre-push: lint
