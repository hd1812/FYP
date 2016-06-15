package physicalInverseModels;

import java.util.ArrayList;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

//Carb count insulin strategy
public class LAInsulinIm2 implements T3Functor<Signals, State, State,Signals>
{
	public Signals Function(final State current, final State target, final Signals sig)
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
			if(timeInDay==60*22){
				u=20.0;
			}
			
		} catch (HammerException e)
		{
		}

		result.put("LAInsulin", u);
		result.put("Aspect","LAInsulin");
		//debugging
		//System.out.println("Sliding scale LA "+u);
		return result;
	}
}

