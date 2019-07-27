import random
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.datasets import load_iris
import numpy as np
import scipy

data=load_iris()
X=data.data

pca=PCA(n_components=2)
reduce_X=pca.fit_transform(X)
X=reduce_X

n_clusters=3
time=100

mean=[]
cov=np.cov(np.transpose(X))
for i in range(n_clusters):
    mean.append(X[(int)(random.random()*len(X))])
change=100
label=np.zeros(len(X))
for t in range(time):
    change=0
    for n in range(len(X)):
        distance=0
        for i in range(n_clusters):
            temp=(np.transpose((X[n]-mean[i])).dot(np.linalg.inv(cov)).dot(X[n]-mean[i]))**0.5
            if temp <distance or i==0 :
                distance=temp
                label[n]=i
                change+=1
    cluster=[[],[],[]]
    for i in range(len(X)):
        cluster[(int)(label[i])].append(X[i])
    for i in range(n_clusters):
        mean[i]=np.mean(np.array(cluster[i]),axis=0)


y=label
red_x,red_y=[],[]
blue_x,blue_y=[],[]
green_x,green_y=[],[]
for i in range(len(reduce_X)):
    if y[i]==0:
        red_x.append(reduce_X[i][0])
        red_y.append(reduce_X[i][1])
    elif y[i]==1:
        blue_x.append(reduce_X[i][0])
        blue_y.append(reduce_X[i][1])
    else:
        green_x.append(reduce_X[i][0])
        green_y.append(reduce_X[i][1])
plt.scatter(red_x,red_y,c='r',marker='x')
plt.scatter(blue_x,blue_y,c='b',marker='D')
plt.scatter(green_x,green_y,c='g',marker='.')
plt.show()

