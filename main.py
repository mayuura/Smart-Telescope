from astroquery.simbad import Simbad
from astropy.coordinates import SkyCoord
import astropy.units as u
import sys
import math
import json
import pandas as pd

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
def Dec_to_degrees(sexagesimal_str):
    # Split the input string into degrees, minutes, and seconds parts
    parts = sexagesimal_str.split()
    if len(parts) != 3:
        raise ValueError("Invalid sexagesimal notation")

    # Extract degrees, minutes, and seconds as integers or floats
    degrees = abs(int(parts[0]))
    minutes = int(parts[1])
    seconds = float(parts[2])

    # Calculate the decimal degrees
    decimal_degrees = - (degrees + minutes/60 + seconds/3600)

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
def query_simbad(ra, dec,r):
    # Initialize Simbad
    custom_simbad = Simbad()

    # Specify the coordinates as a SkyCoord object
    coordinates = SkyCoord(ra, dec, unit=(u.deg, u.deg), frame='icrs')

    # Set the search radius to 2 arcminutes
    radius = r * u.arcminute

    # Query SIMBAD with the specified coordinates and radius
    result_table = custom_simbad.query_region(coordinates, radius=radius)  # Example query
    #result_table = custom_simbad.query_region(coordinates, radius=radius, fields=['DISTANCE'])


    if result_table is not None:
        # Convert the result table to a Pandas DataFrame
        result_df = result_table.to_pandas()
        result_df = result_df[['MAIN_ID', 'RA', 'DEC']]
        # Create an empty list to store angular separations
        angular_separations = []

        # Loop through each row in the DataFrame
        for ind in range(len(result_df)):
            # Calculate angular separation and add it to the list
            #print(1)

            #convert table coordinates to degrees
            ra2=Ra_to_degrees(result_df["RA"][ind])
            dec2=Dec_to_degrees(result_df["DEC"][ind])
            #calculate angular separation
            sep = angular_separation(ra, dec, ra2, dec2)
            angular_separations.append(sep)

        # Add the list of angular separations as a new column in the DataFrame
        result_df['AngularSeparation_arcsec'] = angular_separations

        result_data = result_df.to_json(orient='records', lines=True)

        #column_names = result_df.columns.tolist()

        # Print the list of column names to the console
        #print("Column Names:")
        #for column_name in column_names:
           #print(column_name)

        # Print the results to the console
        #print(result_df)
        print(result_data)
        #print(angular_separations)
    else:
        print("No results found.")
    return result_data
# Perform a test query with ICRS coordinates and a 2 arcminute radius
#query_simbad(3.9084429453, -1.442668530641793)
# Check if the correct number of command-line arguments are provided
if len(sys.argv) != 4:
    print("Usage: python main.py <ra> <dec> <radius>")
else:
    # Get the command-line arguments
    ra = float(sys.argv[1])
    dec = float(sys.argv[2])
    radius = int(sys.argv[3])

    # Perform the SIMBAD query
    query_simbad(ra, dec, radius)



