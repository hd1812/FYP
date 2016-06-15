package physicalInverseModels;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

//normal carb ingestion 45-60 a meal
public class CarbPlanIm1 implements T3Functor<Signals, State, State,Signals>
{
	public Signals Function(final State current, final State target,final Signals sig)
	{
		Signals result = Signals.make();
		int time=0;
		Integer timeInDay;
		Double G=0.0;
		Double hunger=0.0;
		Double toEat=0.0;
		try
		{
			time=current.get("Time");
			hunger=current.get("Hunger");
			timeInDay=time%(24*60);
			toEat=45+hunger*15;			
			
			result.put("Carb", toEat.toString());
			result.put("Aspect","Carb");
		} catch (HammerException e)
		{
		}
		//debugging
		//System.out.println("CarbPlanIm1 To eat " + result.get("ToEat"));
		return result;
	}
}

