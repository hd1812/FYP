/*package hammer.demo1;

import java.util.ArrayList;

import hammer.Hammer;
import hammer.exception.HammerException;
import hammer.forwardmodel.ForwardModel;
import hammer.forwardmodel.SimpleForwardModel;
import hammer.inversemodel.InverseModel;
import hammer.inversemodel.SimpleInverseModel;
import hammer.state.State;

public class Demo1
{

	public static void main(String[] args)
	{
		Hammer HammerDemo = new Hammer();
		State target_=new State();
		
		//Add subscriptions for models. Subscriptions vary for different models.
		ArrayList<String> subscriptions = new ArrayList<String>();
		subscriptions.add("GlucoseLv");
		
		//Specify target state
		target_.put("GlucoseLv", 8);
		
		//Specify arrays of models to pair up
		ArrayList<InverseModel> imArray=new ArrayList<InverseModel>();
		ArrayList<ForwardModel> fmArray=new ArrayList<ForwardModel>();
		
		// Dependency definition
		ArrayList<String> im2dpd = new ArrayList<String>();
		ArrayList<String> im3dpd = new ArrayList<String>();
		ArrayList<String> im4dpd = new ArrayList<String>();
		ArrayList<String> im5dpd = new ArrayList<String>();
				
		//Inverse and forward model declarations
		try
		{
			SimpleInverseModel im1model = new SimpleInverseModel("Im1", subscriptions, new ArrayList<String>(), new GlucoseLvIm1());
			SimpleInverseModel im2model = new SimpleInverseModel("Im2", subscriptions, new ArrayList<String>(), new GlucoseLvIm2());
			SimpleInverseModel im3model = new SimpleInverseModel("Im3", subscriptions, new ArrayList<String>(), new GlucoseLvIm3());
			SimpleInverseModel im4model = new SimpleInverseModel("Im4", subscriptions, new ArrayList<String>(), new GlucoseLvIm4());
			SimpleInverseModel im5model = new SimpleInverseModel("Im5", subscriptions, new ArrayList<String>(), new GlucoseLvIm5());
			SimpleForwardModel fm1model = new SimpleForwardModel("FM1", subscriptions, new ArrayList<String>(), new GlucoseLvFm1());
			SimpleForwardModel fm2model = new SimpleForwardModel("FM2", subscriptions, new ArrayList<String>(), new GlucoseLvFm1());
			SimpleForwardModel fm3model = new SimpleForwardModel("FM3", subscriptions, new ArrayList<String>(), new GlucoseLvFm1());
			imArray.add(im1model);
			imArray.add(im2model);
			imArray.add(im3model);
			imArray.add(im4model);
			imArray.add(im5model);
			fmArray.add(fm1model);
			fmArray.add(fm2model);
			fmArray.add(fm3model);
			HammerDemo.registerModelPair(imArray, fmArray,HammerDemo.getCore());
		} catch (HammerException e1)
		{
		}
		
		HammerDemo.setTargetState(target_);  						//Set target state
		HammerDemo.addSubscription("GlucoseLv");					//Add subscriptions for Hammer
		HammerDemo.stateInitialisation();							//Initialise state and assign confidence function
		HammerDemo.setConfidenceFunction(new GlucoseLvConfidenceFunctor());
		HammerDemo.setSelector(new GlucoseLvConfSelector());
		//HammerDemo.start();		not in use									//Run Hammer
		HammerDemo.printConfidenceMap();							//Print confidence map
	}

}
*/