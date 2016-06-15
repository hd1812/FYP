package hammer.demoPatient;

import java.util.ArrayList;
import java.util.Arrays;

import hammer.common.T1Functor;
import hammer.common.T2Functor;
import hammer.confidenceEvaluation.AspectPair;
import hammer.confidenceEvaluation.StateMap;
import hammer.exception.HammerException;

//the confidence funtion is based on blood glucose risk, reference: dc1386
public class CombinedConfidenceFunctorPos implements T2Functor<Double, StateMap,String>
{
	public Double Function(final StateMap myStateMap,String aspects)
	{
		Double confidenceDelta = 0.0;
		Double BGI=0.0;
		Double pred=0.0;
		Double alpha=1.026;
		Double gamma=1.794;
		ArrayList<String> Aspects=new ArrayList<String>();
		Double confSum=0.0;
		
		String[]tokens=aspects.split("-");
		String aspect=tokens[tokens.length-1];
		
		//System.out.println("GlucoseLvConfidenceFunctor Called");
		try
		{
		//System.out.println("aspect "+aspect);
		if (aspect.equals("Carb")||aspect.equals("Insulin"))
		{
			AspectPair<Object> pair = null;
			pair = myStateMap.get("G");
			pred= Math.max((double)pair.predicted,18);

			BGI = 10*Math.pow((gamma*(Math.pow(Math.log(pred/18), alpha)-1.861)), 2);
			confidenceDelta=-BGI+100;
		}
		else {
			AspectPair<Object> pair = null;
			Aspects=(ArrayList<String>)myStateMap.getAspects();
			//System.out.println("Aspects "+Aspects);
			for(int i=0;i<Aspects.size();i++){
				pair=myStateMap.get((String)Aspects.get(i));
				confSum+=(double)pair.predicted;
			}
			confidenceDelta=confSum/Aspects.size();
		}
		
		} catch (HammerException e)
		{
		}
		System.out.println("confidenceDelta  "+confidenceDelta +"  G "+pred);
		return confidenceDelta;
	}
}
