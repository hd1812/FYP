package hammer.demoPatient;

import java.util.ArrayList;

import hammer.common.T1Functor;
import hammer.confidenceEvaluation.AspectPair;
import hammer.confidenceEvaluation.StateMap;
import hammer.exception.HammerException;

//the confidence funtion is based on blood glucose risk, reference: dc1386
public class QualityFunctorPos implements T1Functor<Double, StateMap>
{
	public Double Function(final StateMap myStateMap)
	{
		Double confidenceDelta = 0.0;
		Double BGI=0.0;
		Double pred=0.0;
		Double alpha=1.026;
		Double gamma=1.794;
		
		ArrayList<String> Aspects=new ArrayList<String>();
		Double confSum=0.0;
		
		//System.out.println("GlucoseLvConfidenceFunctor Called");
		try
		{
		if (myStateMap.contains("G"))
		{
			AspectPair<Object> pair = null;
			pair = myStateMap.get("G");
			pred= (double)pair.predicted;

			BGI = 10*Math.pow((gamma*(Math.pow(Math.log(pred/18), alpha)-1.861)), 2);
			confidenceDelta=-BGI+100;
		}
		else {
			AspectPair<Object> pair = null;
			Aspects=(ArrayList<String>)myStateMap.getAspects();
			for(int i=0;i<Aspects.size();i++){
				pair=myStateMap.get((String)Aspects.get(i));
				confSum+=(double)pair.predicted;
			}
			confidenceDelta=confSum/Aspects.size();
		}
		
		} catch (HammerException e)
		{
		}
		//System.out.println("confidenceDelta  "+confidenceDelta +"  G "+pred);
		return confidenceDelta;
	}
}
