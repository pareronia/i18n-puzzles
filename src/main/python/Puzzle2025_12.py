from __future__ import annotations

import contextlib
import itertools
import locale
import sys
import unicodedata
from enum import Enum
from enum import auto
from enum import unique
from functools import cmp_to_key
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
DUTCH, ENGLISH, SWEDISH = "nl_NL", "en", "sv"
ALFABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
SWEDISH_ALFABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ"


@contextlib.contextmanager
def temp_locale(new_locale: str) -> Generator[Any]:
    original_locale = locale.setlocale(locale.LC_ALL)
    try:
        yield locale.setlocale(locale.LC_ALL, new_locale)
    finally:
        locale.setlocale(locale.LC_ALL, original_locale)


def locale_available() -> bool:
    cnt = 0
    for loc in {DUTCH, ENGLISH, SWEDISH}:
        try:
            with temp_locale(loc):
                cnt += 1
        except locale.Error:
            pass
    return cnt == 3


@unique
class Mode(Enum):
    WITH_LOCALE = auto()
    WITHOUT_LOCALE = auto()

    def sort(self, entries: list[Entry], loc: str) -> list[Entry]:
        def cmp_without_locale(e1: Entry, e2: Entry) -> int:
            def unidecode(s: str) -> str:
                return "".join(
                    (ch if ch in ALFABET else "")
                    for ch in unicodedata.normalize("NFD", s)
                )

            if loc == SWEDISH:
                s1, s2 = (
                    e.last_name(loc).replace("Æ", "Ä").replace("Ø", "Ö")
                    for e in [e1, e2]
                )
                if s1 == s2:
                    return 0
                for ch1, ch2 in zip(s1, s2):
                    if ch1 not in SWEDISH_ALFABET:
                        ch1 = unidecode(ch1)
                    if ch2 not in SWEDISH_ALFABET:
                        ch2 = unidecode(ch2)
                    if ch1 == ch2:
                        continue
                    return SWEDISH_ALFABET.index(ch1) - SWEDISH_ALFABET.index(
                        ch2
                    )
                return len(s1) - len(s2)
            else:
                d1, d2 = (
                    unidecode(
                        e.last_name(loc)
                        .replace("Æ", "A")
                        .replace("Ä", "A")
                        .replace("Å", "A")
                        .replace("Ø", "O")
                    )
                    for e in [e1, e2]
                )
                return 0 if d1 == d2 else -1 if d1 < d2 else 1

        def cmp_with_locale(e1: Entry, e2: Entry) -> int:
            if loc == SWEDISH:
                return locale.strcoll(
                    e1.last_name(loc).replace("Æ", "Ä"),
                    e2.last_name(loc).replace("Æ", "Ä"),
                )
            else:
                return locale.strcoll(e1.last_name(loc), e2.last_name(loc))

        match self:
            case Mode.WITH_LOCALE:
                with temp_locale(loc):
                    return sorted(entries, key=cmp_to_key(cmp_with_locale))
            case Mode.WITHOUT_LOCALE:
                return sorted(entries, key=cmp_to_key(cmp_without_locale))


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
            last.replace("'", "").replace(" ", "").upper(),
            infixes.replace("'", "").replace(" ", "").upper(),
            first_name.replace("'", "").replace(" ", "").upper(),
            int(phone),
        )

    def last_name(self, loc: str) -> str:
        return self.last if loc == DUTCH else self.infixes + self.last


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        mode = Mode.WITH_LOCALE if locale_available() else Mode.WITHOUT_LOCALE
        entries = [Entry.from_input(line) for line in input]
        return prod(
            mode.sort(entries, loc)[len(entries) // 2].phone
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
