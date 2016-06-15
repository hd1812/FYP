close all
clear all

T1=readtable('HammerHistory.dat');
figure;
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
