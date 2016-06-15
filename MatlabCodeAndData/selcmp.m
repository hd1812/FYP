
%close all
%clear all
PatientSimuHistoryMaxConf=readtable('selcmp2/PatientSimuHistoryMaxConf.dat');
PatientSimuHistoryClosest=readtable('selcmp2/PatientSimuHistoryClosest.dat');
PatientSimuHistoryProportionate=readtable('selcmp2/PatientSimuHistoryProportionate.dat');
pareto=readtable('selcmp2/pareto.dat');

figure;
l=size(PatientSimuHistoryMaxConf,1);
lastN=5*24*60;
subplot(4,1,1)
plot([1:lastN]/1440,PatientSimuHistoryMaxConf.G(end-lastN+1:end));
hold on
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
axis([0 5 60 250])
title('Glucose Level Simulation with Maximum Confidence Selector')

subplot(4,1,2)
plot([1:lastN]/1440,PatientSimuHistoryClosest.G(end-lastN+1:end));
hold on
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
axis([0 5 60 250])
title('Glucose Level Simulation with Best Quality Selector')

subplot(4,1,3)
plot([1:lastN]/1440,PatientSimuHistoryProportionate.G(end-lastN+1:end));
hold on
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
axis([0 5 60 250])
title('Glucose Level Simulation with Roulette Wheel Selector')

subplot(4,1,4)
plot([1:lastN]/1440,pareto.G(end-lastN+1:end));
hold on
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
axis([0 5 60 250])
title('Glucose Level Simulation with Pareto Dominant Selector')

