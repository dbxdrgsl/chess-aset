from magnetic_sensor import MagneticSensor
from led import Led

class BoardModule:
    """
    One of the 4 quadrants.
    Contains:
      - MCP23017 input expander
      - MCP23017 output expander
      - 16 sensors
      - 16 LEDs
    """

    def __init__(self, input_expander, output_expander, row_offset, col_offset):
        self.input = input_expander
        self.output = output_expander
        self.row_offset = row_offset
        self.col_offset = col_offset

        self.sensors = []
        self.leds = []

        for i in range(16):
            self.sensors.append(MagneticSensor(self.input, i))
            self.leds.append(Led(self.output, i))

    def read_state(self):
        """
        Returns a 4×4 boolean matrix: True = piece detected
        """
        matrix = []
        for r in range(4):
            row = []
            for c in range(4):
                pin = r * 4 + c
                val = self.sensors[pin].read()
                row.append(val)
            matrix.append(row)
        return matrix

    def led_on(self, r, c):
        pin = r * 4 + c
        self.leds[pin].on()

    def led_off(self, r, c):
        pin = r * 4 + c
        self.leds[pin].off()
