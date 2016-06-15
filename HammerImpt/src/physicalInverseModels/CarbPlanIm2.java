package physicalInverseModels;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

//moderate low carb ingestion 30-130 g per day, 10-43 per meal
public class CarbPlanIm2 implements T3Functor<Signals, State, State,Signals>
{
	public Signals Function(final State current, final State target,final Signals sig)
	{
		Signals result = Signals.make();
		int time=0;
		Double G=0.0;
		Double hunger=0.0;
		Double toEat=0.0;
		Integer timeInDay;
		try
		{
			time=current.get("Time");
			hunger=current.get("Hunger");
			
			timeInDay=time%(24*60);
			
			toEat=10+hunger*(43-10);	
			
			result.put("Carb", toEat.toString());
			result.put("Aspect","Carb");
		} catch (HammerException e)
		{
		}
		//System.out.println("CarbPlanIm2 To eat " + result.get("ToEat"));
		return result;
	}
}

