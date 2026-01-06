from position import Position

class Piece:
    def __init__(self, piece_type, color, position: Position):
        self.type = piece_type
        self.color = color
        self.position = position

    def move_to(self, new_position: Position):
        self.position = new_position
