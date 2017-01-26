package neuralNetwork;

public class Util<T> {

	public static double[][] sigmoid(double[][] matrix){
		double[][] result=new double[matrix.length][matrix[0].length];

		for(int i=0;i<result.length;i++){
			for(int j=0;j<result[i].length;j++){
				result[i][j]=sigmoid(matrix[i][j]);
			}
		}



		return result;
	}


	/* shuffle the array values in place*/
	public static void shuffle(double[][] toShuffle, double[] dependent){
		for(int i=0;i<toShuffle.length;i++){
			//pick a random new index to place each value 
			int rand=(int)(Math.random()*(toShuffle.length-1));
			swap(toShuffle, rand, i);
			swap(dependent, rand, i);
		}
	}
	
	private static void swap(double[] arr, int idxA, int idxB){
		double temp=arr[idxA];
		//swap
		arr[idxA]=arr[idxB];
		arr[idxB]=temp;
	}
	
	private static void swap(double[][] arr, int idxA, int idxB){
		double[] temp=arr[idxA];
		//swap
		arr[idxA]=arr[idxB];
		arr[idxB]=temp;
	}


	public static double sigmoid(double x) {
		return 1/(1+Math.pow(Math.E, -x));
	}

	public static  double sigmoidPrime(double x){
		double sig=sigmoid(x);
		return sig*(1-sig);

	}


	//assumes that inputs are positive
	public static void normalize(double[] in){
		double max=findLargest(in);
		if(max>0){
			for(int i=0;i<in.length;i++){
				in[i]/=max;
			}
		}
	}

	private static double findLargest(double[] in) {
		// TODO Auto-generated method stub
		double largest=-Double.MAX_VALUE;
		for(int i=0;i<in.length;i++){
			if(in[i]<0) throw new IllegalArgumentException("Inputs can't be negative");
			if(in[i]>largest) largest=in[i];
		}

		return largest;

	}






}
