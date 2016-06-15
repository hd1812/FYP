package forwardModels;

import java.util.Random;

import org.ejml.simple.SimpleMatrix;
import org.ejml.ops.RandomMatrices;

import hammer.models.SimpleGP;
import hammer.common.T2Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

public class GPFM implements T2Functor<State, State, Signals>
{
	
	public String s="";
	
    public State Function(final State current, final Signals sig)
    {
		// TODO Auto-generated method stub
	    SimpleGP _gp=new SimpleGP();
		SimpleMatrix trainingData=new SimpleMatrix(0,0);
		SimpleMatrix trainingOb=new SimpleMatrix(0,0);
		
		SimpleMatrix testingData= new SimpleMatrix(0,0);
		SimpleMatrix ob=new SimpleMatrix(1,1);
		Double result;
		State pred=new State();
		String[]tokens;
		String tempKey;
		Integer minNumRow=-1;
		SimpleMatrix tempMat=new SimpleMatrix(0,0);
		Double tempData;
		try
		{
			for(String s:sig.getKeys()){
				tokens=s.split("-");
				tempKey=tokens[tokens.length-1];
				//Break if signal is not number
				if(!tempKey.equals("Aspect")){
					//System.out.println("==================start=====================");
					
					//add new data into testing data matrix(observations,y)
					tempData=Double.parseDouble(sig.get(s).toString());
					tempMat=new SimpleMatrix(1,1);
					tempMat.set(0, 0, tempData);
					testingData=testingData.combine(0, testingData.numCols(), tempMat);
					//add data into training data matrix
					tempMat=new SimpleMatrix(0,0);
					String ts=tempKey+"Train";
					if(current.contains(ts)&&current.get(ts)!=null){
						tempMat=current.get(ts);

						if(minNumRow<0){
							minNumRow=tempMat.numRows();
						}
						else if(tempMat.numRows()<minNumRow ){
							minNumRow=tempMat.numRows();
						}
						trainingData=trainingData.combine(0, trainingData.numCols(), tempMat);

					}
				}

			}
			
			//Add G as a training set and testing set
			if(current.contains("GTrain")){
				tempMat=current.get("GTrain");
				trainingData=trainingData.combine(0, trainingData.numCols(), tempMat);
			}
			if(current.contains("G")){
				tempData=current.get("G");
				tempMat=new SimpleMatrix(1,1);
				tempMat.set(0, 0, tempData);
				testingData=testingData.combine(0, testingData.numCols(), tempMat);
			}
			
			//Formatting and reshaping
			//Reshape the size of training data depending on the minimum number of column
			trainingData=trainingData.extractMatrix(0, Math.max(minNumRow, 0), 0, trainingData.numCols());
			
			//add data to observation matrix, target aspect is set to G
			trainingOb=current.get("GOb");
			
			//reshape the trainig and testing matrix to min dimension
			minNumRow=Math.min(trainingOb.numRows(), trainingData.numRows());
			trainingData=trainingData.extractMatrix(0, Math.max(minNumRow, 0), 0, trainingData.numCols());
			trainingOb=trainingOb.extractMatrix(0, Math.max(minNumRow, 0), 0, trainingOb.numCols());
			//testingData=testingData.extractMatrix(0, Math.max(minNumRow, 0), 0, testingData.numCols());

			//train and run GP
			_gp.setSamples(trainingData);
			_gp.setObservations(trainingOb);
			
			result=_gp.mu(testingData);
			//s="Training Data: "+trainingData+"Training Ob: "+trainingOb+" Testing Data: "+testingData+" Pred: "+result+" Kernel: "+_gp.getKernel()+"\n";
			//System.out.print(s);
			
			//check whether result is a number
			if(result.isNaN()){
				//System.out.println("result is nan");
				pred.put("G", 0.0);
			}
			else{
				pred.put("G", result);
			}
			//System.out.println("result nan: "+result.isNaN()+" training data: "+trainingData.toString()+"trainingOb data: "+trainingOb.toString()+"testing data: "+testingData.toString()+"pred result:    "+result);

			//System.out.println("pred result:    "+result);
		} catch (HammerException e)
		{
		}
		return pred;
    }
}