---
12/4/2016 12:48:51 AM 
####### 识别简单的正方教务系统的验证码 

使用 libsvm库 地址：http://www.csie.ntu.edu.tw/~cjlin/liblinear
修改：
1.在libsvm的 svm_node中加入了构造函数
2.删除libsvm目录中的m4文件

MainTest.train()函数已经训练好了模型，识别率讲道理是97.8333%
图片没有经过特殊处理，实验的时候加上去噪函数，会使字符变细，导致
识别率降低，所以就未处理图片，直接识别了
还有很多可以优化的地方，慢慢补充

trainFile:目录下
	A 文件夹下是识别的结果
	sample 是训练集来源
