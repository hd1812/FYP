package hammer.tutorial1;

import hammer.common.T1Functor;
import hammer.confidenceEvaluation.AspectPair;
import hammer.confidenceEvaluation.StateMap;
import hammer.exception.HammerException;

public class ConfidenceFunctor implements T1Functor<Double, StateMap>
{
	public Double Function(final StateMap myStateMap)
	{
		double confidenceDelta = 0;

		if (myStateMap.contains("distance"))
		{
			AspectPair<Object> pair = null;
			try
			{
				pair = myStateMap.get("distance");
			} catch (HammerException e)
			{
				e.printStackTrace();
			}
			if (pair.actual == pair.predicted)
			{
				confidenceDelta = confidenceDelta + 1;
			} else
			{
				confidenceDelta = confidenceDelta - 1;
			}
		}
		return confidenceDelta;
	}
}
