from aop import log_call

class ChessBoard:
    """
    Aggregates 4 BoardModules to form an 8×8 magnetic sensor grid.
    """

    def __init__(self, m00, m01, m10, m11):
        self.modules = {
            "00": m00,  # top-left
            "01": m01,  # top-right
            "10": m10,  # bottom-left
            "11": m11   # bottom-right
        }

        self.state = [[0]*8 for _ in range(8)]  # sensor stable state

    @log_call
    def scan(self):
        """
        Reads all 4 modules and fills the 8×8 board matrix with sensor results.
        """
        new_state = [[0]*8 for _ in range(8)]

        # Top-left
        tl = self.modules["00"].read_state()
        # Top-right
        tr = self.modules["01"].read_state()
        # Bottom-left
        bl = self.modules["10"].read_state()
        # Bottom-right
        br = self.modules["11"].read_state()

        # assemble 8x8
        for r in range(4):
            for c in range(4):
                new_state[r][c] = tl[r][c]
                new_state[r][c+4] = tr[r][c]
                new_state[r+4][c] = bl[r][c]
                new_state[r+4][c+4] = br[r][c]

        self.state = new_state

        return new_state

    def diff(self, old, new):
        """
        Identify squares that changed.
        Returns list of (row, col, new_value)
        """
        changes = []
        for r in range(8):
            for c in range(8):
                if old[r][c] != new[r][c]:
                    changes.append((r, c, new[r][c]))
        return changes
