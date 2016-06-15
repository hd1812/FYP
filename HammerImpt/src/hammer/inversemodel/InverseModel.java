package hammer.inversemodel;

import java.util.ArrayList;
import java.util.TreeMap;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.confidencemap.ConfidenceMap;
import hammer.core.Core.RankedWeaver;
import hammer.signals.Signals;
import hammer.state.State;

/**
 * @author hao This represents the basic structure of hammer inverse model This
 *         class is supposed to be inherited and implemented by other inverse
 *         models.
 */

public class InverseModel
{

	/**
	 * Properties of inverse model
	 */
	public String							name_;
	public ArrayList<String>				subscriptions_;
	public ArrayList<String>				dependencies_;
	public T3Functor<Signals, State, State, Signals>	functor_;
	//public State							lastPred_;

	public InverseModel()
	{
		name_ = new String();
		subscriptions_ = new ArrayList<String>();
		dependencies_ = new ArrayList<String>();
	}
	/**
	 * Constructor Initialise inverse model with specified properties
	 * 
	 * @param name
	 * @param subscriptions
	 * @param dependencies
	 */
	public InverseModel(final String name, final ArrayList<String> subscriptions, final ArrayList<String> dependencies,
			final T3Functor<Signals, State, State, Signals> functor)
	{
		this.name_ = name;
		this.subscriptions_ = subscriptions;
		this.dependencies_ = dependencies;
		this.functor_ = functor;
	}

	/**
	 * Generates commands which leads object to target state
	 * 
	 * @param current
	 * @param target
	 * @param confidences
	 * @return
	 */
	public Signals simulate(final State current, final State target, final ConfidenceMap confidences,final TreeMap<String,RankedWeaver> sortedweavers_)
	{
		Signals result=new Signals();
		Signals tempSignal=new Signals();
		Signals newSig=new Signals();
		//debugging
		//System.out.println("simulate functor "+functor_.toString());
		//System.out.println("+++++++++++"+result);
		//Get signals from other other threads if it is a high level model
		for(int i=0;i<dependencies_.size();i++){
			for (RankedWeaver RankedWeaver_ : sortedweavers_.values()){
				if(RankedWeaver_.weaver.imName_.equals(dependencies_.get(i))){
					tempSignal=RankedWeaver_.weaver.getSignal();
					for(String s:tempSignal.getKeys()){
						result.put(name_+"|"+s, tempSignal.get(s));
					}
					break;
				}
			}
		}
		
		//if function is not null run the function inside the model as well
		if(functor_!=null){
			newSig=functor_.Function(current, target,null);
			for(String s:newSig.getKeys()){
				result.put(name_+"|"+s, newSig.get(s));
			}
		}
		//debugging
		//System.out.println("+++++++++++"+result);
		return result;
	}
	
	
	/**
	 * It will be called when model's confidence is reset Override if necessary
	 */
	public void reset()
	{
	};

	/**
	 * Fetch inverse model name
	 * 
	 * @return
	 */
	public final String getName()
	{
		return name_;
	}

	/**
	 * Get subscriptions The subscriptions control which aspects of the state
	 * will the simulate() function receive
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
