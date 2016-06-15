package hammer.demoPatient;

import java.util.ArrayList;
import java.util.TreeMap;

import hammer.common.Pair;
import hammer.common.T3Functor;
import hammer.core.Core.RankedWeaver;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

public class ClosestSelector2 implements T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State>
{
	public Pair<Dictionary<String>,Dictionary<String>> Function(TreeMap<String,RankedWeaver>sortedWeavers_,State current,State target){
		Dictionary<String>	actions_=new Dictionary<String>();
		Dictionary<String>	selectedModels_=new Dictionary<String>();
		ArrayList<String>	CommandArray=new ArrayList<String>();
		Dictionary<Double>	ConfArray=new Dictionary<Double>();
		Dictionary<Double>	QualityArray=new Dictionary<Double>();
		Dictionary<Double>	WeightArray=new Dictionary<Double>();
		Dictionary<String>	ActionArray=new Dictionary<String>();
		ArrayList<String>	NameArray=new ArrayList<String>();
		
		Double M=100.0;
		Double MaxConf=0.0;
		Double MaxQuality=0.0;
		Double MinConf=-1.0;
		Double MinQuality=-1.0;
		Double Quality=0.0;
		Double Weight=0.0;
		Double MaxWeight=0.0;
		
		Double ConfWeight=1.0;
		Double QualityWeight=0.0;
		
		String SelName="";
		
		Signals tempSignals=new Signals();
		State pred=new State();
		
		//Find all aspects
		for(RankedWeaver RankedWeaver_ : sortedWeavers_.values()){
			tempSignals=RankedWeaver_.weaver.getSignal();
			for(String ActionCommand:tempSignals.getKeys()){
				if(!CommandArray.contains(ActionCommand)&&!ActionCommand.equals("Aspect")){
					CommandArray.add(ActionCommand);
				}
			}
		}
		//System.out.println(AspectArray);
		//For each aspect, loop through weavers and look at conf and quality
		for(String command:CommandArray){
			
			for (RankedWeaver RankedWeaver_ : sortedWeavers_.values())
			{
				pred=RankedWeaver_.weaver.getPredState();
				if(RankedWeaver_.weaver.getSignal().contains(command)&&pred.contains("G")){
					try
					{
						ConfArray.put(RankedWeaver_.weaver.getName(), RankedWeaver_.weaver.getConfidence());
						Quality=Math.max(M-Math.abs(Math.abs((Double)pred.get("G")-(Double)target.get("G"))), 0);
						QualityArray.put(RankedWeaver_.weaver.getName(), Quality);
						ActionArray.put(RankedWeaver_.weaver.getName(), RankedWeaver_.weaver.getSignal().get(command).toString());
						
						MaxConf=Math.max(MaxConf, RankedWeaver_.weaver.getConfidence());
						MaxQuality=Math.max(MaxQuality, Quality);
						if(MinConf==-1){
							MinConf=RankedWeaver_.weaver.getConfidence();
						}
						else if(RankedWeaver_.weaver.getConfidence()<MinConf){
							MinConf=RankedWeaver_.weaver.getConfidence();
						}
						if(MinQuality==-1){
							MinQuality=Quality;
						}
						else if(Quality<MinQuality){
							MinQuality=Quality;
						}
						
						NameArray.add(RankedWeaver_.weaver.getName());
						
					} catch (HammerException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			//Find the weighted index and Quality values.
			for(String w:NameArray){
				Weight=ConfWeight*(ConfArray.get(w)-MinConf)/MaxConf+QualityWeight*(QualityArray.get(w)-MinQuality)/MaxQuality;
				WeightArray.put(w, Weight);
				if(Weight>MaxWeight){
					MaxWeight=Weight;
					SelName=w;
				}
				//System.out.println(w);
				//System.out.println((ConfArray.get(w)-MinConf)/MaxConf+" "+(QualityArray.get(w)-MinQuality)/MaxQuality+" "+Weight);
			}
			
			if(ActionArray.get(SelName)!=null){
				actions_.put(command, ActionArray.get(SelName));
				selectedModels_.put(command, SelName);
			}
			
			//clear arrays
			ConfArray.empty();
			QualityArray.empty();
			ActionArray.empty();
			WeightArray.empty();
			NameArray.clear();
			MaxConf=0.0;
			MaxQuality=0.0;
			MaxWeight=0.0;
			SelName="";
		}

		return new Pair<Dictionary<String>, Dictionary<String>>(actions_,selectedModels_);
	}
}
