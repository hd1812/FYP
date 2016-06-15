close all
clear all

T1=readtable('pt/pt.dat');
T2=readtable('pt/npt.dat');

figure;
subplot(1,2,1)
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
title('Confidence Level Variation with Pretraining')
legend('Demonstrated Strategy','Sample High-level Strategy','Diet Plan','Fast-acting Insulin','Long-acting Insulin')

subplot(1,2,2)
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
title('Confidence Level Variation without Pretraining')
legend('Demonstrated Strategy','Sample High-level Strategy','Diet Plan','Fast-acting Insulin','Long-acting Insulin')


