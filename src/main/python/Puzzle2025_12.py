from __future__ import annotations

import contextlib
import itertools
import locale
import sys
from math import prod
from typing import Any
from typing import Generator
from typing import NamedTuple

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
Ñíguez Peña, María de los Ángeles: 0151605
Åberg, Rosa-Maria: 0110966
Özaydın, Zeynep: 0185292
van den Heyden, Harm: 0168131
Ämtler, Lorena: 0112717
Olofsson, Mikael: 0103652
van Leeuwen, Joke: 0172199
Vandersteen, Willy: 0120659
Østergård, Magnus: 0113959
van Leeuw, Floor: 0144158
Navarrete Ortiz, Dolores: 0119411
Aalto, Alvar: 0192872
Zondervan, Jan Peter: 0103008
Æbelø, Aurora: 0113267
O'Neill, Cara: 0109551
"""

Output = int
DUTCH = "nl_NL"
ENGLISH = "en"
SWEDISH = "sv"


@contextlib.contextmanager
def temp_locale(new_locale: str) -> Generator[Any]:
    original_locale = locale.setlocale(locale.LC_ALL)
    try:
        yield locale.setlocale(locale.LC_ALL, new_locale)
    finally:
        locale.setlocale(locale.LC_ALL, original_locale)


class Entry(NamedTuple):
    last: str
    infixes: str
    first: str
    phone: int

    @classmethod
    def from_input(cls, line: str) -> Entry:
        person, phone = line.split(": ")
        last_name, first_name = person.split(", ")
        infixes = "".join(
            itertools.takewhile(lambda ch: ch.islower(), last_name.split())
        )
        last = "".join(
            itertools.dropwhile(lambda ch: ch.islower(), last_name.split())
        )
        return cls(
            last.replace("'", "").replace(" ", ""),
            infixes.replace("'", "").replace(" ", ""),
            first_name.replace("'", "").replace(" ", ""),
            int(phone),
        )

    def collation_key(self, loc: str) -> str:
        if loc == DUTCH:
            return self.last
        elif loc == SWEDISH:
            return (self.infixes + self.last).replace("Æ", "Å")
        else:
            return self.infixes + self.last


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def sort(loc: str) -> list[Entry]:
            with temp_locale(loc):
                return sorted(
                    entries, key=lambda e: locale.strxfrm(e.collation_key(loc))
                )

        entries = [Entry.from_input(line) for line in input]
        return prod(
            sort(loc)[len(entries) // 2].phone
            for loc in {ENGLISH, DUTCH, SWEDISH}
        )

    @i18n_puzzles_samples(((TEST, 1885816494308838),))
    def samples(self) -> None:
        pass


solution = Solution(12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
