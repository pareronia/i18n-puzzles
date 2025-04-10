import itertools
import sys
from collections import defaultdict
from datetime import UTC
from datetime import datetime
from pathlib import Path
from typing import Iterator
from zoneinfo import ZoneInfo

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
2024-04-09 18:49:00; Africa/Casablanca
2024-04-10 02:19:00; Asia/Pyongyang
2024-04-10 04:49:00; Antarctica/Casey
2024-04-12 12:13:00; Asia/Pyongyang
2024-04-12 15:54:00; Africa/Casablanca
2024-04-12 16:43:00; Africa/Casablanca
2024-04-13 00:24:00; Asia/Pyongyang
2024-04-13 01:54:00; Antarctica/Casey
2024-04-13 07:43:00; Antarctica/Casey
"""

Output = str


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def timestamps(d: dict[str, set[str]]) -> Iterator[tuple[str, float]]:
            for ver, zone in itertools.product(
                {"2018c", "2018g", "2021b", "2023d"}, d
            ):
                with open(
                    self.get_resource(Path(ver).joinpath(zone)), "rb"
                ) as f:
                    tz = ZoneInfo.from_file(f)
                for date in d[zone]:
                    timestamp = (
                        datetime.fromisoformat(date)
                        .replace(tzinfo=tz)
                        .timestamp()
                    )
                    yield (zone, timestamp)

        d = defaultdict[str, set[str]](set)
        for date, zone in map(lambda line: line.split("; "), input):
            d[zone].add(date)
        c = defaultdict[float, set[str]](set)
        for zone, timestamp in timestamps(d):
            c[timestamp].add(zone)
        ans = next(
            timestamp for timestamp, zones in c.items() if zones == d.keys()
        )
        return datetime.fromtimestamp(ans, UTC).isoformat()

    @i18n_puzzles_samples(((TEST, "2024-04-09T17:49:00+00:00"),))
    def samples(self) -> None:
        pass


solution = Solution(19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
