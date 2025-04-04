import os


class Table:
    def __init__(self, number_of_problems: int) -> None:
        self.number_of_problems = number_of_problems

    def get_path_java(self, puzzle: int) -> str:
        return "/".join(
            [
                "src",
                "main",
                "java",
                "com",
                "github",
                "pareronia",
                "i18n_puzzles",
                f"Puzzle2025_{puzzle:02}.java",
            ]
        )

    def get_progress_bar(self, title: str, scale: int, value: int) -> str:
        return (
            f"![](https://progress-bar.xyz/{value}?title={title}"
            f"&scale={scale}&suffix=/{scale}&progress_background=ff3300)"
        )

    def get_java(self, puzzle: int) -> str:
        path = self.get_path_java(puzzle)
        return f"[👑]({path})" if os.path.exists(path) else ""

    def get_progress_bar_java(self) -> str:
        cnt = sum(
            os.path.exists(self.get_path_java(puzzle))
            for puzzle in range(1, self.number_of_problems + 1)
        )
        return self.get_progress_bar("java", self.number_of_problems, cnt)

    def generate(self, file_name: str) -> None:
        url = "https://i18n-puzzles.com/puzzle/"
        with open(file_name, "r", encoding="utf-8") as f:
            tmp = f.read()
        with open(file_name, "w", encoding="utf-8") as f:
            in_table = False
            for line in tmp.splitlines():
                if line.startswith("<!-- @BEGIN:Puzzles"):
                    in_table = True
                    print(line, file=f)
                    print(
                        f"| Puzzle | {self.get_progress_bar_java()} |",
                        file=f,
                    )
                    print("| :---: | :---: |", file=f)
                    for puzzle in range(1, self.number_of_problems + 1):
                        java = self.get_java(puzzle)
                        line = f"|[{puzzle}]({url}{puzzle})|{java}|"
                        print(line, file=f)
                elif line.startswith("<!-- @END:Puzzles"):
                    in_table = False
                    print(line, file=f)
                else:
                    if not in_table:
                        print(line, file=f)
