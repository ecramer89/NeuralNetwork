package neuralNetwork;

public class Matrix {



	public static double[][] multiply(double[][] a, double[][] b){
		if(numCols(a)!=numRows(b)) throw new IllegalArgumentException("Cannot multiply: illegal matrix dimensions");

		double[][] result=new double[numRows(a)][numCols(b)];
		double[][] temp=rotate(b);
		for(int i=0;i<a.length;i++){
			double[] aRow=a[i];
			for(int j=0;j<temp.length;j++){
				double[] bRow=temp[j];
				result[i][j]=dotProduct(aRow,bRow);
			}
		}
		return result;
	}



	public static double dotProduct(double[] aRow, double[] bRow) {
		if(aRow.length!=bRow.length) throw new IllegalArgumentException("Cannot calculate dot product: vectors have unequal length");
		double result=0;
		
		for(int i=0;i<aRow.length;i++){
			result+=aRow[i]*bRow[i];
		}
		
		return result;
	}

	public static void print(double[] a){
		StringBuffer s=new StringBuffer();
		s.append("{");
		for(int i=0;i<a.length;i++){
			s.append(a[i]);
			if(i<a.length-1) s.append(",");
			
		}
		s.append("}");
		
		System.out.println(s.toString());
	}

	public static void print(double[][] a){

		for(int i=0;i<a.length;i++){
			StringBuffer buff=new StringBuffer();
			buff.append("{");
			for(int j=0;j<a[i].length;j++){
				buff.append(a[i][j]);
				if(j<a[i].length-1) buff.append(",");
			}
			buff.append("}");
			System.out.println(buff.toString());
		}
	}



	public static double[][] rotate(double[][] b) {
		double[][] result=new double[b[0].length][b.length];
		int i=b[0].length-1;
		for(;i>-1;i--){
			double[] row=new double[b.length];
			for(int j=0;j<b.length;j++){
				row[j]=b[j][i];
			}
			result[i]=row;
		}

		return result;


	}



	public static int numRows(double[][] a){
		if(a==null) return 0;
		return a.length;
	}

	public static int numCols(double[][] a){
		if(a==null||a.length==0||a[0]==null) return 0;
		return a[0].length;
	}



	

}
