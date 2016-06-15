package hammer.confidenceEvaluation;

import java.util.ArrayList;

import hammer.exception.HammerException;
import hammer.state.*;

/**
 * @author hao Class to compare the actual State and a State generated by an
 *         inverse-forward pair at a certain timestamp.
 */
public class StateMap
{

	private State				actual_;
	private State				predicted_;
	private ArrayList<String>	aspects_;
	private String				inverseModelName_;

	/**
	 * Constructor
	 * 
	 * @param Actual
	 * @param Predicted
	 * @param InverseModelName
	 */
	public StateMap(final State Actual, final State Predicted, final String InverseModelName)
	{
		this.actual_ = Actual;
		this.predicted_ = Predicted;
		this.inverseModelName_ = InverseModelName;
		aspects_ = new ArrayList<String>(Math.min(Actual.getAspects().size(), Predicted.getAspects().size()));
		for (String aspect : actual_.pimpl_.aspects_)
		{
			//debugging
			//System.out.println("statemap aspect "+aspect);
			if (predicted_.contains(aspect))
			{
				aspects_.add(aspect);
			}
		}
	}

	/**
	 * Create a new instance of statemap
	 * 
	 * @param actual
	 * @param predicted
	 * @param inverseModelName
	 * @return
	 */
	public static StateMap make(final State actual, final State predicted, final String inverseModelName)
	{
		return new StateMap(actual, predicted, inverseModelName);
	}

	/**
	 * Get the aspects of actual and predicted state Throw exception if aspect
	 * does not exist
	 * 
	 * @param aspect
	 * @return
	 * @throws HammerException
	 */
	public <T> AspectPair<T> get(final String aspect) throws HammerException
	{
		if (!actual_.contains(aspect))
		{
			throw new HammerException("Requested aspect: " + aspect + " not found in current state");
		} else if (!predicted_.contains(aspect))
		{
			throw new HammerException("Requested aspect: " + aspect + " not found in predicted state");
		}
		return new AspectPair<T>(actual_.get(aspect), predicted_.get(aspect));
	}

	/**
	 * Check whether aspect is included in both actual and predicted states
	 * 
	 * @param aspect
	 * @return
	 */
	public boolean contains(String aspect)
	{
		// debugging
		// System.out.println("actual" + actual_.contains(aspect) + " predicted
		// "+predicted_.contains(aspect));
		//System.out.println("Aspect: "+aspect);
		//System.out.println("current: "+actual_.contains(aspect));
		//System.out.println("pred: "+predicted_.contains(aspect));
		return actual_.contains(aspect) && predicted_.contains(aspect);
	}

	/**
	 * Check whether state pair is empty
	 * 
	 * @return
	 */
	public boolean empty()
	{
		return actual_.empty() && predicted_.empty();
	}

	/**
	 * Return the list of aspects
	 * 
	 * @return
	 */
	public final ArrayList<String> getAspects()
	{
		return aspects_;
	}

	/**
	 * Get the name of inverse model
	 * 
	 * @return
	 */
	public final String getName()
	{
		return inverseModelName_;
	}
	
	public final void setAspects(ArrayList<String> aspects){
		aspects_=aspects;
	}
	
	public final State getActualState(){
		return actual_;
	}
	public final State getPredState(){
		return predicted_;
	}
}
