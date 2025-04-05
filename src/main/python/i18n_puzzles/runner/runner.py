import importlib
import types
from argparse import ArgumentParser

from ..format import fmt_answer
from ..format import fmt_duration
from ..format import fmt_title


class Runner:
    def __init__(self, number_of_puzzles: int) -> None:
        self.number_of_puzzles = number_of_puzzles

    def get_module(self, problem: int) -> types.ModuleType | None:
        try:
            return importlib.import_module(f"Puzzle2025_{problem:0>2}")
        except ModuleNotFoundError:
            return None

    def run(self, day_mod: types.ModuleType) -> None:
        result = day_mod.solution.execute()
        title = fmt_title(day_mod.solution.puzzle.puzzle)
        answer = fmt_answer(result.answer)
        time = fmt_duration(result.duration_as_ms)
        print(f"{title} : {answer} ({time})")

    def main(self, _main_args: list[str]) -> None:
        parser = ArgumentParser(
            prog="Internationalization Puzzles",
            description="i18n-puzzles Puzzle runner",
        )
        parser.add_argument(
            "-p",
            "--puzzle",
            type=int,
            nargs=1,
            help="Number of Puzzle to run.",
        )
        parser.add_argument(
            "-a",
            "--all",
            action="store_true",
            help="Run all Puzzles",
        )
        args = parser.parse_args()
        if args.all:
            for problem in range(1, self.number_of_puzzles + 1):
                day_mod = self.get_module(problem)
                if day_mod is None:
                    continue
                self.run(day_mod)
        else:
            day_mod = self.get_module(args.puzzle[0])
            if day_mod is None:
                return
            self.run(day_mod)
