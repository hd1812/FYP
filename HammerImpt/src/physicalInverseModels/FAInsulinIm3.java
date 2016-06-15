package physicalInverseModels;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

//Carb count insulin strategy
public class FAInsulinIm3 implements T3Functor<Signals, State, State,Signals>
{
	public Signals Function(final State current, final State target,final Signals sig)
	{
		Signals result = Signals.make();
		int time=0;
		Double CarbCount=0.0;
		Double toEat=0.0;
		Double u=0.0;
		Double C2IRatio=10.0;
		Integer timeInDay;
		result.put("Aspect","Insulin");
		try
		{
			time=current.get("Time");
			timeInDay=time%(60*24);
			if(sig.contains("Carb")){
				toEat=Double.parseDouble(sig.get("Carb").toString());
			}
			if(timeInDay==8*60-30||timeInDay==12*60-30||timeInDay==18*60-30){
				u=Math.ceil(CarbCount/C2IRatio);
			}
			//System.out.println("toeat "+CarbCount);
			//System.out.println("Carb Counting "+u);
		} catch (HammerException e)
		{
		}
		result.put("FAInsulin",u.toString());
		return result;
	}
}

