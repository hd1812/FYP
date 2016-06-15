package hammer.tutorial1;

import hammer.common.T2Functor;
import hammer.signals.Signals;
import hammer.state.State;

public class ForwardModel1Functor implements T2Functor<State, State, Signals>
{
	public State Function(final State current, final Signals sig)
	{
		final String speed = (String) sig.get("speed");
		int distance;
		if (speed == "slow")
			distance = 1;
		else if (speed == "normal")
			distance = 2;
		else
			distance = 3;
		State result = State.make();
		result.put("distance", distance);
		return result;
	}
}
