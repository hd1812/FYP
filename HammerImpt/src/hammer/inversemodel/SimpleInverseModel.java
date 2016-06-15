package hammer.inversemodel;

import java.util.ArrayList;
import java.util.TreeMap;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.confidencemap.ConfidenceMap;
import hammer.core.Core.RankedWeaver;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

/**
 * @author hao Class inherits and fully implements InverseModel. It is ideal for
 *         stateless inverse models.
 */
public class SimpleInverseModel extends InverseModel
{

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param subscriptions
	 * @param dependencies
	 * @param functor
	 * @throws HammerException
	 */
	public SimpleInverseModel(String name, ArrayList<String> subscriptions, ArrayList<String> dependencies,
			T3Functor<Signals, State, State,Signals> functor) throws HammerException
	{
		super(name, subscriptions, dependencies, functor);
	}
	public SimpleInverseModel(){}

	/**
	 * It creates a new instance of signals
	 * 
	 * @param name
	 * @param subscriptions
	 * @param functor
	 * @return
	 * @throws HammerException
	 */
	public static InverseModel make(final String name, final ArrayList<String> subscriptions,
			final T3Functor<Signals, State, State,Signals> functor) throws HammerException
	{
		return new SimpleInverseModel(name, subscriptions, null, functor);
	}

	public Double lastOb=0.0;
	
	/*
	 * (non-Javadoc) Method calls function specified in constructor and ignores
	 * the confidence map.
	 * 
	 * @see hammer.inversemodel.InverseModel#simulate(hammer.state.State,
	 * hammer.state.State, hammer.confidencemap.ConfidenceMap)
	 */
	public Signals simulate(final State current, final State target, final ConfidenceMap confidences,final TreeMap<String,RankedWeaver> sortedweavers_)
	{
		Signals result=new Signals();
		Signals tempSignal=new Signals();
		Signals newSig=new Signals();
		Signals lowerSig=new Signals();
		String[] tokens;
		Integer temp=0;
		Integer index=0;
		
		try
		{
			lastOb=current.get("LastOb");
		} catch (HammerException e)
		{
		}
		
		//debugging
		//System.out.println("simulate functor "+functor_.toString());
		//System.out.println("+++++++++++"+result);
		//Get signals from other other threads if it is a high level model
		for(int i=0;i<dependencies_.size();i++){
			for (RankedWeaver RankedWeaver_ : sortedweavers_.values()){
				if(RankedWeaver_.weaver.imName_.equals(dependencies_.get(i))){
					tempSignal=RankedWeaver_.weaver.getSignal();
					for(String s:tempSignal.getKeys()){
						tokens=s.split("-");
						temp=Integer.parseInt(tokens[0])+1;
						result.put(temp.toString()+"-"+tokens[tokens.length-1], tempSignal.get(s)); //encode abstract level with aspect
					}
					break;
				}
			}
		}
		
		//if function is not null run the function inside the model as well
		lowerSig=result.clone();
		if(functor_!=null){
			newSig=functor_.Function(current, target,lowerSig);
			for(String s:newSig.getKeys()){
				tokens=s.split("-");
				if(tokens.length==1){
					result.put(0+"-"+tokens[tokens.length-1], newSig.get(s));
				}
				else{
					temp=Integer.parseInt(tokens[0])+1;
					result.put(temp.toString()+"-"+tokens[tokens.length-1], newSig.get(s)); //encode abstract level with aspect
				}
			}
		}
		
		//debugging
		//System.out.println("+++++++++++"+result);
		return result;
	}
}
