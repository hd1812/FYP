close all
clear all

T1=readtable('confs/k5.dat');
T2=readtable('confs/k10.dat');
T3=readtable('confs/k20.dat');
T4=readtable('confs/k30.dat');
figure;
subplot(2,2,1)
hold on;
l=size(T1,1);
plot([1:l],T1.HighCarbIm_LAInsulinIm2_FAInsulinIm1_GPFm1);
plot([1:l],T1.NormalCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);
plot([1:l],T1.LowCarbIm_GPFm1);
plot([1:l],T1.FAInsulinIm1_GPFm1);
plot([1:l],T1.LAInsulinIm1_GPFm1);

grid on;
xlabel('Iteration of the Recommender System');
ylabel('Confidence Level')
title('Confidence Level Comparison k=5')
legend('Demonstrated Strategy','Sample High-level Strategy','Diet Plan','Fast-acting Insulin','Long-acting Insulin')

subplot(2,2,2)
hold on;
l=size(T2,1);
plot([1:l],T2.HighCarbIm_LAInsulinIm2_FAInsulinIm1_GPFm1);
plot([1:l],T2.NormalCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);
plot([1:l],T2.LowCarbIm_GPFm1);
plot([1:l],T2.FAInsulinIm1_GPFm1);
plot([1:l],T2.LAInsulinIm1_GPFm1);
grid on;
xlabel('Iteration of the Recommender System');
ylabel('Confidence Level')
title('Confidence Level Comparison k=10')
legend('Demonstrated Strategy','Sample High-level Strategy','Diet Plan','Fast-acting Insulin','Long-acting Insulin')

subplot(2,2,3)
hold on;
l=size(T3,1);
plot([1:l],T3.HighCarbIm_LAInsulinIm2_FAInsulinIm1_GPFm1);
plot([1:l],T3.NormalCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);
plot([1:l],T3.LowCarbIm_GPFm1);
plot([1:l],T3.FAInsulinIm1_GPFm1);
plot([1:l],T3.LAInsulinIm1_GPFm1);
grid on;
xlabel('Iteration of the Recommender System');
ylabel('Confidence Level')
title('Confidence Level Comparison k=20')
legend('Demonstrated Strategy','Sample High-level Strategy','Diet Plan','Fast-acting Insulin','Long-acting Insulin')

subplot(2,2,4)
hold on;
l=size(T4,1);
plot([1:l],T4.HighCarbIm_LAInsulinIm2_FAInsulinIm1_GPFm1);
plot([1:l],T4.NormalCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);
plot([1:l],T4.LowCarbIm_GPFm1);
plot([1:l],T4.FAInsulinIm1_GPFm1);
plot([1:l],T4.LAInsulinIm1_GPFm1);
grid on;
xlabel('Iteration of the Recommender System');
ylabel('Confidence Level')
title('Confidence Level Comparison k=30')
legend('Demonstrated Strategy','Sample High-level Strategy','Diet Plan','Fast-acting Insulin','Long-acting Insulin')


