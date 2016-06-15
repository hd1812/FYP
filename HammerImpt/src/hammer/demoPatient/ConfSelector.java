package hammer.demoPatient;

import java.util.TreeMap;

import hammer.common.Pair;
import hammer.common.T3Functor;
import hammer.core.Core.RankedWeaver;
import hammer.dictionary.impl.Dictionary;
import hammer.signals.Signals;
import hammer.state.State;

public class ConfSelector implements T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State>
{
	public Pair<Dictionary<String>,Dictionary<String>> Function(TreeMap<String,RankedWeaver>sortedWeavers_,State current,State target){
		Dictionary<String>	actions_=new Dictionary<String>();
		Dictionary<Double>	actionConfidence_=new Dictionary<Double>();
		Dictionary<String>	selectedModels_=new Dictionary<String>();
		Signals tempSignals=new Signals();
		
		for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
		{
			tempSignals=RankedWeaver_.weaver.getSignal();
			for(String str:tempSignals.getKeys()){
				if(!str.equals("Aspect")){
					if(actions_.contains(str)){
						if(actionConfidence_.get(str)<RankedWeaver_.weaver.getConfidence()){
							actions_.put(str,tempSignals.get(str).toString());
							actionConfidence_.put(str, RankedWeaver_.weaver.getConfidence());
							selectedModels_.put(str, RankedWeaver_.weaver.getName());
						}
					}
					else{
						actions_.put(str,tempSignals.get(str).toString());
						actionConfidence_.put(str, RankedWeaver_.weaver.getConfidence());
						selectedModels_.put(str, RankedWeaver_.weaver.getName());
					}
				}
				//System.out.println("action confidence: "+actionConfidence_.get("GlucoseLv"));
			}
		}
		return new Pair<Dictionary<String>, Dictionary<String>>(actions_,selectedModels_);
	}
}
