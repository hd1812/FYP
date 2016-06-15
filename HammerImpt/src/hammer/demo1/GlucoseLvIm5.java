package hammer.demo1;

import hammer.common.T2Functor;
import hammer.exception.HammerException;
import hammer.signals.Signals;
import hammer.state.State;

public class GlucoseLvIm5 implements T2Functor<Signals, State, State>
{
	public Signals Function(final State current, final State target)
	{
		Double currentlv=0.0;
		Double targetlv=0.0;
		Double diff=0.0;
		String action="No action required";
		try
		{
			currentlv=Double.parseDouble(current.get("GlucoseLv").toString());
			targetlv=Double.parseDouble(target.get("GlucoseLv").toString());
		} catch (HammerException e)
		{
		}
		diff=currentlv-targetlv;
		//System.out.println(diff);
		if(diff>0.5){
			action="Exercise";
		}
		else if(diff<-0.5&&diff>-2){
			action="Take pills";
		}
		else if(diff<-2){
			action="Insulin injection";
		}
		
		Signals result = Signals.make();
		result.put("GlucoseLv", action);
		return result;
	}
}

