package neuralNetwork;


public class Network {


	//each neuron is one column in the matrix.
	//there is one row for each input dimension
	private double[][] hiddenLayer;
	private double[] outputWeights;

	//cached temp data (for each set of training data)
	private double[][] hiddenLayerResults;
	private double[][] rawHiddenLayerResults;
	private double[] rawOutputLayerResults;
	private double[] outputLayerResults;
	private double[][] input;
	private double[] expected; 

	public Network(NetworkParams params){
		this(params.getNumHiddenUnits(), params.getNumInputDimensions());
	}


	private Network(int numHiddenUnits, int numInputDimensions){
		hiddenLayer=new double[numInputDimensions][numHiddenUnits];
		outputWeights=new double[numHiddenUnits];
		//initialize rows; one row for each input dimension; each row has one entry per hidden unit (entries are weights)	

		hiddenLayer=new double[][]{{.8, .4, .3},{.2,.9,.5}};

		outputWeights=new double[]{.3, .5, .9};

		/*for(int i=0;i<hiddenLayer.length;i++){
			hiddenLayer[i]=new double[numHiddenUnits];
			//initialize random weights
			for(int j=0;j<numHiddenUnits;j++){
				hiddenLayer[i][j]=Math.random();
			}
		}


		for(int i=0;i<outputWeights.length;i++){
			outputWeights[i]=Math.random();
		}*/

	}


	public void train(double[][] input, double[] expected){

		checkInputs(input, expected);
		normalizeInputs(input, expected);
		saveInputs(input, expected);
		forwardPropagate();

		backPropagate();


	}




	private void backPropagate() {

		for(int i=0;i<hiddenLayerResults.length;i++){
			double[] hiddenResultsForOneInput=hiddenLayerResults[i];
			double output=outputLayerResults[i];
			double rawOutput=rawOutputLayerResults[i];
			double error=expected[i]-output;
			
			System.out.println("Prediction: "+output);
			System.out.println("Error: "+error);
			
			
			double sigPrimeRawOutput=Util.sigmoidPrime(rawOutput);
			//derivative of sigmoid times the error
			double proposedDeltaOutput=error*sigPrimeRawOutput;

			double[] deltaOutputWeights=new double[outputWeights.length];
			//calculate the proposed change in the hidden layer weights
			for(int j=0;j<deltaOutputWeights.length;j++){
				//length of currHiddenLayer results = number of hidden neurons
				deltaOutputWeights[j]=proposedDeltaOutput/hiddenResultsForOneInput[j];
			}

			//calculate delta hidden sums for each hidden neuron
			//"sums" sum over the inputs, across the dimensions. one sum per hidden neuron
			double[] deltaHiddenSums=new double[hiddenResultsForOneInput.length];
			double[] rawHiddenResultsForOneInput=rawHiddenLayerResults[i];

			for(int j=0;j<deltaHiddenSums.length;j++){
				//the proposed change in the hidden sums is the proposed change of the output sum, 
				//divided by ("proportionate to, weighted by") the weighting of the relation between
				//the output and this hidden neuron by the derivative of the function by which it computed it result,
				//taken at that result
				deltaHiddenSums[j]=proposedDeltaOutput/outputWeights[j]*Util.sigmoidPrime(rawHiddenResultsForOneInput[j]);
			}

			//one delta hidden sum per neuron, collapses across each dimension
			//update the input-hidden weights
			double[] currInput=input[i];
			//each row in hidden layer (0-j) corresponds to one dimension (0-currInput.length) of input
			for(int j=0;j<hiddenLayer.length;j++){
				double[] currWeights=hiddenLayer[j]; //should have same length as number of hidden neurons
				//the weight between each hidden neuron and the dimension in question
				double dimensionVal=currInput[j]; //value for the dimension in question
				//go througheach neurons

				if(dimensionVal>0){ //if val contributed to the sum at all
					for(int k=0;k<currWeights.length;k++){
						//adjust the weight of each hidden layer by the proposed change in the hidden layer,
						//divided by the magnitude of the input
						double currWeight=hiddenLayer[j][k];
						double deltaWeight=deltaHiddenSums[k]/dimensionVal;

						hiddenLayer[j][k]+=deltaWeight;
					}
				}

			}

			//update the hidden-output weights
			for(int j=0;j<deltaOutputWeights.length;j++){
				outputWeights[j]+=deltaOutputWeights[j];
			}

		}
		
		
		System.out.println("New output weights");
		Matrix.print(outputWeights);
        System.out.println("New input weights");
        Matrix.print(hiddenLayer);
	}


	private void forwardPropagate() {
		hiddenLayerResults=Matrix.multiply(input, hiddenLayer);
		rawHiddenLayerResults=hiddenLayerResults.clone();
		hiddenLayerResults=Util.sigmoid(hiddenLayerResults);
		//each row in hiddenLayerResults represents the output of each neuron to a single input example.
		rawOutputLayerResults=new double[input.length];
		outputLayerResults=new double[input.length];

		for(int i=0;i<hiddenLayerResults.length;i++){

			double[] hiddenResultsForOneInput=hiddenLayerResults[i];

			double outputsum=Matrix.dotProduct(hiddenResultsForOneInput, outputWeights);
			rawOutputLayerResults[i]=outputsum;
		
			outputsum=Util.sigmoid(outputsum);

			outputLayerResults[i]=outputsum;
			
			
		}

	}


	private void normalizeInputs(double[][] input, double[] expected){
	
		for(int i=0;i<input.length;i++){
			Util.normalize(input[i]);
		}
		Util.normalize(expected);
	}


	private void saveInputs(double[][] input, double[] expected) {
	
		this.input=input;
		this.expected=expected;

	}


	private void checkInputs(double[][] input, double[] expected) {
		if(input.length!=expected.length) throw new IllegalArgumentException("umber of inputs must equal number of expected outputs.");

	}


}
