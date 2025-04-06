import sys
import zoneinfo
from datetime import datetime

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
Departure: Europe/London                  Mar 04, 2020, 10:00
Arrival:   Europe/Paris                   Mar 04, 2020, 11:59

Departure: Europe/Paris                   Mar 05, 2020, 10:42
Arrival:   Australia/Adelaide             Mar 06, 2020, 16:09

Departure: Australia/Adelaide             Mar 06, 2020, 19:54
Arrival:   America/Argentina/Buenos_Aires Mar 06, 2020, 19:10

Departure: America/Argentina/Buenos_Aires Mar 07, 2020, 06:06
Arrival:   America/Toronto                Mar 07, 2020, 14:43

Departure: America/Toronto                Mar 08, 2020, 04:48
Arrival:   Europe/London                  Mar 08, 2020, 16:52
"""

Output = int


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        return sum(
            e if i % 2 else -e
            for i, e in enumerate(
                int(
                    datetime.strptime(line[42:], "%b %d, %Y, %H:%M")
                    .replace(tzinfo=zoneinfo.ZoneInfo(line.split()[1]))
                    .timestamp()
                    // 60
                )
                for line in input
                if len(line) != 0
            )
        )

    @i18n_puzzles_samples(((TEST, 3143),))
    def samples(self) -> None:
        pass


solution = Solution(4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
