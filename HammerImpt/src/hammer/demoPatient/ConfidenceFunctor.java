package hammer.demoPatient;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import hammer.common.T1Functor;
import hammer.common.T2Functor;
import hammer.confidenceEvaluation.AspectPair;
import hammer.confidenceEvaluation.StateMap;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;

//the confidence funtion is based on blood glucose risk, reference: dc1386
public class ConfidenceFunctor implements T2Functor<Double, StateMap,String>
{
	
	Dictionary<Double> preds=new Dictionary<Double>();
	
	public Double Function(final StateMap myStateMap,final String name)
	{
		Double confidenceDelta = 0.0;
		Double actual=0.0;
		Double preOb=0.0;
		Double lastPred=0.0;
		Lock	criticalLock	= new ReentrantLock();
		
		try
		{
			criticalLock.lock();
		if (myStateMap.contains("G"))
		{
  
			synchronized(this) {
				if(preds.get(name)!=null){
					lastPred=preds.get(name);
				} 
		    }
			
			AspectPair<Object> pair = null;
			pair = myStateMap.get("G");
			
			//actual=(double)pair.actual;//current G cannot be used to compute confidence
			preOb=myStateMap.getActualState().get("CurrentOb");
			//System.out.println("preOb: "+preOb);
			//confidenceDelta=Math.exp(-Math.abs(preOb-lastPred));
			
			//confidenceDelta=Math.max(0, Math.pow(10-Math.abs(preOb-lastPred), 2));
			confidenceDelta=Math.max(0, 10-Math.abs(preOb-lastPred));
			
			if(!myStateMap.getActualState().get("Mode").equals("Train")){
				confidenceDelta=0.0;
			}
			
			preds.put(name, (double)pair.predicted);
			//System.out.println(preds.data.keySet()+" "+preds.data.values());//cause concurrent modification, thread access pred
		}
		criticalLock.unlock();
		} catch (HammerException e)
		{
		}
		
		return confidenceDelta;
	}
}
