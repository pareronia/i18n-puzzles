import sys
from collections import Counter
from datetime import datetime
from datetime import timezone

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
2019-06-05T08:15:00-04:00
2019-06-05T14:15:00+02:00
2019-06-05T17:45:00+05:30
2019-06-05T05:15:00-07:00
2011-02-01T09:15:00-03:00
2011-02-01T09:15:00-05:00
"""

Output = str


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        return (
            Counter(datetime.fromisoformat(line) for line in input)
            .most_common(1)[0][0]
            .astimezone(timezone.utc)
            .isoformat()
        )

    @i18n_puzzles_samples(((TEST, "2019-06-05T12:15:00+00:00"),))
    def samples(self) -> None:
        pass


solution = Solution(2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
