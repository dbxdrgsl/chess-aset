# position.py
class Position:
    def __init__(self, row, col):
        self.row = row
        self.col = col

    def __repr__(self):
        return f"({self.row}, {self.col})"
