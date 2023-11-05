% -------------------------------------------------------------------
% Attitude estimation using a quaternion-based indirect Kalman filter
% -------------------------------------------------------------------
function [q4, eulercom4, bahat, bghat, bghatEst, bahatEst] = Compute_Attitude(yg, ya, ym, tt, Rg, Ra, Rm)

D2R = pi/180;                               % Conversion from deg to rad

% Number of data: N
N = max(size(ya));

% Question 8
g = 9.8;
g_tilde = [0; 0; g];

dip = 50*D2R; % the dip angle
m_tilde = [cos(dip); 0; -sin(dip)];

% Question 9
Qba = 0.000001*eye(3);
Qbg = 0.000001*eye(3);
Q = zeros(9, 9); 
Q(1 : 3, 1 : 3) = 0.25*Rg;
Q(4 : 6, 4 : 6) = Qba;
Q(7 : 9, 7 : 9) = Qbg;

% --------------------------------------------------------
% Kalman Filter
% --------------------------------------------------------

% q4: quaternion
q4 = zeros(4, N);

% eulercom4: euler angles
eulercom4 = zeros(3, N);

% Estimated bias for gyroscope (bghat) and accelerometer (bahat) 
bghat = zeros(3, 1);
bahat = zeros(3, 1);
bghatEst = zeros(3, N);
bahatEst = zeros(3, N);

% inital orientation estimation using the TRIAD method
yabar = ya(:, 1) / norm(ya(:, 1));
ymbar = ym(:, 1) / norm(ym(:, 1));

foo1 = cross(yabar, ymbar) / norm( cross(yabar, ymbar) );
C = [-cross(yabar, foo1), foo1, yabar ] ;
q4(:, 1) = dcm2quaternion(C);

% Kalman filter state
x = zeros(9, 1); % initalize the filter state

% Question 10
% Initialize the matrix P
P = zeros(9, 9); 
P(1 : 3, 1 : 3) = 0.01*eye(3);
P(4 : 6, 4 : 6) = 0.000001*eye(3);
P(7 : 9, 7 : 9) = 0.000001*eye(3);

% Question 11
% We need to subtract the estimated bias from the measurements
Ohm = [0                                  -yg(1, 1)+bghat(1)              -yg(2, 1)+bghat(2)               -yg(3, 1)+bghat(3);
             yg(1, 1)-bghat(1)         0                                         yg(3, 1)-bghat(3)                  -yg(2, 1)+bghat(2);
             yg(2, 1)-bghat(2)         -yg(3, 1)+bghat(3)              0                                          yg(1, 1)-bghat(1);
             yg(3, 1)-bghat(3)         yg(2, 1)-bghat(2)                -yg(1, 1)+bghat(1)               0];

% variable used in the adaptive algorithm      
r2count = 100;

% parameter for adaptive algorithm
M1 = 3;
M2 = 2; % we changed this to be coherent with the paper
gamma = 0.1;

R = zeros(3, 3*N);

