import math


def Dec_to_degrees(sexagesimal_str):
    # Split the input string into degrees, minutes, and seconds parts
    parts = sexagesimal_str.split()
    if len(parts) != 3:
        raise ValueError("Invalid sexagesimal notation")

    degrees = int(parts[0])
    minutes = int(parts[1])
    seconds = int(parts[2])

    decimal_degrees = - (degrees + minutes / 60 + seconds / 3600)

    return decimal_degrees

def Ra_to_degrees(sexagesimal_str):
    # Split the input string into degrees, minutes, and seconds parts
    parts = sexagesimal_str.split()
    if len(parts) != 3:
        raise ValueError("Invalid sexagesimal notation")

    # Extract degrees, minutes, and seconds as integers or floats
    hours = int(parts[0])
    minutes = int(parts[1])
    seconds = float(parts[2])

    # Calculate the decimal degrees
    decimal_degrees = (hours + minutes/60 + seconds/3600)*(360/24)

    return decimal_degrees
def angular_separation(ra1,dec1,ra2,dec2):
    #make sure the input is in degrees not sexagesimal
    # Convert degrees to radians
    ra1 = math.radians(ra1)
    dec1 = math.radians(dec1)
    ra2 = math.radians(ra2)
    dec2 = math.radians(dec2)

    # Haversine formula
    delta_ra = ra2 - ra1
    delta_dec = dec2 - dec1
    a = math.sin(delta_dec / 2) ** 2 + math.cos(dec1) * math.cos(dec2) * math.sin(delta_ra / 2) ** 2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

    # Angular separation in radians
    theta = c

    # Convert radians to degrees
    theta_deg = math.degrees(theta)*3600

    return theta_deg

# Example usage:
Ra_str = "20 33 44.4"
Dec_str="-01 25 34"
degrees = Dec_to_degrees(Dec_str)
ra=Ra_to_degrees(Ra_str)
print(f"{Dec_str} is approximately equal to {degrees} degrees")
print(f"{Ra_str} is approximately equal to {ra} degrees")
sep = angular_separation(3908.4429453, -1.442668530641793, ra, degrees)
print(sep)
