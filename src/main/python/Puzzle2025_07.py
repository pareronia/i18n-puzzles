import sys
import zoneinfo
from datetime import datetime
from datetime import timedelta

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
2012-11-05T09:39:00.000-04:00   969     3358
2012-05-27T17:38:00.000-04:00   2771    246
2001-01-15T22:27:00.000-03:00   2186    2222
2017-05-15T07:23:00.000-04:00   2206    4169
2005-09-02T06:15:00.000-04:00   1764    794
2008-03-23T05:02:00.000-03:00   1139    491
2016-03-11T00:31:00.000-04:00   4175    763
2015-08-14T12:40:00.000-03:00   3697    568
2013-11-03T07:56:00.000-04:00   402     3366
2010-04-16T09:32:00.000-04:00   3344    2605
"""

Output = int
HAL = zoneinfo.ZoneInfo("America/Halifax")
SAN = zoneinfo.ZoneInfo("America/Santiago")


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def corrected(line: str) -> datetime:
            dt_, *minutes = line.split()
            dt = datetime.fromisoformat(dt_)
            tz = HAL if HAL.utcoffset(dt) == dt.utcoffset() else SAN
            dt += timedelta(minutes=int(minutes[0]) - int(minutes[1]))
            return dt.astimezone(tz)

        return sum(
            i * corrected(line).hour for i, line in enumerate(input, start=1)
        )

    @i18n_puzzles_samples(((TEST, 866),))
    def samples(self) -> None:
        pass


solution = Solution(7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
