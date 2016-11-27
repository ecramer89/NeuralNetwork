package neuralNetwork;

public class Driver {

	public static void main(String[] args) {

		double[][] inputs={{1,1}};
		double[] expected={0};


       NetworkParams params=new NetworkParams();
       params.setNumHiddenUnits(3);
       params.setNumInputDimensions(2);

		Network n=new Network(params);
		for(int i=0;i<50;i++){
			System.out.println("Training round: "+i);
			n.train(inputs, expected);
			System.out.println();
		}
	}

}
