class Led:
    """
    Represents a single LED controlled through MCP23017 output expander.
    """

    def __init__(self, expander, pin):
        self.expander = expander
        self.pin = pin

    def on(self):
        self.expander.write_pin(self.pin, 1)

    def off(self):
        self.expander.write_pin(self.pin, 0)
