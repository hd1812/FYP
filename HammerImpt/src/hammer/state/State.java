package hammer.state;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.rits.cloning.Cloner;

import java.lang.Object;

import hammer.common.T1Functor;
import hammer.confidenceEvaluation.StateMap;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.*;

/**
 * @author hao
 * Class represents the state of the world at a given time-step.
 * It includes everything necessary to identify a state of a world,
 * but ignore additional info of the world for models to run
 */
/**
 * @author hao
 *
 */
public class State
{

	/**
	 * Public implementation of state
	 */
	public Impl pimpl_ = new Impl();

	/**
	 * Pointer to a constant instance of this class.
	 */
	public static final State KPtr = null;

	/**
	 * Deep cloner
	 */
	public Cloner cloner = new Cloner();

	/**
	 * Basic constructor of state, creating a new instance of implementation
	 */
	public State()
	{
		this.pimpl_ = new Impl();
	}
	public State(final Impl pimpl)
	{
		this.pimpl_ = pimpl;
	}

	/**
	 * Copy constructor of state, used in cloning function
	 * 
	 * @param s
	 */
	public State(State s)
	{
		this(s.pimpl_);
	}

	/**
	 * @author hao Constructor of Implementation of state
	 */
	public class Impl
	{
		public Dictionary<Object>			dictionary_;
		public int							timestamp_;
		//public T1Functor<Double, StateMap>	confidenceFunctor_;
		public ArrayList<String>			aspects_;
		Impl()
		{
			this.dictionary_ = new Dictionary<Object>();
			this.timestamp_ = 0;
			this.aspects_ = new ArrayList<String>();
		}
	}

	/**
	 * Create a new instance of state
	 * 
	 * @return
	 */
	public static State make()
	{
		return new State();
	}

	// Create a deep copy of state
	public State clone()
	{
		return cloner.deepClone(this);
	}

	/**
	 * Increment timestamp
	 * object.
	 * 
	 * @param obj
	 * @return
	 */
	public static State next(final State obj)
	{
		State newState = State.make();
		newState.pimpl_.timestamp_ = obj.pimpl_.timestamp_ + 1;
		return newState;
	}

	/**
	 * Factory method to create a new State instance containing a subset of the
	 * aspect in another object
	 * 
	 * @param obj
	 * @param aspects
	 * @return
	 */
	public static State subset(final State obj, final ArrayList<String> aspects)
	{

		State newState = State.make();

		newState.pimpl_.timestamp_ = obj.pimpl_.timestamp_;
		//newState.pimpl_.confidenceFunctor_ = obj.pimpl_.confidenceFunctor_;
		//debugging
		//System.out.println("!!!!!!!!!"+aspects);
		obj.pimpl_.dictionary_.subset(aspects, newState.pimpl_.dictionary_);

		//newState.pimpl_.aspects_ = new ArrayList<String>(newState.pimpl_.aspects_.size());

		return newState;

	}

	//Transferred to core
	
	/**
	 * Compute the confidence value of an inverse model pair.
	 * 
	 * @param Actual
	 * @param Predicted
	 * @param inverseModelName
	 * @return
	 * @throws HammerException
	 */
	/*
	public static double confidence(final State Actual, final State Predicted, final String inverseModelName)
	{
		//debugging
		//System.out.println("ready to run confidence function");
		// Check functions are the same
		if (Actual.pimpl_.confidenceFunctor_ == null)
		{
			try
			{
				//System.out.println("Confidence Function was not defined");
				throw new HammerException("Confidence Function was not defined");
			} catch (HammerException e)
			{
			}
		}

		// Create a new state map
		StateMap stateMap = StateMap.make(Actual, Predicted, inverseModelName);

		// And evaluate the confidence with user-defined function
		//debugging
		//System.out.println("run confidence function");
		return Actual.pimpl_.confidenceFunctor_.Function(stateMap);
	}
	*/
	/**
	 * Check whether an aspect is present in the state or not
	 * 
	 * @param aspect
	 * @return
	 */
	public boolean contains(final String aspect)
	{
		boolean result;
		try
		{
			result = pimpl_.dictionary_.data.containsKey(aspect) && (pimpl_.dictionary_.data.get(aspect) != null);
		} catch (NullPointerException e)
		{
			result = false;
		}
		return result;
	}

