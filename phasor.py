import math

class Phasor:
    """A class representing phasors (complex numbers in polar form)."""
    
    def __init__(self, magnitude: float, angle: float):
        self.magnitude = magnitude
        self.angle = angle  # In degrees

    def __repr__(self):
        return f"{round(self.magnitude, 3)} ∠ {round(self.angle, 3)}°"

    def to_rect(self):
        """Returns the rectangular form as (real, imaginary)."""
        real = self.magnitude * math.cos(math.radians(self.angle))
        imag = self.magnitude * math.sin(math.radians(self.angle))
        return round(real, 3), round(imag, 3)

    @classmethod
    def from_rect(cls, real: float, imag: float):
        """Creates a Phasor from rectangular (a + bi) form."""
        magnitude = math.sqrt(real**2 + imag**2)
        angle = math.degrees(math.atan2(imag, real))
        return cls(magnitude, angle)

    def p(magnitude: float, angle: float):
        """Creates a phasor in polar form (magnitude, angle)."""
        return Phasor(magnitude, angle)

    @classmethod
    def r(cls, val: float, exp: int = 0):
        """Creates a phasor representing a resistor."""
        return cls(val * (10 ** exp), 0)

    @classmethod
    def c(cls, val: float, freq: float, exp: int):
        """Creates a phasor for a capacitor."""
        return cls((10 ** -exp) / (val * freq), -90)

    @classmethod
    def l(cls, val: float, freq: float, exp: int):
        """Creates a phasor for an inductor."""
        return cls((10 ** exp) * val * freq, 90)

    def __add__(self, other):
        """Phasor addition in rectangular form."""
        real1, imag1 = self.to_rect()
        real2, imag2 = other.to_rect()
        return Phasor.from_rect(real1 + real2, imag1 + imag2)

    def __sub__(self, other):
        """Phasor subtraction in rectangular form."""
        real1, imag1 = self.to_rect()
        real2, imag2 = other.to_rect()
        return Phasor.from_rect(real1 - real2, imag1 - imag2)

    def __mul__(self, other):
        """Phasor multiplication (polar form)."""
        return Phasor(self.magnitude * other.magnitude, self.angle + other.angle)

    def __truediv__(self, other):
        """Phasor division (polar form)."""
        return Phasor(self.magnitude / other.magnitude, self.angle - other.angle)

    def inverse(self):
        """Returns the reciprocal of the phasor."""
        return Phasor(1 / self.magnitude, -self.angle)

    def scale(self, factor: float):
        """Scales the phasor magnitude by a factor."""
        return Phasor(self.magnitude * factor, self.angle)

    def negate(self):
        """Negates the phasor (180-degree phase shift)."""
        return Phasor(-self.magnitude, self.angle)

    @staticmethod
    def parallel(p1, p2):
        """Computes parallel impedance: (p1 * p2) / (p1 + p2)."""
        return (p1 * p2) / (p1 + p2)

    @staticmethod
    def series(p1, p2):
        """Computes series impedance: p1 + p2."""
        return p1 + p2

    @staticmethod
    def vdiv(z1, z2, v):
        """Voltage divider: V * (Z1 / (Z1 + Z2))"""
        return v * (z1 / (z1 + z2))

    @staticmethod
    def idiv(z1, z2, i):
        """Current divider: I * (Z2 / (Z1 + Z2))"""
        return i * (z2 / (z1 + z2))

p = Phasor.p
from_rect = Phasor.from_rect
r = Phasor.r
c = Phasor.c
l = Phasor.l
parallel = Phasor.parallel
series = Phasor.series
vdiv = Phasor.vdiv
idiv = Phasor.idiv
