package forwardModels;

import java.util.ArrayList;
import java.util.Random;

import hammer.common.T2Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

public class Fm implements T2Functor<State, State, Signals>
{
	public State Function(final State current, final Signals sig)
	{
		State pred = State.make();
		ArrayList<Double> rfccArray=new ArrayList<Double>();
		
		//Carb model 1
		Double 	Gpred = 0.0;
		Double 	IIcoef=0.0;
		Double 	GI60coef=0.0;
		Double 	Gcoef=0.0;
		Double 	C=0.0;
		Double  II=0.0;
		Double  G0=0.0;
		String  Aspect=new String();
		Double  Carb=0.0;
		Double 	GI60=0.0;
		
		//Insulin model 1
		Double II60coef=0.0;
		Double GIcoef=0.0;
		Double Insulin=0.0;
		Double II60=0.0;
		Double GI=0.0;
		
		//Psychological intervention
		String Intervention="";
		Double Depression=0.0;
		Double Anxiety=0.0;
		Double EmotionalAdjustment=0.0;
		Random ran=new Random();
		
		//Health Education Impact parameters
		String HEI="";
		Double PositiveAndActiveEngagementInLife=0.0;
		Double HealthDirectedBehavior=0.0;
		Double SkillAndTechniqueAcquisition=0.0;
		Double ConstructiveAttitudesAndApproaches=0.0;
		Double SelfMonitoringAndInsight=0.0;
		Double EmotionalWellbeing=0.0;
		
		//User Engagement parameters
		String UE="";
		Double AestheticAppeal=0.0;
		Double Challenge=0.0;
		Double Endurability=0.0;
		Double Feedback=0.0;
		Double Interactivity=0.0;
		Double Pleasure=0.0;
		Double SensoryAppeal=0.0;
		Double VarietyNovelty=0.0;
		
		//Morning injection and glucose
		//1 unit of insulin = 34.7 ug
		//1 ug insulin = 172.1 pmol
		//u(t) (pmol/kg/min)
		Double unit2ug=34.7;
		Double ug2pmol=172.1;
		Double BW=70.0;
		
		try
		{
			//Find the latest parameter from virtual patients
			II=current.get("II");
			GI=current.get("GI");
			G0=current.get("G");
			rfccArray=current.get("rfcc");
			
			Aspect=(String) sig.get("Aspect");
			if(Aspect!=null){
				System.out.println("Aspect: "+Aspect);
				if(Aspect.equals("Carb")){
					
					//Coefs
					IIcoef=-1.8894;
					GI60coef=0.021811;
					Gcoef=0.41597;
					C=77.223;
					
					Carb=Double.parseDouble((String)sig.get("Carb")) ;
					GI60=rfccArray.get(12)*Carb*1000;	//look at 30 min inteval, index is in mg
					Gpred=II*IIcoef+GI60*GI60coef+Gcoef*G0+C;
					//System.out.println("Gpred==============="+Gpred);
					pred.put("G", Gpred);
				}
				else if(Aspect.equals("Insulin")){
					//Coef settings
					II60coef=-13.755;
					GIcoef=0.014099;
					Gcoef=0.1744;
					C=161.11;
					Insulin=Double.parseDouble((String)sig.get("Insulin"));
					II60=rfccArray.get(12)*Insulin*unit2ug*ug2pmol/BW;
					Gpred=II60*II60coef+GI*GIcoef+Gcoef*G0+C;
					pred.put("G", Gpred);
				}
				else if(Aspect.equals("Psychological")){
					
					Intervention=(String)sig.get("Intervention");
					Depression=current.get("Depression");
					Anxiety=current.get("Anxiety");
					EmotionalAdjustment=current.get("EmotionalAdjustment");

					if(Intervention.equals("GE")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("GD")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("ST")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("B")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("PS")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("C")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("SSup")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("R")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("Bio")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("RP")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("D")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("E")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					else if(Intervention.equals("Mis")){
						pred.put("Depression", Depression+ran.nextFloat());
						pred.put("Anxiety", Anxiety+ran.nextFloat());
						pred.put("EmotionalAdjustment", EmotionalAdjustment+ran.nextFloat());
					}
					
				}
				else if(Aspect.equals("HEI")){
					
					HEI=(String)sig.get("HEI");
					PositiveAndActiveEngagementInLife=current.get("PositiveAndActiveEngagementInLife");
					HealthDirectedBehavior=current.get("HealthDirectedBehavior");
					SkillAndTechniqueAcquisition=current.get("SkillAndTechniqueAcquisition");
					ConstructiveAttitudesAndApproaches=current.get("ConstructiveAttitudesAndApproaches");
					SelfMonitoringAndInsight=current.get("SelfMonitoringAndInsight");
					EmotionalWellbeing=current.get("EmotionalWellbeing");
					pred.put("PositiveAndActiveEngagementInLife", PositiveAndActiveEngagementInLife+ran.nextFloat());
					pred.put("HealthDirectedBehavior", HealthDirectedBehavior+ran.nextFloat());
					pred.put("SkillAndTechniqueAcquisition", SkillAndTechniqueAcquisition+ran.nextFloat());
					pred.put("ConstructiveAttitudesAndApproaches", ConstructiveAttitudesAndApproaches+ran.nextFloat());
					pred.put("SelfMonitoringAndInsight", SelfMonitoringAndInsight+ran.nextFloat());
					pred.put("EmotionalWellbeing", EmotionalWellbeing+ran.nextFloat());
					
				}
				else if(Aspect.equals("UE")){
					
					UE=(String)sig.get(UE);
					AestheticAppeal=current.get("AestheticAppeal");
					Challenge=current.get("Challenge");
					Endurability=current.get("Endurability");
					Feedback=current.get("Feedback");
					Interactivity=current.get("Interactivity");
					Pleasure=current.get("Pleasure");
					SensoryAppeal=current.get("SensoryAppeal");
					VarietyNovelty=current.get("VarietyNovelty");
					pred.put("AestheticAppeal", AestheticAppeal+ran.nextFloat());
					pred.put("Challenge", Challenge+ran.nextFloat());
					pred.put("Endurability", Endurability+ran.nextFloat());
					pred.put("Feedback", Feedback+ran.nextFloat());
					pred.put("Interactivity", Interactivity+ran.nextFloat());
					pred.put("Pleasure", Pleasure+ran.nextFloat());
					pred.put("SensoryAppeal", SensoryAppeal+ran.nextFloat());
					pred.put("VarietyNovelty", VarietyNovelty+ran.nextFloat());
					
				}
			}
			

		} catch (HammerException e)
		{
		}
		
		return pred;
	}
}
