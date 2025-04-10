from __future__ import annotations

import time
from abc import ABC
from abc import abstractmethod
from pathlib import Path
from typing import Any
from typing import Callable
from typing import Generic
from typing import Iterable
from typing import NamedTuple
from typing import TypeVar
from typing import cast

import i18n_puzzles.memo as memo
from i18n_puzzles.format import fmt_answer
from i18n_puzzles.format import fmt_duration
from i18n_puzzles.format import fmt_title
from prettyprinter import cpprint


def clog(c: Callable[[], object]) -> None:
    if __debug__:
        log(c())


def log(msg: object) -> None:
    if __debug__:
        cpprint(msg)


def to_blocks(inputs: Iterable[str]) -> list[list[str]]:
    blocks = list[list[str]]()
    idx = 0
    blocks.append([])
    for input in inputs:
        if len(input) == 0:
            blocks.append([])
            idx += 1
        else:
            blocks[idx].append(input)
    return blocks


InputData = tuple[str, ...]
OUTPUT = TypeVar("OUTPUT", bound=str | int)


class Puzzle:
    def __init__(self, puzzle: int):
        self.puzzle = puzzle

    def get_input(self) -> tuple[str, ...] | None:
        return memo.get_input(self.puzzle)


class SolutionBase(ABC, Generic[OUTPUT]):
    class Execution(NamedTuple):
        answer: Any = None
        duration: int = 0
        no_input: bool = False

        @property
        def duration_as_ms(self) -> float:
            return self.duration / 1_000_000

    def __init__(self, puzzle: int):
        self.puzzle = Puzzle(puzzle)

    def get_resource(self, path: Path) -> Path:
        return Path(__file__).parent.parent.parent.joinpath(
            "resources", str(self.puzzle.puzzle), path
        )

    @abstractmethod
    def samples(self) -> None:
        pass

    @abstractmethod
    def solve(self, input: InputData) -> OUTPUT:
        pass

    def execute(self) -> SolutionBase.Execution:
        input = self.puzzle.get_input()
        if input is None:
            return SolutionBase.Execution(no_input=True)
        else:
            start = time.time()
            answer = self.solve(input)
            return SolutionBase.Execution(
                answer, int((time.time() - start) * 1e9)
            )

    def run(self, main_args: list[str]) -> None:  # noqa E103
        print()
        print(fmt_title(self.puzzle.puzzle))
        print()
        if __debug__:
            self.samples()
        result = self.execute()
        if result.no_input:
            print("== NO INPUT FOUND ==")
        else:
            answer = fmt_answer(result.answer)
            duration = fmt_duration(result.duration_as_ms)
            print(f"Answer: {answer}, took {duration}")


F = TypeVar("F", bound=Callable[..., Any])


def i18n_puzzles_samples(
    tests: tuple[tuple[str, Any], ...],
) -> Callable[[F], F]:
    def decorator(func: F) -> F:
        def wrapper(*args: Any) -> Any:
            _self = args[0]
            for test in tests:
                func = "solve"
                input, expected = test
                input_data = tuple(_ for _ in input.splitlines())
                actual = getattr(_self, func)(input_data)
                message = f"FAILED Expected: '{expected}', was: '{actual}'"
                assert actual == expected, message

        return cast(F, wrapper)

    return decorator
