clc;
clear all;
close all;

GOb=csvread('GOb.dat',1,0);
GTrain=csvread('GTrain.dat',1,0);
LAInsulinTrain=csvread('LAInsulinTrain.dat',1,0);
FAInsulinTrain=csvread('FAInsulinTrain.dat',1,0);
CarbTrain=csvread('CarbTrain.dat',1,0);

com1_Fm1=csvread('/home/hao/Data/Temp/Preds/com1_Fm1.dat');

a=[GOb,GTrain,LAInsulinTrain,FAInsulinTrain,CarbTrain];
T=array2table(a,'VariableNames',{'Ob','G','LA','FA','Carb'});

gprMdl = fitrgp(T,'Ob','KernelFunction','ardsquaredexponential',...
      'FitMethod','sr','PredictMethod','fic','Standardize',1)
  
pred=resubPredict(gprMdl);
figure;
plot(GOb,'b');
hold on;
plot(pred,'ro');
grid on;
legend('Actual','Pred')

%%

PatientSimuHistory=readtable('PatientSimuHistory.dat');
figure;
l=size(PatientSimuHistory,1);
hold on;
plot([1:l]/1440,PatientSimuHistory.G);
plot([1:l]/1440,180*ones(1,l),'b')
plot([1:l]/1440,70*ones(1,l),'r')
plot([1:l]/1440,112.5*ones(1,l),'g')
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
title('T1DM Patient Simulation')
legend('Fixed Plan','Reco Plan','Hyperglycemia level','Hypoglycemia level','Target level')

%% last few

PatientSimuHistoryMaxConf=readtable('PatientSimuHistoryMaxConf.dat');
PatientSimuHistoryClosest=readtable('PatientSimuHistoryClosest.dat');
PatientSimuHistoryProportionate=readtable('PatientSimuHistoryProportionate.dat');
figure;
l=size(PatientSimuHistoryMaxConf,1);
lastN=5*24*60;
subplot(3,1,1)
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

subplot(3,1,2)
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

subplot(3,1,3)
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
%legend('Max Conf Selector','Closest Selector','Proportionate Selector','Hyperglycemia level','Hypoglycemia level','Target level')

%%



