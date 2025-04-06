import operator
import sys
import unicodedata
from collections import Counter
from functools import reduce

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
iS0
V8AeC1S7KhP4Ļu
pD9Ĉ*jXh
E1-0
ĕnz2cymE
tqd~üō
IgwQúPtd9
k2lp79ąqV
"""

Output = int


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def is_valid(line: str) -> bool:
            normalized = unicodedata.normalize("NFD", line.lower())
            c = Counter(ch for ch in normalized if ch.isalpha())
            return (
                4 <= len(line) <= 12
                and not any(v > 1 for v in c.values())
                and reduce(
                    operator.or_,
                    map(
                        lambda ch: ch.isnumeric() * 0b001
                        + (ch in {"a", "e", "i", "o", "u"}) * 0b010
                        + (
                            ch.isalpha()
                            and ch not in {"a", "e", "i", "o", "u"}
                        )
                        * 0b100,
                        (ch for ch in normalized),
                    ),
                )
                == 0b111
            )

        return sum(is_valid(line) for line in input)

    @i18n_puzzles_samples(((TEST, 2),))
    def samples(self) -> None:
        pass


solution = Solution(8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
