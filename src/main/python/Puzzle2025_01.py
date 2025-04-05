import sys

from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples

TEST = """\
néztek bele az „ártatlan lapocskába“, mint ahogy belenézetlen mondták ki rá a\
 halálos itéletet a sajtó csupa 20–30 éves birái s egyben hóhérai.
livres, et la Columbiad Rodman ne dépense que cent soixante livres de poudre\
 pour envoyer à six milles son boulet d'une demi-tonne.  Ces
Люди должны были тамъ и сямъ жить въ палаткахъ, да и мы не были помѣщены въ\
 посольскомъ дворѣ, который также сгорѣлъ, а въ двухъ деревянныхъ
Han hade icke träffat Märta sedan Arvidsons middag, och det hade gått nära\
 en vecka sedan dess. Han hade dagligen promenerat på de gator, där
"""

Output = int
SIZE_SMS, SIZE_TWEET = 160, 140
RATE_SMS, RATE_TWEET, RATE_BOTH = 11, 7, 13


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        def rate(line: str) -> int:
            byteses = bytes(line, "utf-8")
            chars = len(str(byteses, "utf-8"))
            if chars <= SIZE_TWEET and len(byteses) <= SIZE_SMS:
                return RATE_BOTH
            elif chars <= SIZE_TWEET:
                return RATE_TWEET
            elif len(byteses) <= SIZE_SMS:
                return RATE_SMS
            else:
                return 0

        return sum(map(rate, input))

    @i18n_puzzles_samples(((TEST, 31),))
    def samples(self) -> None:
        pass


solution = Solution(1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
