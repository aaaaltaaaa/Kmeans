import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.datasets import load_iris
import numpy as np
import scipy
import jpype

data=load_iris()
X=data.data
np.savetxt("data.txt",X)

jvmPath="C:/Program Files/Java/jdk-11.0.2/bin/server/jvm.dll"
if not jpype.isJVMStarted():
    jpype.startJVM(jvm=jvmPath)
javaClass = jpype.JClass('Kmeans')
javaInstance = javaClass(3,0)
javaInstance.load("data.txt")
javaInstance.fit_predict("Euclidean")
javaInstance.store("result.txt")

y=np.loadtxt("result.txt")
data=load_iris()
X=data.data

pca=PCA(n_components=2)
reduce_X=pca.fit_transform(X)

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
