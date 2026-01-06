# main.py
import time
import urequests
from machine import I2C, Pin
from mcp23017_input import MCP23017Input
from mcp23017_output import MCP23017Output
from board_module import BoardModule
from chessboard import ChessBoard

BACKEND_URL = "..."

def send_move(changes):
    payload = {"changes": changes}
    try:
        r = urequests.post(BACKEND_URL, json=payload)
        print("Server response:", r.text)
    except:
        print("ERR: Cannot send move to backend")

def main():
    # I2C bus
    i2c = I2C(0, scl=Pin(17), sda=Pin(16), freq=400000)

    # four modules with different I2C addresses
    m00_in = MCP23017Input(i2c, 0x20)
    m00_out = MCP23017Output(i2c, 0x21)

    m01_in = MCP23017Input(i2c, 0x22)
    m01_out = MCP23017Output(i2c, 0x23)

    m10_in = MCP23017Input(i2c, 0x24)
    m10_out = MCP23017Output(i2c, 0x25)

    m11_in = MCP23017Input(i2c, 0x26)
    m11_out = MCP23017Output(i2c, 0x27)

    # Create 4 modules
    mod00 = BoardModule(m00_in, m00_out, 0, 0)
    mod01 = BoardModule(m01_in, m01_out, 0, 4)
    mod10 = BoardModule(m10_in, m10_out, 4, 0)
    mod11 = BoardModule(m11_in, m11_out, 4, 4)

    # Create global board
    board = ChessBoard(mod00, mod01, mod10, mod11)

    prev_state = board.scan()

    while True:
        new_state = board.scan()

        changes = board.diff(prev_state, new_state)
        if changes:
            print("Detected changes:", changes)
            send_move(changes)

        prev_state = new_state

        time.sleep(0.2)


if __name__ == "__main__":
    main()