	/**
	 * Function to determine whether State contains any aspect.
	 * 
	 * @return
	 */
	public boolean empty()
	{

		if (pimpl_.dictionary_.empty())
		{
			return true;
		}
		for (Entry<String, Object> entry : pimpl_.dictionary_.data.entrySet())
		{
			if (entry.getValue() != null)
				return false;
		}
		return true;

	}

	/**
	 * Returns a string vector with the list of aspects present in state Can be
	 * used for iteration
	 * 
	 * @return
	 */
	public final ArrayList<String> getAspects()
	{
		return pimpl_.aspects_;
	}

	/**
	 * @param aspects
	 */
	public void require(final ArrayList<String> aspects)
	{

		for (final String aspect : aspects)
		{
			if (!contains(aspect))
			{
				pimpl_.dictionary_.put(aspect, null);
			}
		}
		pimpl_.aspects_ = new ArrayList<String>(pimpl_.dictionary_.data.size());
	}

	/**
	 * Define a set of aspects that this state needs to be complete, but do not
	 * give them values yet.
	 * 
	 * @return
	 */
	public boolean consistent()
	{
		for (Entry<String, Object> entry : pimpl_.dictionary_.data.entrySet())
		{
			if (entry.getValue() == null)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Get state timestamp
	 * 
	 * @return
	 */
	public final int getTimestamp()
	{
		return pimpl_.timestamp_;
	}

	/**
	 * set state time stamp
	 * 
	 * @param newTimestamp
	 */
	public void setTimestamp(final int newTimestamp)
	{
		pimpl_.timestamp_ = newTimestamp;
	}

	/**
	 * Set time confidence function
	 * 
	 * @param confidenceFunctor
	 */
	/*
	public void setConfidenceFunction(final T1Functor<Double, StateMap> confidenceFunctor)
	{
		pimpl_.confidenceFunctor_ = confidenceFunctor;
	}
	*/
	
	/**
	 * get aspect
	 * 
	 * @param aspect
	 * @return
	 * @throws HammerException
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String aspect) throws HammerException
	{
		return (T) getAny(aspect); // cast object to type T
	}

	/**
	 * Insert aspect and corresponding value Use deep clone to copy the whole
	 * object
	 * 
	 * @param aspect
	 * @param value
	 */
	public <T> void put(String aspect, Object value)
	{
		putAny(aspect, cloner.deepClone(value));
	}

	/**
	 * Retrieve an aspect from state
	 * 
	 * @param aspect
	 * @return
	 * @throws HammerException
	 */
	private Object getAny(final String aspect) throws HammerException
	{

		final Object result = pimpl_.dictionary_.get(aspect);
		if (result == null)
		{
			throw new HammerException("Aspect was required but never specified");
		}
		return result;

	}

	/**
	 * put an aspect into the state
	 * 
	 * @param aspect
	 * @param value
	 */
	private void putAny(final String aspect, final Object value)
	{
		if (!contains(aspect))
		{
			pimpl_.aspects_.add(aspect);
		}
		pimpl_.dictionary_.put(aspect, value);
	}
	/*
	public static double confidence(final State Actual, final State Predicted, final String inverseModelName)
	{
		//debugging
		//System.out.println("ready to run confidence function");
		// Check functions are the same
		if (Actual.pimpl_.confidenceFunctor_ == null)
		{
			try
			{
				//System.out.println("Confidence Function was not defined");
				throw new HammerException("Confidence Function was not defined");
			} catch (HammerException e)
			{
			}
		}

		// Create a new state map
		StateMap stateMap = StateMap.make(Actual, Predicted, inverseModelName);

		// And evaluate the confidence with user-defined function
		//debugging
		//System.out.println("run confidence function");
		return Actual.pimpl_.confidenceFunctor_.Function(stateMap);
	}
	*/
}
