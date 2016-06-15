close all;
clear all;

ln1=readtable('hcmp/ln1.dat');

hn1=readtable('hcmp/hn1.dat');
hn2=readtable('hcmp/hn2.dat');
hn3=readtable('hcmp/hn3.dat');
hn4=readtable('hcmp/hn4.dat');
hn5=readtable('hcmp/hn5.dat');

target =112.5;
hyper=180;
hypo=70;
lastN=10*24*60;

varln=(var(ln1.G(end-lastN+1:end)-target))
varhn=(var(hn1.G(end-lastN+1:end)-target)+var(hn2.G(end-lastN+1:end)-target)+var(hn3.G(end-lastN+1:end)-target)+var(hn4.G(end-lastN+1:end)-target)+var(hn5.G(end-lastN+1:end)-target))/5

Hyperln=size(find(ln1.G(end-lastN+1:end)>180),1)/60
Hypoln=size(find(ln1.G(end-lastN+1:end)<70),1)/60

Hyperhn=(size(find(hn1.G(end-lastN+1:end)>180),1)/60+size(find(hn2.G(end-lastN+1:end)>180),1)/60+size(find(hn3.G(end-lastN+1:end)>180),1)/60+size(find(hn4.G(end-lastN+1:end)>180),1)/60+size(find(hn5.G(end-lastN+1:end)>180),1)/60)/5
Hypohn=(size(find(hn1.G(end-lastN+1:end)<70),1)/60+size(find(hn2.G(end-lastN+1:end)<70),1)/60+size(find(hn3.G(end-lastN+1:end)<70),1)/60+size(find(hn4.G(end-lastN+1:end)<70),1)/60+size(find(hn5.G(end-lastN+1:end)<70),1)/60)/5

