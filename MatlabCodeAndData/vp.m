close all;
clear all;

t1=readtable('vpsample/t1dm.dat');
t2=readtable('vpsample/t2dm.dat');
normal=readtable('vpsample/normal.dat');
PatientSimuHistory=readtable('vpsample/PatientSimuHistory.dat');

lastN=4*24*60;

figure;
hold on;
plot([1:lastN]*24/1440,PatientSimuHistory.G(end-lastN+1:end));
plot([1:lastN]*24/1440,180*ones(1,lastN),'r--')
plot([1:lastN]*24/1440,70*ones(1,lastN),'r--')
plot([1:lastN]*24/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Time in a Day');
ylabel('Glucose Level  (mg/dl)')
title('T1DM Simulation')
legend('Glucose Level Variation','Hyperglycemia level','Hypoglycemia level','Target level')
axis([0 24 60 190])

figure;
subplot(3,1,1)
hold on;
plot([1:lastN]/1440,t1.G(end-lastN+1:end));
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
axis([0 4 50 270])
title('T1DM Simulation')
legend('Glucose Level Variation','Hyperglycemia level','Hypoglycemia level','Target level')

subplot(3,1,2)
hold on;
plot([1:lastN]/1440,t2.G(end-lastN+1:end));
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
axis([0 4 50 270])
title('T2DM Simulation')
legend('Glucose Level Variation','Hyperglycemia level','Hypoglycemia level','Target level')

subplot(3,1,3)
hold on;
plot([1:lastN]/1440,normal.G(end-lastN+1:end));
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
axis([0 4 50 270])
title('Nondiabetic Simulation')
legend('Glucose Level Variation','Hyperglycemia level','Hypoglycemia level','Target level')

