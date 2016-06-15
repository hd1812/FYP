package hammer.demoPatient;

import java.util.TreeMap;

import hammer.common.Pair;
import hammer.common.T3Functor;
import hammer.core.Core.RankedWeaver;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

public class ClosestSelector implements T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State>
{
	public Pair<Dictionary<String>,Dictionary<String>> Function(TreeMap<String,RankedWeaver>sortedWeavers_,State current,State target){
		Dictionary<String>	actions_=new Dictionary<String>();
		Dictionary<Double>	actionConfidence_=new Dictionary<Double>();
		Dictionary<String>	selectedModels_=new Dictionary<String>();
		
		Signals tempSignals=new Signals();
		State pred=new State();
		Dictionary<Double>	tempClosestValue=new Dictionary<Double>();
		
		for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
		{
			tempSignals=RankedWeaver_.weaver.getSignal();
			pred=RankedWeaver_.weaver.getPredState();
			for(String aspect:target.getAspects()){
				//if target is defined, find the model closest to target
				if(pred.contains(aspect)){
					for(String ActionCommand:tempSignals.getKeys()){
						if(!ActionCommand.equals("Aspect")){
							if(actions_.contains(ActionCommand)){
								try
								{
									if(Math.abs((Double)pred.get(aspect)-(Double)target.get(aspect))<Math.abs(tempClosestValue.get(aspect)-(Double)target.get(aspect))){
										tempClosestValue.put(aspect,pred.get(aspect));									
										actions_.put(ActionCommand,tempSignals.get(ActionCommand).toString());
										actionConfidence_.put(ActionCommand, RankedWeaver_.weaver.getConfidence());
										selectedModels_.put(ActionCommand, RankedWeaver_.weaver.getName());
									}
								} catch (HammerException e)
								{
								}
							}
							else{
								try
								{
									tempClosestValue.put(aspect,pred.get(aspect));									
									actions_.put(ActionCommand,tempSignals.get(ActionCommand).toString());
									actionConfidence_.put(ActionCommand, RankedWeaver_.weaver.getConfidence());
									selectedModels_.put(ActionCommand, RankedWeaver_.weaver.getName());
								} catch (HammerException e)
								{
								}
							}
						}
					}
				}

				//System.out.println("action confidence: "+actionConfidence_.get("GlucoseLv"));
			}
		}
		return new Pair<Dictionary<String>, Dictionary<String>>(actions_,selectedModels_);
	}
}
