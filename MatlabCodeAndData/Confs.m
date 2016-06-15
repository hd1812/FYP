close all
clear all

T=readtable('HammerHistory/HammerHistory3.dat');
figure;
hold on;

l=size(T,1);
plot([1:l],T.HighCarbIm_LAInsulinIm2_FAInsulinIm1_GPFm1);
plot([1:l],T.NormalCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);
plot([1:l],T.LowCarbIm_GPFm1);
plot([1:l],T.FAInsulinIm1_GPFm1);
plot([1:l],T.LAInsulinIm1_GPFm1);

grid on;
xlabel('Iteration of the Recommender System');
ylabel('Confidence Level')
title('Confidence Level Comparison on Models in Different Abstract Levels')
legend('Best High-level Strategy','Other High-level Strategy','Diet Plan Low-level Model','Fast-acting Insulin Low-level Model','Long-acting Insulin Low-level Model')

%%
nameArray=T.Properties.VariableNames;
lastValues=T(end,:)
t=zeros(1,size(T,2))
for i=1:(size(nameArray,2)-1)
    a=cell2mat(strfind(nameArray(i),'_'))
    k = size(a,2)
    t(1,i)=k
end

lowMean=mean(table2array(lastValues(1,t==1)))
HighMean=mean(table2array(lastValues(1,t==3)))