% Kalman filter loop
for i = 2 : N
    % Question 12
    T = tt(i) - tt(i - 1);
    
    % Question 13
    A = [-vec2product(yg(:, i - 1) - bghat)      -0.5*eye(3)         zeros(3, 3);               zeros(6, 9)];                      
    
    % Question 14
    phiK = eye(9) + A*T + 0.5*A^2*T^2;
    
    % Question 15
    Qd = Q*T + 0.5*A*Q + 0.5*Q*A';
    
    x = phiK*x;
    
    % Question 16
    P = phiK*P*phiK' + Qd;
    
    % Question 17
    OhmPast = Ohm; % store the last measurement
    % Get the new measurements
    Ohm = [0                        -yg(1, i)+bghat(1)      -yg(2, i)+bghat(2)      -yg(3, i)+bghat(3);
           yg(1, i)-bghat(1)      0                                 yg(3, i)-bghat(3)        -yg(2, i)+bghat(2);
           yg(2, i)-bghat(2)      -yg(3, i)+bghat(3)      0                                 yg(1, i)-bghat(1);
           yg(3, i)-bghat(3)      yg(2, i)-bghat(2)        -yg(1, i)+bghat(1)       0];
    
    % Question 18
    q4(:, i) = (eye(4) + 0.75*Ohm*T - 0.25*OhmPast*T - (1/6)*norm(yg(:, i - 1) - bghat)^2*eye(4)*T^2-(1/24)*Ohm*OhmPast*T^2 - (1/48)*norm(yg(:, i - 1) - bghat)^2*Ohm*T^3)*q4(:, i - 1);
    
    Cq = quaternion2dcm(q4(:, i));
     
    % ----------------------------------------------------
    % two-step measurement update
    % ----------------------------------------------------
    
    % Question 19
    % Accelerometer measurement update
    Ha = [2*vec2product(Cq*g_tilde) zeros(3, 3) eye(3)];
    za = ya(:, i) - Cq*g_tilde;
    
    % adaptive algorithm
    % Appoximating Uk
    fooR1 = (za - Ha*x)*(za - Ha*x)';
    R(:, 3*(i - 1) + 1 : 3*i) = fooR1;
    uk = fooR1;
    for j = i - 1 : min([i - (M1 - 1), 1])
        uk = uk + R(:, 3*(j - 1) + 1 : 3*j);
    end
    uk = uk / M1;
    
    fooR2 = Ha*P*Ha' + Ra;
  
    [u, s, v] = svd(uk);
    u1 = u(:, 1);
    u2 = u(:, 2);
    u3 = u(:, 3);
    
    lambda = [s(1) , s(2) , s(3)];
    mu =  [u1' * fooR2 * u1, u2' * fooR2 * u2, u3' * fooR2 * u3];
    
    % The switch condition for mode 1 and 2
    if (max(lambda - mu) > gamma)
        r2count = 0;
        Qstar = max(lambda(1) - mu(1), 0)*u1*u1' + max(lambda(2) - mu(2), 0)*u2*u2' + max(lambda(3) - mu(3), 0)*u3*u3';
    else
        r2count = r2count + 1;
        if (r2count < M2)
            Qstar = max(lambda(1) - mu(1), 0)*u1*u1' + max(lambda(2) - mu(2), 0)*u2*u2' + max(lambda(3) - mu(3), 0)*u3*u3';
        else
            Qstar = zeros(3, 3);
        end
    end
    
    % Question 20
    Ka = P*Ha'*inv(Ha*P*Ha' + Ra + Qstar); 
    
    % Question 21
    x = x + Ka*(za - Ha*x);
    
    % Question 22
    Pa = (eye(9) - Ka*Ha)*P*(eye(9) - Ka*Ha)' + Ka*(Ra + Qstar)*Ka'; 
    
    % Question 23
    % Update Cq using x
    qe = x(1 : 3);
    q4(:, i) = quaternionmul(q4(:, i), [1; qe]);
    % Normalize
    q4(:, i) = q4(:, i) / norm(q4(:, i));
    
    Cq = quaternion2dcm(q4(:, i));
    x(1 : 3) = zeros(3, 1);
    
    % Question 24
    % Magnetic sensor measurement update
    Hm = [2*vec2product(Cq*m_tilde) zeros(3, 3) zeros(3, 3)];
    zm = ym(:, i) - Cq*m_tilde;
    
    % Question 25
    Pm = [Pa(1 : 3, 1 : 3)          zeros(3, 6);        zeros(6, 3)             zeros(6,6)];
    r3 = Cq*[0; 0; 1];
    Km = [r3*r3'  zeros(3,6); zeros(6,3) zeros(6,6)]*Pm*Hm'*inv(Hm*Pm*Hm' + Rm);

    x = x + Km*(zm - Hm*x);
    P = (eye(9) - Km*Hm)*Pa*(eye(9) - Km*Hm)' + Km*Rm*Km';
    
    % Estimate the biases from x
    bghat = bghat + x(4 : 6);
    bghatEst(:, i) = bghat;
    x(4 : 6) = zeros(3, 1);
    
    bahat = bahat + x(7 : 9);
    bahatEst(:, i) = bahat;
    x(7 : 9) = zeros(3, 1);
    
    % Question 26
    % For the last iteration
    qe = x(1 : 3);
    q4(:, i) = quaternionmul(q4(:, i), [1; qe]);
    % Normalize
    q4(:, i) = q4(:, i) / norm(q4(:, i));
    
    % Question 27
    % Find the Euler angles and chnage to degree
    eulercom4(:, i) = quaternion2euler(q4(:, i))/D2R;
end