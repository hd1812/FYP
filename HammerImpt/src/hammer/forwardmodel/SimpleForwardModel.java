package hammer.forwardmodel;

import hammer.signals.Signals;
import hammer.state.State;

import java.util.ArrayList;

import hammer.common.T2Functor;
import hammer.exception.*;

/**
 * @author hao Class inherits and fully implements ForwardModel. It is ideal for
 *         stateless forward models.
 */
public class SimpleForwardModel extends ForwardModel
{

	/**
	 * Initialise a simple forward model with specified functionality
	 * 
	 * @param functor
	 * @throws HammerException
	 */
	public SimpleForwardModel(String name, ArrayList<String> subscriptions, ArrayList<String> dependencies,
			T2Functor<State, State, Signals> functor) throws HammerException
	{
		super(name, subscriptions, dependencies, functor);
		if (functor_ == null)
		{
			throw new HammerException("Empty function argument");
		}
	}
	public SimpleForwardModel(){}

	/**
	 * Create a copy of forward model
	 * 
	 * @param functor
	 * @return
	 * @throws HammerException
	 */
	public static ForwardModel make(String name, ArrayList<String> subscriptions,
			T2Functor<State, State, Signals> functor) throws HammerException
	{
		return new SimpleForwardModel(name, subscriptions, null, functor);
	}

	/*
	 * (non-Javadoc) Execute forward model to predict a result
	 * 
	 * @see hammer.forwardmodel.ForwardModel#predict(hammer.signals.Signals)
	 */
	public State predict(final State current, final Signals inputSignal)
	{
		assert(functor_ != null) : "Empty functor";
		return functor_.Function(current, inputSignal);
	}

}
