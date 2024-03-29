# -*- coding: utf-8 -*-
"""
Created on Sat Nov 18 16:50:02 2023

@author: El Mehdi
"""

from skyfield.api import Topos, load
from astropy.coordinates import EarthLocation, AltAz, get_sun, ICRS, Angle, Longitude, Latitude, get_moon
from astropy.time import Time
import astropy.units as u

# Your telescope's location
latitude = 47.2496387
longitude = 5.9925203
#values for 15/11
calculated_ra=74.8923781665778
calculated_dec=42.7503613

# Use Skyfield to calculate RA and Dec
ts = load.timescale()
t = ts.now()
telescope_location = Topos(latitude, longitude)
observer = telescope_location.at(t)
ra, dec, _ = observer.radec()

# Compare with your calculated values
print("Skyfield - Calculated RA:", ra, "Calculated Dec:", dec)
print("Your Calculations - RA:", calculated_ra, "Dec:", calculated_dec)




def calculate_celestial_coordinates(latitude, longitude, roll, pitch, yaw):
   # Define observer's location
    observer_location = EarthLocation(lat=Latitude(latitude * u.deg), lon=Longitude(longitude * u.deg))

    # Convert GPS time to Astropy Time object
    observation_time = Time.now()

    # Calculate Altitude and Azimuth from IMU data
   
    alt_az = AltAz(obstime=observation_time, location=observer_location, az=yaw * u.deg, alt=pitch * u.deg)
    alt_az_transformed = alt_az.transform_to(ICRS())

    # Get RA and Dec
    ra, dec = alt_az_transformed.ra.deg, alt_az_transformed.dec.deg

    return ra,dec

roll = 0  # Replace with your IMU roll angle in degrees
pitch = 0  # Replace with your IMU pitch angle in degrees
yaw = 0 # Replace with your IMU yaw angle in degrees


# Calculate celestial coordinates
ra, dec = calculate_celestial_coordinates(latitude, longitude, roll, pitch, yaw)

# Display the results
print(f'Input Latitude: {latitude}, Longitude: {longitude}, Roll: {roll}, Pitch: {pitch}, Yaw: {yaw}')

print(f'Calculated RA: {ra} degrees, Dec: {dec} degrees')