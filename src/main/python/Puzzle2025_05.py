import sys

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase

TEST = (
    " ⚘   ⚘ ",
    "  ⸫   ⸫",
    "🌲   💩  ",
    "     ⸫⸫",
    " 🐇    💩",
    "⸫    ⸫ ",
    "⚘🌲 ⸫  🌲",
    "⸫    🐕 ",
    "  ⚘  ⸫ ",
    "⚘⸫⸫   ⸫",
    "  ⚘⸫   ",
    " 💩  ⸫  ",
    "     ⸫⸫",
)

Output = int
POO = "💩"


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        return sum(
            line[(i * 2) % len(line)] == POO for i, line in enumerate(input)
        )

    def samples(self) -> None:
        assert self.solve(TEST) == 2


solution = Solution(5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
