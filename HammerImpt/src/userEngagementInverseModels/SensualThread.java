package userEngagementInverseModels;

import hammer.common.T2Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

//normal carb ingestion 45-60 a meal
public class SensualThread implements T2Functor<Signals, State, State>
{
	public Signals Function(final State current, final State target)
	{
		Signals result = Signals.make();
		int time=0;
		try
		{
			time=current.get("Time");
			result.put("UE", "SensualThread");
			result.put("Aspect","UE");
		} catch (HammerException e)
		{
		}
		return result;
	}
}

