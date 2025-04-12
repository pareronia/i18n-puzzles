import sys
import unicodedata
from collections import deque
from functools import cache

import bcrypt
from i18n_puzzles.common import InputData
from i18n_puzzles.common import SolutionBase
from i18n_puzzles.common import i18n_puzzles_samples
from i18n_puzzles.common import to_blocks

TEST = """\
etasche $2b$07$0EBrxS4iHy/aHAhqbX/ao.n7305WlMoEpHd42aGKsG21wlktUQtNu
mpataki $2b$07$bVWtf3J7xLm5KfxMLOFLiu8Mq64jVhBfsAwPf8/xx4oc5aGBIIHxO
ssatterfield $2b$07$MhVCvV3kZFr/Fbr/WCzuFOy./qPTyTVXrba/2XErj4EP3gdihyrum
mvanvliet $2b$07$gf8oQwMqunzdg3aRhktAAeU721ZWgGJ9ZkQToeVw.GbUlJ4rWNBnS
vbakos $2b$07$UYLaM1I0Hy/aHAhqbX/ao.c.VkkUaUYiKdBJW5PMuYyn5DJvn5C.W
ltowne $2b$07$4F7o9sxNeaPe..........l1ZfgXdJdYtpfyyUYXN/HQA1lhpuldO

etasche .pM?XÑ0i7ÈÌ
mpataki 2ö$p3ÄÌgÁüy
ltowne 3+sÍkÜLg._
ltowne 3+sÍkÜLg?_
mvanvliet *íÀŸä3hñ6À
ssatterfield 8É2U53N~Ë
mpataki 2ö$p3ÄÌgÁüy
mvanvliet *íÀŸä3hñ6À
etasche .pM?XÑ0i7ÈÌ
ssatterfield 8É2U53L~Ë
mpataki 2ö$p3ÄÌgÁüy
vbakos 1F2£èÓL
"""

Output = int


class Solution(SolutionBase[Output]):
    def solve(self, input: InputData) -> Output:
        @cache
        def check(secret: str, hash: str) -> bool:
            def variants(secret: str) -> set[str]:
                ans = set[str]()
                q = deque[tuple[int, str]]()
                q.append((0, ""))
                while q:
                    size, var = q.popleft()
                    if size == len(secret):
                        ans.add(var)
                    else:
                        c = secret[size]
                        n = unicodedata.normalize("NFD", c)
                        if n != c:
                            q.append((size + 1, var + n))
                        q.append((size + 1, var + c))
                return ans

            hashed = hash.encode()
            return any(
                bcrypt.checkpw(s.encode(), hashed) for s in variants(secret)
            )

        blocks = to_blocks(input)
        auths = {a: b for a, b in map(lambda line: line.split(), blocks[0])}
        return sum(
            check(unicodedata.normalize("NFC", attempt), auths[username])
            for username, attempt in map(lambda line: line.split(), blocks[1])
        )

    @i18n_puzzles_samples(((TEST, 4),))
    def samples(self) -> None:
        pass


solution = Solution(10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
