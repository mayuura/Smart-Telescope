%% Mobile Robotics _ Perception _ Lab _ 17 Dec. 2020
% Student: TRAN Gia Quoc Bao

%% Default commands
close all; clear all; clc;

%--------------------------------------------------------------------------------------------------
% Main program to read IMU data, run the fuction that estimates quaternion/Euler angles, and biases 
% and plots IMU data, external acceleration and estimated Euler angles
%--------------------------------------------------------------------------------------------------
R2D = 180/pi;                           % Conversion from rad to deg

% Question 3
load('IMU_Data.mat');
% The corresponding size is N = 333 so we shorten the ym to match that size
ym = ym(:, 1 : N);

% Question 4
% The first data are the outputs by each triaxial sensor
figure('Name', 'Output'); 
subplot(311); plot(tt, ya); legend('x', 'y', 'z'); grid on; xlabel('Time (s)'); ylabel({'Accelerometer', 'output (m/s^2)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
title('Output by each triaxial sensor');
subplot(312); plot(tt, yg); legend('x', 'y', 'z'); grid on; xlabel('Time (s)'); ylabel({'Gyroscope', 'output (rad/s)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
subplot(313); plot(tt, ym); legend('x', 'y', 'z'); grid on; xlabel('Time (s)'); ylabel({'Magnetic sensor', 'output (arbitrary unit)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);

% The second data are the noises in each triaxial sensor
figure('Name', 'Noise'); 
subplot(311); plot(tt, wa); legend('x', 'y', 'z'); grid on; xlabel('Time (s)'); ylabel({'Accelerometer', 'noise (m/s^2)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
title('Noise on each triaxial sensor');
subplot(312); plot(tt, wg); legend('x', 'y', 'z'); grid on; xlabel('Time (s)'); ylabel({'Gyroscope', 'noise (rad/s)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
subplot(313); plot(tt, wm); legend('x', 'y', 'z'); grid on; xlabel('Time (s)'); ylabel({'Magnetic sensor', 'noise (arbitrary unit)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);

% The third data are the external acceleration
figure('Name', 'External acceleration'); 
subplot(311); plot(tt, acc(1, :)); grid on; xlabel('Time (s)'); ylabel({'Ext. acceleration', 'on x-axis (m/s^2)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
title('External acceleration');
subplot(312); plot(tt, acc(2, :)); grid on; xlabel('Time (s)'); ylabel({'Ext. acceleration', 'on y-axis (m/s^2)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
subplot(313); plot(tt, acc(3, :)); grid on; xlabel('Time (s)'); ylabel({'Ext. acceleration', 'on z-axis (m/s^2)'}); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);

% The fourth data are the Euler angles
figure('Name', 'Euler angles'); 
subplot(311); plot(tt, euler(1, :)); grid on; xlabel('Time (s)'); ylabel('Pitch (deg)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
title('True Euler angles');
subplot(312); plot(tt, euler(2, :)); grid on; xlabel('Time (s)'); ylabel('Roll (deg)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
subplot(313); plot(tt, euler(3, :)); grid on; xlabel('Time (s)'); ylabel('Yaw (deg)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);

% Question 5
% Noise error covariance matrices
Ra = 0.0056*eye(3); Rg = 0.003*eye(3); Rm = 0.001*eye(3); 

%% Question 6
% The following function computes the quaternions, Euler angles, and the estimated
% biases using the measurements and the noise error covariance matrices,
% using the proposed method.
% We added 2 more array variables to plot the estimated biases w.r.t time
[q4, eulercom4, bahat, bghat, bghatEst, bahatEst] = Compute_Attitude(yg, ya, ym, tt, Rg, Ra, Rm);

% This function below is to experiment with an accelerometer norm-based adaptive algorithm
% [q4, eulercom4, bahat, bghat] = Compute_Attitude_Norm(yg, ya, ym, tt, Rg, Ra, Rm); % experiment with norm-based

% Question 28
% Results of orientation estimation
figure('Name', 'Euler angles'); 
subplot(311); plot(tt, euler(1, :), 'b', tt, eulercom4(1, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('Pitch (deg)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
title('Euler angle estimation using quaternion-based indirect Kalman filter with adaptive estimation of external acceleration');
subplot(312); plot(tt, euler(2, :), 'b', tt, eulercom4(2, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('Roll (deg)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
subplot(313); plot(tt, euler(3, :), 'b', tt, eulercom4(3, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('Yaw (deg)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);

% Plot the estimated biases to see
figure('Name', 'Accelerometer bias estimation'); 
subplot(311); plot(tt, ba(1)*ones(1, N), '--b', tt, bahatEst(1, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('x-axis (m/s^2)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
title('Accelerometer bias estimation');
subplot(312); plot(tt, ba(2)*ones(1, N), '--b', tt, bahatEst(2, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('y-axis (m/s^2)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
subplot(313); plot(tt, ba(3)*ones(1, N), '--b', tt, bahatEst(3, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('z-axis (m/s^2)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);

figure('Name', 'Gyroscope bias estimation'); 
subplot(311); plot(tt, ba(1)*ones(1, N), '--b', tt, bghatEst(1, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('x-axis (rad/s)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
title('Gyroscope bias estimation');
subplot(312); plot(tt, ba(2)*ones(1, N), '--b', tt, bghatEst(2, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('y-axis (rad/s)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);
subplot(313); plot(tt, ba(3)*ones(1, N), '--b', tt, bghatEst(3, :), 'r'); legend('True', 'Estimated'); grid on; xlabel('Time (s)'); ylabel('z-axis (rad/s)'); set(findall(gca, 'Type', 'Line'), 'LineWidth', 1.5); set(gca, 'FontSize', 14);