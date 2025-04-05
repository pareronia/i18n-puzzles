from typing import Any

from termcolor import colored


def fmt_duration(duration_as_ms: float) -> str:
    if duration_as_ms <= 1_000:
        return f"{duration_as_ms:.3f} ms"
    elif duration_as_ms <= 5_000:
        return colored(f"{duration_as_ms:.0f} ms", "yellow")
    else:
        return colored(f"{duration_as_ms:.0f} ms", "red")


def fmt_title(problem: int) -> str:
    return colored(f"i18n Problem {problem:>2}", "yellow")


def fmt_answer(answer: Any) -> str:
    return str(colored(answer, "white", attrs=["bold"]))
