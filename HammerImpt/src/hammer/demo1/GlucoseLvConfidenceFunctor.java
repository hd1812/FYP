package hammer.demo1;

import hammer.common.T1Functor;
import hammer.common.T2Functor;
import hammer.confidenceEvaluation.AspectPair;
import hammer.confidenceEvaluation.StateMap;
import hammer.exception.HammerException;

public class GlucoseLvConfidenceFunctor implements T2Functor<Double, StateMap,String>
{
	public Double Function(final StateMap myStateMap,final String aspect)
	{
		double confidenceDelta = 0;
		double cmp=0.0;
		if (myStateMap.contains("GlucoseLv"))
		{
			AspectPair<Object> pair = null;
			try
			{
				pair = myStateMap.get("GlucoseLv");
				cmp=(double)pair.actual - (double)pair.predicted;
			} catch (HammerException e)
			{
			}
			confidenceDelta = (0.3-Math.abs(cmp))*2;

		}
		return confidenceDelta;
	}
}
