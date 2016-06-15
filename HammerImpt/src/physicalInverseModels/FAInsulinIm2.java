package physicalInverseModels;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

//Sample regimen using insulin NPH and regular insulin
public class FAInsulinIm2 implements T3Functor<Signals, State, State,Signals>
{
	public Signals Function(final State current, final State target,final Signals sig)
	{
		Signals result = Signals.make();
		int time=0;
		int timeInDay=0;
		Double G=0.0;
		Double u=0.0;
		
		try
		{
			time=current.get("Time");
			timeInDay=time%(60*24);
			G=current.get("G");
			
			if(timeInDay==8*60-30){
				if(G>=70&&G<100){
					u=4.0;
				}
				else if(G>=100&&G<150){
					u=5.0;
				}
				else if(G>=150&&G<200){
					u=6.0;
				}
				else if(G>=200&&G<250){
					u=7.0;
				}
				else if(G>=250&&G<300){
					u=8.0;
				}
				else if(G>=300){
					u=9.0;
				}
			}
			else if(timeInDay==12*60-30){
				if(G>=250&&G<300){
					u=1.0;
				}
				else if(G>=300){
					u=2.0;
				}
			}
			else if(timeInDay==18*60-30){
				if(G>=70&&G<100){
					u=4.0;
				}
				else if(G>=100&&G<150){
					u=5.0;
				}
				else if(G>=150&&G<200){
					u=6.0;
				}
				else if(G>=200&&G<250){
					u=7.0;
				}
				else if(G>=250&&G<300){
					u=8.0;
				}
				else if(G>=300){
					u=9.0;
				}
			}
			else if(timeInDay==22*60){
				if(G>=250&&G<300){
					u=1.0;
				}
				else if(G>=300){
					u=2.0;
				}	
			}
			
		} catch (HammerException e)
		{
		}
		//System.out.println("++++++++++++++++++++++++"+current.contains("FAInsulinTrain"));
		result.put("FAInsulin", u.toString());
		result.put("Aspect","FAInsulin");
		//System.out.println("Sliding scale "+u);
		return result;
	}
}

