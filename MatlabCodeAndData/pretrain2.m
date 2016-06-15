close all;
clear all;

pt20=readtable('pretrain2/pretrain20.dat');

npt20=readtable('pretrain2/nopretrain20.dat');

target =112.5;
hyper=180;
hypo=70;
lastN=20*24*60;

varpt=var(pt20.G(end-lastN+1:end)-target)
varnpt=var(npt20.G(end-lastN+1:end)-target)

Hyperpt=size(find(pt20.G(end-lastN+1:end)>180),1)/60
Hypopt=size(find(pt20.G(end-lastN+1:end)<70),1)/60

Hypernpt=size(find(npt20.G(end-lastN+1:end)>180),1)/60
Hyponpt=size(find(npt20.G(end-lastN+1:end)<70),1)/60

figure;
plot(pt20.G)

figure;
plot(npt20.G)