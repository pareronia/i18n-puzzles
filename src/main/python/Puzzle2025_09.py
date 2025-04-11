from __future__ import annotations

import itertools
import operator
import sys
from datetime import datetime
from enum import Enum
from enum import unique
from functools import reduce

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
16-05-18: Margot, Frank
02-17-04: Peter, Elise
06-02-29: Peter, Margot
31-09-11: Elise, Frank
09-11-01: Peter, Frank, Elise
11-09-01: Margot, Frank
"""

Output = str
NINE_ELEVEN = datetime(year=2001, month=9, day=11)


@unique
class DateFormat(Enum):
    format: str
    mask: int

    def __new__(cls, format: str, mask: int) -> DateFormat:
        obj = object.__new__(cls)
        obj._value_ = format
        obj.mask = mask
        return obj

    DMY = ("%d-%m-%y", 0b0001)
    MDY = ("%m-%d-%y", 0b0010)
    YMD = ("%y-%m-%d", 0b0100)
    YDM = ("%y-%d-%m", 0b1000)

    def is_valid(self, date: str) -> bool:
        try:
            datetime.strptime(date, self.value)
            return True
        except ValueError:
            return False


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def parse(line: str) -> tuple[str, list[str]]:
            date, names = line.split(": ")
            return date, names.split(", ")

        entries = [parse(line) for line in input]
        d = dict[str, int]()
        for date, names in entries:
            matches = reduce(
                operator.or_,
                (df.mask for df in DateFormat if df.is_valid(date)),
            )
            for name in names:
                d[name] = d[name] & matches if d.get(name) else matches
        return " ".join(
            sorted(
                name
                for date, names in entries
                for name, df in itertools.product(names, DateFormat)
                if (
                    df.mask & d[name]
                    and NINE_ELEVEN.strftime(df.value) == date
                )
            )
        )

    @i18n_puzzles_samples(((TEST, "Margot Peter"),))
    def samples(self) -> None:
        pass


solution = Solution(9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
