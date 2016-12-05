package libsvm;
public class svm_node implements java.io.Serializable
{
	public int index;
	public double value;
	public svm_node(){}
	public svm_node(int index,double value){
		this.index = index;
		this.value = value;
	}
}
