import os
import sys

from . import I18N_PUZZLES_DIR


def get_memo_dir() -> str:
    if I18N_PUZZLES_DIR in os.environ:
        return os.environ[I18N_PUZZLES_DIR]
    if sys.platform.startswith("win"):
        return os.path.join(os.environ["APPDATA"], "i18n-puzzles")
    if sys.platform.startswith("linux"):
        return os.path.join(os.environ["HOME"], ".config", "i18n-puzzles")
    raise RuntimeError("OS not supported")


def get_input_file(problem: int) -> str:
    return os.path.join(get_memo_dir(), f"2025_{problem:>02}_input.txt")


def get_input(problem: int) -> tuple[str, ...] | None:
    file = get_input_file(problem)
    if not os.path.exists(file):
        return None
    return tuple(_ for _ in read_lines_from_file(file))


def read_lines_from_file(file: str) -> list[str]:
    with open(file, "r", encoding="utf-8") as f:
        data = f.read()
    return data.rstrip("\r\n").splitlines()
