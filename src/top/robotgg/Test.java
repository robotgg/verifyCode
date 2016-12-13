package top.robotgg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/*
 * Author:robotgg
 * Mail: robotgg@126.com
 */

public class Test {

	public static void main(String[] args) throws Exception{
		
		//train();System.exit(0);

		int T=19;
		System.out.println("-----start------");
		while(T<20){
			BufferedImage img = ImageHelper.getImage("http://zfxk.zjtcm.net/CheckCode.aspx");
			String ans = predict(img,true);
			T++;
			System.out.println(ans);
		}
		System.out.println("-----End------");
	}
	
	public static String predict(BufferedImage img,boolean isSave) throws Exception{
		String ansString = "";
		svm_node [][] cNodes = ImageHelper.imageToAttr(img);
		svm_model cModel = svm.svm_load_model("models/myMode.mode");
		for(int i=0;i<4;i++){
			double s = 0;
			s = svm.svm_predict(cModel, cNodes[i]);
			ansString+=(char)(int)s;
		}
		if(isSave)ImageIO.write(img, "png", new File("cache/"+ansString+".png"));
		return ansString;
	}

	private static void train() throws Exception{
		
		//ImageHelper.toSingle();
		
		double []lables = ImageHelper.getLables();
		svm_node [][]datasNode = ImageHelper.getAttrs();
		
		svm_problem problem = new svm_problem();
		problem.l = 300 * 4;
		problem.y = lables;
		problem.x = datasNode;
	
		svm_parameter param = new svm_parameter();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		
		param.gamma = 0.0078125;
		param.C = 8.0;
		
		param.cache_size = 1000;
		param.eps = 0.00001;
		
		System.out.println(svm.svm_check_parameter(problem, param));
		
		svm_model model = svm.svm_train(problem, param); 
		
		svm.svm_save_model("models/myMode.mode",model);
		
	}
}
