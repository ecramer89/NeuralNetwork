package neuralNetwork;
import static neuralNetwork.Util.*;



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

	
		double[][] trainingSet={{1,1}, {1,0}, {1,1}, {1,0}, {0,1}, {0,1}, {0,0}};
		double[] expectedTrainingOutputs={123, 234,  340, 28, 55, 79, 120};
		
		
		NetworkParams params=new NetworkParams();
		params.setNumHiddenUnits(3);
		params.setNumInputDimensions(2);

		Network n=new Network(params);
		for(int i=0;i<300;i++){
			shuffle(trainingSet, expectedTrainingOutputs);
			System.out.println("Training round: "+i);
			n.train(trainingSet, expectedTrainingOutputs);
			System.out.println();
		}
	}

}
