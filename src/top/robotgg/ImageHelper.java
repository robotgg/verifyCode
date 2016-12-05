package top.robotgg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;  

import javax.imageio.ImageIO;
import javax.transaction.xa.Xid;

import com.sun.tracing.dtrace.ArgsAttributes;

import libsvm.svm_node;
import sun.awt.image.PNGImageDecoder.Chromaticities;
import sun.net.www.content.audio.wav;
import sun.net.www.content.audio.x_aiff;

/*
 * author : robotgg
 * mail:robotgg@126.com
 * function:提供一些方法帮助
 * 			处理图片，进行识别
 */
public class ImageHelper {
	
	private final static int Total = 300;
	private final static int n_m = 16*21;
	
	public static int colorToRGB(int alpha,int red,int green,int blue){
		int pixel = alpha;
		pixel<<=8;pixel+=red;pixel<<=8;pixel+=green;pixel<<=8;pixel+=blue;
		return pixel;
	}
	
	public static double RgbToDoubleValue(int rgbInt){
		int r = (rgbInt>>16)&0xff;
		int g = (rgbInt>>8)&0xff;
		int b = rgbInt&0xff;
		return (0.3*r+0.59*g+0.11*b)/255.0;
	}
	
	public static List<BufferedImage> splitImage(BufferedImage img)throws Exception{
		List<BufferedImage> aList = new ArrayList<BufferedImage>();
		for(int i=2;i<50;i+=12){
			aList.add(img.getSubimage(i, 1, 16, 21));
		}
		return aList;
	}
	
	/*
	 * function:图片转化为4个小图
	 */
	public static void toSingle() throws Exception{
		for(int i=0;i<Total;i++){
			BufferedImage img = ImageIO.read(new File("trainFile/samples/"+i+".png"));
			List<BufferedImage> list = splitImage(img);
			for(int j=0;j<4;j++){
				BufferedImage tBufferedImage = list.get(j);
				ImageIO.write(tBufferedImage, "png", new File("trainFile/sample_single/"+i+"-"+j+".png"));
			}
		}
	}
	
	public static double [] getLables() throws Exception{
		double lables[] = new double[Total*4];
		BufferedReader in = new BufferedReader(new FileReader("trainFile/answer.txt"));
		String str = in.readLine();
		int i = 0;
		while(str != null){
			for(int j=0;j<4;j++)
				lables[i++] = (int)(str.charAt(j));
			str = in.readLine();
		}
		in.close();
		return lables;
	}
	
	public static svm_node[][] getAttrs() throws Exception{
		svm_node [][]tmpNode = new svm_node[Total*4][n_m];//1200是数据总数，16*21是图片大小，也就是每一条数据的列数
		BufferedImage img=null;
		BufferedWriter out = new BufferedWriter(new FileWriter("H:\\zzz.txt"));//写入到文件，使用grid.py 计算最优化的g,c参数
		double [] lables = getLables();
		int sz=0;
		for(int i=0;i<Total;i++){
			for(int j=0;j<4;j++){
				img = getImage("trainFile/sample_single/"+i+"-"+j+".png");
				//ImageIO.write(img, "png",new File("trainFile/cc/"+i+"-"+j+".png"));
				int index = 0;
				out.write(lables[sz]+" ");
				for(int x=0;x<img.getWidth();x++)
					for(int y=0;y<img.getHeight();y++){
						double val = RgbToDoubleValue(img.getRGB(x, y));
						tmpNode[sz][index] = new svm_node(index+1,val);
						out.write((index+1)+":"+val+" ");
						index++;
					}
				sz++;
				out.newLine();
			}
		}
		out.close();
		System.out.println("一共有 ：" + sz +" 条数据");
		return tmpNode;
	}
	
	public static BufferedImage getImage(String imagePath) throws MalformedURLException, IOException{
		BufferedImage cBufferedImage = null;
		try{
			cBufferedImage = ImageIO.read(new File(imagePath));
		}catch(Exception e){
			cBufferedImage = ImageIO.read(new URL(imagePath));
		}
		return cBufferedImage;
	}
	
	public static svm_node[][] imageToAttr(BufferedImage cBufferedImage) throws Exception{
		svm_node [][]cNode = new svm_node[4][n_m];
		List<BufferedImage> cList = splitImage(cBufferedImage);
		int f=0;
		for (BufferedImage img : cList){
			int index = 0;
			for(int i=0;i<img.getWidth();i++){
				for(int j=0;j<img.getHeight();j++){
					double val = RgbToDoubleValue(img.getRGB(i, j));
					cNode[f][index] = new svm_node(index+1,val);
					index++;
				}
			}
			f++;
		}
		return cNode;
	}
	
	/*function:去除噪点
	 * @param:BufferedImage
	 * @return:BufferedImage
	 * */
	public static BufferedImage erosion(BufferedImage img) throws Exception{
		int n = img.getWidth(), m = img.getHeight();
		BufferedImage newBufferedImage = new BufferedImage(n, m, img.getType());
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				int pixel = img.getRGB(i, j);
				if(pixel != -16777063){
					newBufferedImage.setRGB(i, j, Color.WHITE.getRGB());continue;
				}
				newBufferedImage.setRGB(i, j, Color.BLUE.getRGB());
			}
		}
		return newBufferedImage;
	}
}
