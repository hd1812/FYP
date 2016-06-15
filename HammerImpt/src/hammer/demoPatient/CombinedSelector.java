package hammer.demoPatient;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import hammer.common.Pair;
import hammer.common.T3Functor;
import hammer.core.Core.RankedWeaver;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

public class CombinedSelector implements T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State>
{
	public Pair<Dictionary<String>,Dictionary<String>> Function(TreeMap<String,RankedWeaver>sortedWeavers_,State current,State target){
	
		
		//Define exploitation/exploration trade off threshold
		//0-th1 Closest, th1-th2 max conf, th2-1 Proportionate
		Double th1=0.2; 
		Double th2=0.5;
		Random ran=new Random();
		
		//Closest
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
										actions_.put(ActionCommand,(String) tempSignals.get(ActionCommand));
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
									actions_.put(ActionCommand,(String) tempSignals.get(ActionCommand));
									actionConfidence_.put(ActionCommand, RankedWeaver_.weaver.getConfidence());
									selectedModels_.put(ActionCommand, RankedWeaver_.weaver.getName());
								} catch (HammerException e)
								{
								}
							}
						}
					}
				}
				//if target is not defined, find the model with highest confidence
				else{
					
					for(String ActionCommand:tempSignals.getKeys()){
						if(!ActionCommand.equals("Aspect")){
							if(actions_.contains(ActionCommand)){
								if(actionConfidence_.get(ActionCommand)<RankedWeaver_.weaver.getConfidence()){
									actions_.put(ActionCommand,tempSignals.get(ActionCommand).toString());
									actionConfidence_.put(ActionCommand, RankedWeaver_.weaver.getConfidence());
									selectedModels_.put(ActionCommand, RankedWeaver_.weaver.getName());
								}
							}
							else{
								actions_.put(ActionCommand,tempSignals.get(ActionCommand).toString());
								actionConfidence_.put(ActionCommand, RankedWeaver_.weaver.getConfidence());
								selectedModels_.put(ActionCommand, RankedWeaver_.weaver.getName());
							}
						}
						//System.out.println("action confidence: "+actionConfidence_.get("GlucoseLv"));
					}
					
				}
				//System.out.println("action confidence: "+actionConfidence_.get("GlucoseLv"));
			}
		}
		
		//Max confidence part
		Dictionary<String>	actions2_=new Dictionary<String>();
		Dictionary<Double>	actionConfidence2_=new Dictionary<Double>();
		Dictionary<String>	selectedModels2_=new Dictionary<String>();
		Signals tempSignals2=new Signals();
		
		for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
		{
			tempSignals2=RankedWeaver_.weaver.getSignal();
			for(String str:tempSignals2.getKeys()){
				if(!str.equals("Aspect")){
					if(actions2_.contains(str)){
						if(actionConfidence2_.get(str)<RankedWeaver_.weaver.getConfidence()){
							actions2_.put(str,tempSignals2.get(str).toString());
							actionConfidence2_.put(str, RankedWeaver_.weaver.getConfidence());
							selectedModels2_.put(str, RankedWeaver_.weaver.getName());
						}
					}
					else{
						actions2_.put(str,tempSignals2.get(str).toString());
						actionConfidence2_.put(str, RankedWeaver_.weaver.getConfidence());
						selectedModels2_.put(str, RankedWeaver_.weaver.getName());
					}
				}
				//System.out.println("action confidence: "+actionConfidence_.get("GlucoseLv"));
			}
		}
		
		//Proportionate Selector
		Dictionary<String>	actions3_=new Dictionary<String>();
		Dictionary<Double>	actionConfidence3_=new Dictionary<Double>();
		Dictionary<String>	selectedModels3_=new Dictionary<String>();
		Signals tempSignals3=new Signals();
		Dictionary<Double>	maxConfMap3=new Dictionary<Double>();//min conf can be negative
		ArrayList<String> tempActionArray3=new ArrayList<String>();
		ArrayList<Double> tempConfArray3=new ArrayList<Double>();
		ArrayList<String> tempNameArray3=new ArrayList<String>();
		Double confSum3=0.0;
		Random ran3=new Random();
		Double wheel=0.0;
		int selected3=0;
		//find min confidence value
		for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
		{
			tempSignals3=RankedWeaver_.weaver.getSignal();
			for(String str:tempSignals3.getKeys()){
				if(actions3_.contains(str)){
					if(maxConfMap3.get(str)<RankedWeaver_.weaver.getConfidence()){
						maxConfMap3.put(str, RankedWeaver_.weaver.getConfidence());
					}
				}
				else{
					maxConfMap3.put(str, RankedWeaver_.weaver.getConfidence());
				}
			}
		}
		
		
		for(String key:maxConfMap3.data.keySet()){
			for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
			{
				tempSignals3=RankedWeaver_.weaver.getSignal();
				if(tempSignals3.contains(key)){
					tempActionArray3.add(tempSignals3.get(key).toString());
					tempConfArray3.add(RankedWeaver_.weaver.getConfidence());
					tempNameArray3.add(RankedWeaver_.weaver.getName());
					confSum3+=RankedWeaver_.weaver.getConfidence();
				}
			}
		}
		for(String str:maxConfMap3.data.keySet()){
			wheel=ran3.nextDouble()*confSum3;
			for(int i=0;i<tempConfArray3.size();i++){
				if(wheel<tempConfArray3.get(i)){
					selected3=i;
					break;
				}
			}
			actions3_.put(str,(String) tempActionArray3.get(selected3));
			actionConfidence3_.put(str, tempConfArray3.get(selected3));
			selectedModels3_.put(str, tempNameArray3.get(selected3));
		}
		
		
		Dictionary<String>	actionsSel_=new Dictionary<String>();
		Dictionary<Double>	actionConfidenceSel_=new Dictionary<Double>();
		Dictionary<String>	selectedModelsSel_=new Dictionary<String>();
		
		Double i=0.0;
		for(String s:actions_.data.keySet()){
			i=ran.nextDouble();
			if(i<th1){
				actionsSel_.put(s,actions_.get(s));
				actionConfidenceSel_.put(s,actionConfidence_.get(s));
				selectedModelsSel_.put(s,selectedModels_.get(s));
			}
			else if(i<th2){
				actionsSel_.put(s,actions2_.get(s));
				actionConfidenceSel_.put(s,actionConfidence2_.get(s));
				selectedModelsSel_.put(s,selectedModels2_.get(s));
			}
			else{
				actionsSel_.put(s,actions3_.get(s));
				actionConfidenceSel_.put(s,actionConfidence3_.get(s));
				selectedModelsSel_.put(s,selectedModels3_.get(s));
			}
		}
		
		
		
		return new Pair<Dictionary<String>, Dictionary<String>>(actionsSel_,selectedModelsSel_);
	}
}
