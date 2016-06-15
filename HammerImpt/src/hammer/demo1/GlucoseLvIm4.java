package hammer.demo1;

import hammer.common.T2Functor;
import hammer.signals.Signals;
import hammer.state.State;

public class GlucoseLvIm4 implements T2Functor<Signals, State, State>
{
	public Signals Function(final State current, final State target)
	{
		Signals result = Signals.make();
		result.put("GlucoseLv", "Exercise");
		return result;
	}
}

