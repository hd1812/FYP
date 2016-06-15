package physicalInverseModels;

import java.util.ArrayList;

import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

//NPH dose
public class LAInsulinIm1 implements T3Functor<Signals, State, State,Signals>
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
			if(timeInDay==8*60-30){
				u=12.0;
			}
			else if(timeInDay==18*60-30){
				u=6.0;
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

