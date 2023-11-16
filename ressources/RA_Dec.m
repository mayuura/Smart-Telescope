%Calculate Right Ascension(RA)
JD_J2000= 2451545.0; %JD for the J2000 epoch

%JD = 367Y - INT(7 * (Y + INT((M + 9) / 12)) / 4) + INT(275 * M / 9) + D + 1721013.5 + UT/24
% %Y is the year.
% M is the month.
% D is the day.
% UT is the time in hours.

JD=2459320.5;%JD of the current day
D= JD-JD_J2000; %the number of days since the J2000 epoch (a reference point in time)
lambda=5.9939724; %longitude in degrees
T=23; %the number of centuries since the J2000 epoch
T0=280.46061837; %constant offset : LST at Greenwich
roll=0; %roll angle in degrees (from the sensors)    
LST=100.46 + 0.985647 * D + lambda + 15 * (T - T0);
alpha=roll*pi/180;    

RA=LST-alpha;

pitch=0;%pitch angle we get from the sensor
delta_0=pitch*pi/180;
phi=47.2520176; %latitude
yaw=0; %yaw angle from the sensor
theta=yaw*pi/180;
Dec = asin(sin(delta_0) * sin(phi) + cos(delta_0) * cos(phi) * cos(theta));