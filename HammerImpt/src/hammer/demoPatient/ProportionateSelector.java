package hammer.demoPatient;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import hammer.common.Pair;
import hammer.common.T3Functor;
import hammer.core.Core.RankedWeaver;
import hammer.dictionary.impl.Dictionary;
import hammer.signals.Signals;
import hammer.state.State;

public class ProportionateSelector implements T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State>
{
	public Pair<Dictionary<String>,Dictionary<String>> Function(TreeMap<String,RankedWeaver>sortedWeavers_,State current,State target){
		Dictionary<String>	actions_=new Dictionary<String>();
		Dictionary<Double>	actionConfidence_=new Dictionary<Double>();
		Dictionary<String>	selectedModels_=new Dictionary<String>();
		Signals tempSignals=new Signals();
		//Dictionary<Double>	ConfSumArray=new Dictionary<Double>();//min conf can be negative
		ArrayList<String> tempActionArray=new ArrayList<String>();
		ArrayList<Double> tempConfArray=new ArrayList<Double>();
		ArrayList<String> tempNameArray=new ArrayList<String>();
		Double confSum=0.0;
		Double tempSum=0.0;
		Random ran=new Random();
		Double wheel=0.0;
		int selected=0;
		
		//initialise action array
		for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
		{
			tempSignals=RankedWeaver_.weaver.getSignal();
			for(String command:tempSignals.getKeys()){
				if(!actions_.contains(command)){
					actions_.put(command, "Dummy");
				}
			}
		}
		
		//for each action command
		for(String action:actions_.data.keySet()){
			confSum=0.0;
			//System.out.println(action);
			for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
			{
				tempSignals=RankedWeaver_.weaver.getSignal();
				if(tempSignals.contains(action)){
					tempActionArray.add(tempSignals.get(action).toString());
					confSum+=RankedWeaver_.weaver.getConfidence();
					tempConfArray.add(confSum);
					tempNameArray.add(RankedWeaver_.weaver.getName());
					//System.out.println(String.format( "%.1f", confSum));
				}
			}
			
			//Go through options to select one
			wheel=ran.nextDouble()*confSum;
			for(int i=0;i<tempConfArray.size();i++){
				if(wheel<=tempConfArray.get(i)){
					selected=i;
					break;
				}
			}
			actions_.put(action,tempActionArray.get(selected).toString());
			actionConfidence_.put(action, tempConfArray.get(selected));
			selectedModels_.put(action, tempNameArray.get(selected));
			
			//clear all temporary arrays
			tempActionArray.clear();
			tempConfArray.clear();
			tempNameArray.clear();
		}
		
		return new Pair<Dictionary<String>, Dictionary<String>>(actions_,selectedModels_);
	}
}
