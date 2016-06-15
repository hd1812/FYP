package hammer.forwardmodel;

import java.util.ArrayList;

import hammer.common.T2Functor;
import hammer.signals.Signals;
import hammer.state.State;

/**
 * @author hao Class represents a black-box whose input is the current signals
 *         to the robot and its output the predicted state of the world if
 *         commands are executed
 */
public class ForwardModel
{

	/**
	 * Properties of Forward model
	 */
	private String							name_;
	private ArrayList<String>				subscriptions_;
	private ArrayList<String>				dependencies_;
	public T2Functor<State, State, Signals>	functor_;

	/**
	 * Constructor
	 */
	public ForwardModel()
	{
		name_ = new String();
		subscriptions_ = new ArrayList<String>();
		dependencies_ = new ArrayList<String>();
	}
	public ForwardModel(final String name, final ArrayList<String> subscriptions, final ArrayList<String> dependencies,
			final T2Functor<State, State, Signals> functor)
	{
		this.name_ = name;
		this.subscriptions_ = subscriptions;
		this.dependencies_ = dependencies;
		this.functor_ = functor;
	}

	/**
	 * Default predict function returns null Override if possible
	 * 
	 * @param inputSignals
	 *            are proposed commands leading robot to target state
	 * @return
	 */
	public State predict(final State current, final Signals inputSignals)
	{
		return functor_.Function(current, inputSignals);
	}

	/**
	 * Fetch forward model name
	 * 
	 * @return
	 */
	public final String getName()
	{
		return name_;
	}

	/**
	 * It outputs subcriptions
	 * 
	 * @return
	 */
	public final ArrayList<String> getSubcriptions()
	{
		return subscriptions_;
	}

	/**
	 * The dependencies affect which confidences are passed to the model in
	 * simulate(), if no dependencies are provided, simulate will receive the
	 * model's last confidence value.
	 * 
	 * @return
	 */
	public final ArrayList<String> getDependencies()
	{
		return dependencies_;
	}
}
