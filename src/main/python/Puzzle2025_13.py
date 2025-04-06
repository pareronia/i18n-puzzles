import sys

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples
from i18n_puzzles.common import to_blocks

TEST = """\
616e77c3a4686c65
796c74e46de47373e4
efbbbf73796b6b696dc3a46bc3b6
0070006f00eb0065006d
feff0069007400e4007000e400e4006800e4006e
61757373e46774
626c6173c3a9
637261776cc3a9
6c00e20063006800e2007400
64657370656e68e1
6c6964e172656973
fffe6700e20063006800e9006500
6500700069007400e100660069006f00
feff007300fc006e006400650072006e
fffe7200f600730074006900

   ..s....
  ...w..
 ....i
.....f..
    .t.......
"""

Output = int


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def get_word(line: str) -> str:
            b = bytes.fromhex(line)
            if line.startswith("efbbbf"):
                return str(b[3:], encoding="utf-8")
            if line.startswith("feff") or line.startswith("fffe"):
                return str(b, encoding="utf-16")
            for enc in ["utf-8", "iso-8859-1", "utf-16le", "utf-16be"]:
                try:
                    s = str(b, encoding=enc)
                    if all(ch.isalnum() for ch in s):
                        return s
                except UnicodeDecodeError:
                    pass
            raise RuntimeError

        def match_word(line: str, words: list[str]) -> int:
            for i, ch in enumerate(line):
                if line[i] == ".":
                    continue
                for j, word in enumerate(words):
                    if len(word) == len(line) and word[i] == ch:
                        return j + 1
            raise RuntimeError

        blocks = to_blocks(input)
        words = [get_word(line) for line in blocks[0]]
        return sum(match_word(line.strip(), words) for line in blocks[1])

    @i18n_puzzles_samples(((TEST, 47),))
    def samples(self) -> None:
        pass


solution = Solution(13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
