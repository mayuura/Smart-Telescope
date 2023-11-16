% -------------------------------------------------------------------
% Attitude estimation using an accelerometer norm-based adaptive algorithm
% -------------------------------------------------------------------

function [q4, eulercom4, bahat, bghat] = Compute_Attitude_Norm(yg, ya, ym, tt, Rg, Ra, Rm)

D2R = pi/180;                               % Conversion from deg to rad

% Number of data: N
N = max(size(ya));

% Question 8
g = 9.8;
g_tilde = [0; 0; g];

dip = 50*D2R;
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

% inital orientation estimation using the TRIAD method
yabar = ya(:, 1) / norm(ya(:, 1));
ymbar = ym(:, 1) / norm(ym(:, 1));

foo1 = cross(yabar, ymbar) / norm( cross(yabar, ymbar) );
C = [-cross(yabar, foo1), foo1, yabar ] ;
q4(:, 1) = dcm2quaternion(C);

% Kalman filter state
x = zeros(9, 1);

% Question 10
P = zeros(9, 9); 
P(1 : 3, 1 : 3) = 0.01*eye(3);
P(4 : 6, 4 : 6) = 0.000001*eye(3);
P(7 : 9, 7 : 9) = 0.000001*eye(3);

% Question 11
Ohm = [0                                  -yg(1, 1)+bghat(1)              -yg(2, 1)+bghat(2)               -yg(3, 1)+bghat(3);
             yg(1, 1)-bghat(1)         0                                         yg(3, 1)-bghat(3)                  -yg(2, 1)+bghat(2);
             yg(2, 1)-bghat(2)         -yg(3, 1)+bghat(3)              0                                          yg(1, 1)-bghat(1);
             yg(3, 1)-bghat(3)         yg(2, 1)-bghat(2)                -yg(1, 1)+bghat(1)               0];

% parameter for adaptive algorithm
threshold = 0.2;
s = 10;

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
    OhmPast = Ohm;
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
    Ha = [2*vec2product(Cq*g_tilde) zeros(3, 3) eye(3)];
    za = ya(:, i) - Cq*g_tilde;
    
    % Norm-based adaptive algorithm
    if (abs(norm(ya(:, i) - bahat) - g) > threshold) % it means that there is external acceleration
        Qstar = s*eye(3); % doing this makes us lose more information than necessary
%         Qstar = zeros(3, 3); % doing this means no adaptive algorithm (standard filter)
    else
        Qstar = zeros(3, 3);
    end
    
    % Question 20
    Ka = P*Ha'*inv(Ha*P*Ha' + Ra + Qstar); 
    
    % Question 21
    x = x + Ka*(za - Ha*x);
    
    % Question 22
    Pa = (eye(9) - Ka*Ha)*P*(eye(9) - Ka*Ha)' + Ka*(Ra + Qstar)*Ka'; 
    
    % Question 23
    qe = x(1 : 3);
    q4(:, i) = quaternionmul(q4(:, i), [1; qe]);
    % Normalize
    q4(:, i) = q4(:, i) / norm(q4(:, i));
    
    Cq = quaternion2dcm(q4(:, i));
    x(1 : 3) = zeros(3, 1);
    
    % Question 24
    Hm = [2*vec2product(Cq*m_tilde) zeros(3, 3) zeros(3, 3)];
    zm = ym(:, i) - Cq*m_tilde;
    
    % Question 25
    Pm = [Pa(1 : 3, 1 : 3)          zeros(3, 6);        zeros(6, 3)             zeros(6,6)];
    r3 = Cq*[0; 0; 1];
    Km = [r3*r3'  zeros(3,6); zeros(6,3) zeros(6,6)]*Pm*Hm'*inv(Hm*Pm*Hm' + Rm);

    x = x + Km*(zm - Hm*x);
    P = (eye(9) - Km*Hm)*Pa*(eye(9) - Km*Hm)' + Km*Rm*Km';
    
    bghat = bghat + x(4 : 6);
    x(4 : 6) = zeros(3, 1);
    
    bahat = bahat + x(7 : 9);
    x(7 : 9) = zeros(3, 1);
    
    % Question 26
    qe = x(1 : 3);
    q4(:, i) = quaternionmul(q4(:, i), [1; qe]);
    % Normalize
    q4(:, i) = q4(:, i) / norm(q4(:, i));
    
    % Question 27
    eulercom4(:, i) = quaternion2euler(q4(:, i))*180/pi;
end