class MagneticSensor:
    """
    Represents a single digital magnetic sensor connected to MCP23017 input expander.
    """

    def __init__(self, expander, pin):
        self.expander = expander
        self.pin = pin

    def read(self):
        """Returns True if magnetic field detected."""
        return not self.expander.read_pin(self.pin)  # low=magnet
