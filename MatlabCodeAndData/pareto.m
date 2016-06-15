close all;
clear all;

Good=readtable('pareto/Good.dat');
Bad=readtable('pareto/Bad.dat');
figure;
subplot(2,1,1)
lastN=5*24*60;

plot([1:lastN]/1440,Good.G(end-lastN+1:end));
hold on
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
axis([0 5 60 250])
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
title('Glucose Variation Simulation with Pareto Dominant Selector and Good Demonstrator')
legend('Glucose Level','Hyperglycemia level','Hypoglycemia level','Target level')

subplot(2,1,2)

plot([1:lastN]/1440,Bad.G(end-lastN+1:end));
hold on
plot([1:lastN]/1440,180*ones(1,lastN),'r--')
plot([1:lastN]/1440,70*ones(1,lastN),'r--')
plot([1:lastN]/1440,112.5*ones(1,lastN),'k--')
axis([0 5 60 250])
grid on;
xlabel('Day');
ylabel('Glucose Level  (mg/dl)')
title('Glucose Variation Simulation with Pareto Dominant Selector and Bad Demonstrator')
legend('Glucose Level','Hyperglycemia level','Hypoglycemia level','Target level')