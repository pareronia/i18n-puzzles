import sys

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
σζμ γ' ωοωλδθαξλδμξρ οπξρδυζ οξκτλζσθρ Ξγτρρδτρ.
αφτ κ' λαλψφτ ωπφχλρφτ δξησηρζαλψφτ φελο, Φκβωωλβ.
γ βρφαγζ ωνψν ωγφ πγχρρφ δρδαθωραγζ ρφανφ.
"""

Output = int
VARIANTS = {
    "Οδυσσευς".upper(),
    "Οδυσσεως".upper(),
    "Οδυσσει".upper(),
    "Οδυσσεα".upper(),
    "Οδυσσευ".upper(),
}
UPPER = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ"


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        return sum(
            i
            for line in input
            for i, test in enumerate(
                "".join(
                    UPPER[(UPPER.find(ch) + j) % len(UPPER)]
                    for ch in line.upper()
                )
                for j in range(len(UPPER))
            )
            if any(v in test for v in VARIANTS)
        )

    @i18n_puzzles_samples(((TEST, 19),))
    def samples(self) -> None:
        pass


solution = Solution(11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
