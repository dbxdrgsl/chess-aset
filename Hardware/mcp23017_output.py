from machine import I2C

class MCP23017Output:
    """
    Handles 16 output pins from MCP23017 configured for LED driving.
    """

    def __init__(self, i2c: I2C, address=0x21):
        self.i2c = i2c
        self.addr = address
        self.stateA = 0x00
        self.stateB = 0x00

        self.i2c.writeto_mem(self.addr, 0x00, b'\x00')  # output
        self.i2c.writeto_mem(self.addr, 0x01, b'\x00')  # output

    def write_pin(self, pin, value):
        if pin < 8:
            if value == 1: self.stateA |= (1 << pin)
            else:          self.stateA &= ~(1 << pin)
            self.i2c.writeto_mem(self.addr, 0x12, bytes([self.stateA]))
        else:
            p = pin - 8
            if value == 1: self.stateB |= (1 << p)
            else:          self.stateB &= ~(1 << p)
            self.i2c.writeto_mem(self.addr, 0x13, bytes([self.stateB]))
