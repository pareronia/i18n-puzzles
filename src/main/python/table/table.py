import os


class Table:
    def __init__(self, number_of_problems: int) -> None:
        self.number_of_problems = number_of_problems

    def get_java(self, puzzle: int) -> str:
        path = "/".join(
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
        return f"[👑]({path})" if os.path.exists(path) else ""

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
                    print("| Puzzle | java |", file=f)
                    print("| --- | --- |", file=f)
                    for problem in range(1, self.number_of_problems + 1):
                        java = self.get_java(problem)
                        line = f"|[{problem}]({url}{problem})|{java}|"
                        print(line, file=f)
                elif line.startswith("<!-- @END:Puzzles"):
                    in_table = False
                    print(line, file=f)
                else:
                    if not in_table:
                        print(line, file=f)
