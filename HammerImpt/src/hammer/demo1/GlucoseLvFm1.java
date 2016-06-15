package hammer.demo1;

import hammer.common.T2Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

public class GlucoseLvFm1 implements T2Functor<State, State, Signals>
{
	public State Function(final State current, final Signals sig)
	{
		String action = "No action required";
		Double base = 0.0;
		try
		{
			action = (String) sig.get("GlucoseLv");
			base = current.get("GlucoseLv");
		} catch (HammerException e)
		{
		}
		Double result=base;
		if (action.equals("No action required")){
			result = base;
		}
		else if (action.equals("Exercise")){
			result = base-0.1;
		}
		else if (action.equals("Take pills")){
			result = base +0.11;
		}
		else if (action.equals("Insulin Injection")){
			result = base +0.2;
		}
		State pred = State.make();
		pred.put("GlucoseLv", result);
		//debugging
		//System.out.println("Predict: "+result);
		return pred;
	}
}
