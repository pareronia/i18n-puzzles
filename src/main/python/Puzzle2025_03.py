import operator
import sys
from functools import reduce

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
d9Ō
uwI.E9GvrnWļbzO
ž-2á
Ģ952W*F4
?O6JQf
xi~Rťfsa
r_j4XcHŔB
71äĜ3
"""

Output = int


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def is_valid(line: str) -> bool:
            return (
                4 <= len(line) <= 12
                and reduce(
                    operator.or_,
                    map(
                        lambda ch: ch.isnumeric() * 0b0001
                        + ch.isupper() * 0b0010
                        + ch.islower() * 0b0100
                        + (ord(ch) > 127) * 0b1000,
                        (ch for ch in line),
                    ),
                )
                == 0b1111
            )

        return sum(is_valid(line) for line in input)

    @i18n_puzzles_samples(((TEST, 2),))
    def samples(self) -> None:
        pass


solution = Solution(3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
