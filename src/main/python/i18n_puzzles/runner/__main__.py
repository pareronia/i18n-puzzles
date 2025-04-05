import sys

from .. import NUMBER_OF_PUZZLES
from .runner import Runner

Runner(NUMBER_OF_PUZZLES).main(sys.argv[1:])
