import sys

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples
from i18n_puzzles.common import to_blocks

TEST = """\
geléet
träffs
religiÃ«n
tancées
kÃ¼rst
roekoeÃ«n
skälen
böige
fÃ¤gnar
dardÃ©es
amènent
orquestrÃ¡
imputarão
molières
pugilarÃÂ£o
azeitámos
dagcrème
zÃ¶ger
ondulât
blÃ¶kt

   ...d...
    ..e.....
     .l...
  ....f.
......t..
"""

Output = int


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def get_word(line: str, idx: int) -> str:
            def decode(line: str) -> str:
                return str(
                    bytes(line, encoding="iso-8859-1"),
                    encoding="utf-8",
                    errors="ignore",
                )

            if idx % 3 == 0 or idx % 5 == 0:
                if idx % 15 == 0:
                    return decode(decode(line))
                return decode(line)
            return line

        def match_word(line: str, words: list[str]) -> int:
            for i, ch in enumerate(line):
                if line[i] == ".":
                    continue
                for j, word in enumerate(words):
                    if (
                        len(word) == len(line)
                        and word[i].lower() == ch.lower()
                    ):
                        return j + 1
            raise RuntimeError

        blocks = to_blocks(input)
        words = [
            get_word(line, i) for i, line in enumerate(blocks[0], start=1)
        ]
        return sum(match_word(line.strip(), words) for line in blocks[1])

    @i18n_puzzles_samples(((TEST, 50),))
    def samples(self) -> None:
        pass


solution = Solution(6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
