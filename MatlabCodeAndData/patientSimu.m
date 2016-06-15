close all;
clear all;

PatientSimuHistory=readtable('PatientSimuHistory.dat');
figure;
lastN=5*24*60;

plot([1:lastN]/1440,PatientSimuHistory.G(end-lastN+1:end));
hold on
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
axis([0 5 60 250])
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
title('T1DM Patient Simulation')
legend('Fixed Plan','Reco Plan','Hyperglycemia level','Hypoglycemia level','Target level')

gl=PatientSimuHistory.G;
vrange=20*24*60;
target=112.5;
vArray=[];
for i=1:size(gl,1)-vrange+1
    vArray=[vArray;var(gl(i:i+vrange-1)-target)];
end

figure;
plot([1:size(vArray,1)]/1440,vArray);

figure;
plot([1:size(gl,1)]/1440,gl)
