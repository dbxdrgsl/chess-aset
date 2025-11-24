# mcp23017_input.py
from machine import I2C

class MCP23017Input:
    """
    Handles 16 input pins from the MCP23017 in INPUT mode.
    """

    def __init__(self, i2c: I2C, address=0x20):
        self.i2c = i2c
        self.addr = address

        # IODIRA = 0xFF => all input
        self.i2c.writeto_mem(self.addr, 0x00, b'\xFF')
        self.i2c.writeto_mem(self.addr, 0x01, b'\xFF')

    def read_pin(self, pin):
        bank = 0x12 if pin < 8 else 0x13
        mask = 1 << (pin % 8)

        val = self.i2c.readfrom_mem(self.addr, bank, 1)[0]
        return 1 if (val & mask) else 0
