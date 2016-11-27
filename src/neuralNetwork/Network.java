package neuralNetwork;


/*
 * uses online training. need to make it so we visit the inputs in a randomorder (not always the same or else
 * it will always privelige the first input)
 * 
 * 
 */


public class Network {

	private boolean test=true;
	private double[][] testHiddenLayer =new double[][]{{.8, .4, .3},{.2,.9,.5}};
	//expect updated weights: 0.712, .355, .268, .112, .855, .468
	private double[] testOutputWeights =new double[]{.3, .5, .9};
	//expect .116 .329 .708


	//each neuron is one column in the matrix.
	//there is one row for each input dimension
	private double[][] hiddenLayer;
	private double[] outputWeights;

	//cached temp data (for each set of training data)
	private double[][] hiddenLayerResults;
	private double[][] rawHiddenLayerResults;
	private double[] rawOutputLayerResults;
	private double[] outputLayerResults;
	private double[] errors;
	private double[][] input;
	private double[] expected; 
	

	public Network(NetworkParams params){
		this(params.getNumHiddenUnits(), params.getNumInputDimensions());
	}


	private Network(int numHiddenUnits, int numInputDimensions){
		hiddenLayer=new double[numInputDimensions][numHiddenUnits];
		outputWeights=new double[numHiddenUnits];
		//initialize rows; one row for each input dimension; each row has one entry per hidden unit (entries are weights)	
      
		if(test){
			hiddenLayer=testHiddenLayer;
			outputWeights=testOutputWeights;
		} else{
			setRandomInitialWeigths(numHiddenUnits);
		}

	}


	private void setRandomInitialWeigths(int numHiddenUnits){
		for(int i=0;i<hiddenLayer.length;i++){
			hiddenLayer[i]=new double[numHiddenUnits];
			//initialize random weights
			for(int j=0;j<numHiddenUnits;j++){
				hiddenLayer[i][j]=Math.random();
			}
		}


		for(int i=0;i<outputWeights.length;i++){
			outputWeights[i]=Math.random();
		}
	}


	
	public void train(Input[] input){
		
	}
	
	

	public void train(double[][] input, double[] expected){

		checkInputs(input, expected);
		normalizeInputs(input, expected);
		saveInputs(input, expected);
		forwardPropagate();
		backPropagate();


	}




	private void backPropagate() {

		System.out.println("Predictions: ");
		Matrix.print(outputLayerResults);
		System.out.println("Errors: ");
		Matrix.print(errors);
		
		for(int i=0;i<hiddenLayerResults.length;i++){
			double[] hiddenResultsForOneInput=hiddenLayerResults[i];
			double output=outputLayerResults[i];
			double rawOutput=rawOutputLayerResults[i];
			double error=errors[i];

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
		calculateHiddenOuputs();
		calculateOutputs();
	}


	private void calculateHiddenOuputs(){
		hiddenLayerResults=Matrix.multiply(input, hiddenLayer);
		rawHiddenLayerResults=hiddenLayerResults.clone();
		hiddenLayerResults=Util.sigmoid(hiddenLayerResults);
	}


	private void calculateOutputs(){
		rawOutputLayerResults=new double[input.length];
		outputLayerResults=new double[input.length];

		for(int i=0;i<hiddenLayerResults.length;i++){

			calculateOutput(i);
			calculateError(i);


		}
	}
	
	private void calculateOutput(int inputIndex){
		double[] hiddenResultsForOneInput=hiddenLayerResults[inputIndex];

		double outputsum=Matrix.dotProduct(hiddenResultsForOneInput, outputWeights);
		rawOutputLayerResults[inputIndex]=outputsum;

		outputsum=Util.sigmoid(outputsum);

		outputLayerResults[inputIndex]=outputsum;
	}
	
	
	
	private void calculateError(int inputIndex){
		double output=outputLayerResults[inputIndex];
		double error=expected[inputIndex]-output;
		errors[inputIndex]=error;
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
		errors=new double[input.length];

	}


	private void checkInputs(double[][] input, double[] expected) {
		if(input.length!=expected.length) throw new IllegalArgumentException("umber of inputs must equal number of expected outputs.");

	}


}
