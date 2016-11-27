package neuralNetwork;



/* to do
 * 
 * -inputs normalized before passed to network
 * -network amples from input data randomly (or shuffles the array)
 * 
 * 
 * 
 * 
 */
public class Driver {

	public static void main(String[] args) {

	
		double[][] inputs={{1,1}, {2,2}, {8, 9}, {1, 2}, {9, 3}, {1, 2}, {3, 6}};
		double[] expected={0, 1};
		
		
		Util.shuffle(inputs);
		Matrix.print(inputs);

/*
		NetworkParams params=new NetworkParams();
		params.setNumHiddenUnits(3);
		params.setNumInputDimensions(2);

		Network n=new Network(params);
		for(int i=0;i<100;i++){
			System.out.println("Training round: "+i);
			n.train(inputs, expected);
			System.out.println();
		}*/
	}

}
