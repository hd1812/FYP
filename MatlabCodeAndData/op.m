close all;
clear all;

opnt1=readtable('op/opnt1.dat');
opnt2=readtable('op/opnt2.dat');
opnt3=readtable('op/opnt3.dat');
opnt4=readtable('op/opnt4.dat');
opnt5=readtable('op/opnt5.dat');

figure;

vrange=50*24*60;
target=112.5;

gl=opnt1.G;
vArray1=[];
for i=1:size(gl,1)-vrange+1
    vArray1=[vArray1;var(gl(i:i+vrange-1)-target)];
end

gl=opnt2.G;
vArray2=[];
for i=1:size(gl,1)-vrange+1
    vArray2=[vArray2;var(gl(i:i+vrange-1)-target)];
end

gl=opnt3.G;
vArray3=[];
for i=1:size(gl,1)-vrange+1
    vArray3=[vArray3;var(gl(i:i+vrange-1)-target)];
end

gl=opnt4.G;
vArray4=[];
for i=1:size(gl,1)-vrange+1
    vArray4=[vArray4;var(gl(i:i+vrange-1)-target)];
end

gl=opnt5.G;
vArray5=[];
for i=1:size(gl,1)-vrange+1
    vArray5=[vArray5;var(gl(i:i+vrange-1)-target)];
end

figure;
plot([1:size(vArray3,1)]/1440,(vArray1+vArray2+vArray3+vArray4+vArray5)/5);
grid on;
xlabel('Day');
ylabel('Variance')
title('Glucose variance in 100-day glucose level simulation.')

%%
target =112.5;
hyper=180;
hypo=70;
lastN=20*24*60;

gl=opnt1.G;
itv=10*24*60;


gl=opnt2.G;
itv=10*24*60;
hpArray1=[];
for i=1:floor(size(gl,1)/itv)
    Hyper=size(find(gl((i-1)*itv+1:i*itv)>hyper),1)/60
    Hypo=size(find(gl((i-1)*itv+1:i*itv)<hypo),1)/60
    hphp=Hyper+Hypo;
    hpArray1=[hpArray1,hphp];
end

gl=opnt3.G;
itv=10*24*60;
hpArray2=[];
for i=1:floor(size(gl,1)/itv)
    Hyper=size(find(gl((i-1)*itv+1:i*itv)>hyper),1)/60
    Hypo=size(find(gl((i-1)*itv+1:i*itv)<hypo),1)/60
    hphp=Hyper+Hypo;
    hpArray2=[hpArray2,hphp];
end

gl=opnt4.G;
itv=10*24*60;
hpArray3=[];
for i=1:floor(size(gl,1)/itv)
    Hyper=size(find(gl((i-1)*itv+1:i*itv)>hyper),1)/60
    Hypo=size(find(gl((i-1)*itv+1:i*itv)<hypo),1)/60
    hphp=Hyper+Hypo;
    hpArray3=[hpArray3,hphp];
end

gl=opnt5.G;
itv=10*24*60;
hpArray4=[];
for i=1:floor(size(gl,1)/itv)
    Hyper=size(find(gl((i-1)*itv+1:i*itv)>hyper),1)/60
    Hypo=size(find(gl((i-1)*itv+1:i*itv)<hypo),1)/60
    hphp=Hyper+Hypo;
    hpArray4=[hpArray4,hphp];
end

hpArray5=[];
for i=1:floor(size(gl,1)/itv)
    Hyper=size(find(gl((i-1)*itv+1:i*itv)>hyper),1)/60
    Hypo=size(find(gl((i-1)*itv+1:i*itv)<hypo),1)/60
    hphp=Hyper+Hypo;
    hpArray5=[hpArray5,hphp];
end

plot([5:10:95],(hpArray1+hpArray2+hpArray3+hpArray4+hpArray5)/5,'-')
grid on;
xlabel('Day');
ylabel('Hours')
axis([0 100 12 32])
title('Number of hours of hyper- or hypo-glycemia in each 10-day interval in the 100-day simuation')

%%
opt1=readtable('op/opt1.dat');

vrange=50*24*60;
target=112.5;

gl=opt1.G;
vArray1=[];
for i=1:size(gl,1)-vrange+1
    vArray1=[vArray1;var(gl(i:i+vrange-1)-target)];
end

figure;
subplot(2,1,1)
plot([1:size(vArray1,1)]/1440,vArray1)

grid on;
xlabel('Day');
ylabel('Variance')
title('Number of hours of hyper- or hypo-glycemia in each 10-day interval in the 100-day simuation')


subplot(2,1,2)
gl=opnt1.G;
vArray5=[];
for i=1:size(gl,1)-vrange+1
    vArray5=[vArray5;var(gl(i:i+vrange-1)-target)];
end
plot([1:size(vArray5,1)]/1440,vArray5)
grid on;
xlabel('Day');
ylabel('Variance')
title('Number of hours of hyper- or hypo-glycemia in each 10-day interval in the 100-day simuation')

figure;
plot([1:size(vArray1,1)-200]/1440,vArray1(201:end))
hold on
plot([1:size(vArray5,1)]/1440,vArray5)
legend('Combined Knowledge','Built-in Knowledge')
axis([0 50 500 1200])
xlabel('Day');
ylabel('Variance')
axis([0 ])
title('Glucose variance comparison using system built-in knowledge and combined knowledge with the expert')
grid on;

%%


T=readtable('op/HammerHistory.dat');
figure;
hold on;

l=size(T,1);
plot([1:l],T.VeryLowCarbIm_LAInsulinIm2_FAInsulinIm1_GPFm1);
plot([1:l],T.LowCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);
plot([1:l],T.VeryLowCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);
plot([1:l],T.NormalCarbIm_LAInsulinIm1_FAInsulinIm1_GPFm1);

grid on;
xlabel('Iteration of the Recommender System');
ylabel('Confidence Level')
axis([0 40 0 160])
title('Confidence level variations on various strategies')
legend('Best Strategy','Other Strategy 1','Other Strategy 2','Other Strategy 3')



