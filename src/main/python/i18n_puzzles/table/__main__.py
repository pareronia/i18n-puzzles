import sys

from .. import NUMBER_OF_PUZZLES
from .table import Table

Table(NUMBER_OF_PUZZLES).generate(sys.argv[1])
