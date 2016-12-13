package top.robotgg;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

public class VerifyCode {
	public static String predict(BufferedImage img) throws Exception{
		String ansString = "";
		svm_node [][] cNodes = ImageHelper.imageToAttr(img);
		svm_model cModel = svm.svm_load_model(new BufferedReader(new InputStreamReader(VerifyCode.class.getClassLoader().getResource("myMode.mode").openStream())));
		for(int i=0;i<4;i++){
			double s = 0;
			s = svm.svm_predict(cModel, cNodes[i]);
			ansString+=(char)(int)s;
		}
		return ansString;
	}
}