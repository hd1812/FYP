/*package hammer.tutorial1;

import java.util.ArrayList;

import hammer.Hammer;
import hammer.demo1.GlucoseLvConfidenceFunctor;
import hammer.exception.HammerException;
import hammer.forwardmodel.SimpleForwardModel;
import hammer.inversemodel.SimpleInverseModel;

//This class declares models for tutorial1 and runs the Hammer architecture.
public class Tutorial1Main
{
	
	public static void main(String[] args)
	{
		Hammer HammerDemo = new Hammer();
		
		//Add subscriptions for models. Subscriptions vary for different models.
		ArrayList<String> subscriptions = new ArrayList<String>();
		subscriptions.add("distance");
		
		// Dependency definition
		ArrayList<String> im2dpd = new ArrayList<String>();
		ArrayList<String> im3dpd = new ArrayList<String>();
		ArrayList<String> im4dpd = new ArrayList<String>();
		ArrayList<String> im5dpd = new ArrayList<String>();
		ArrayList<String> im6dpd = new ArrayList<String>();
		ArrayList<String> im7dpd = new ArrayList<String>();
		im5dpd.add("Slow");im5dpd.add("Normal");im5dpd.add("Fast");
		im6dpd.add("Coordinates");
		im7dpd.add("Speed");im7dpd.add("Position");
				
		//Inverse and forward model declarations
		try
		{
			SimpleInverseModel im1model = new SimpleInverseModel("Slow", subscriptions, new ArrayList<String>(), new InverseModel1Functor());
			SimpleInverseModel im2model = new SimpleInverseModel("Normal", subscriptions, im2dpd, new InverseModel2Functor());
			SimpleInverseModel im3model = new SimpleInverseModel("Fast", subscriptions, im3dpd, new InverseModel3Functor());
			SimpleInverseModel im4model = new SimpleInverseModel("Coordinates", subscriptions, im4dpd, new InverseModel3Functor());
			SimpleInverseModel im5model = new SimpleInverseModel("Speed", subscriptions, im5dpd, new InverseModel1Functor());
			SimpleInverseModel im6model = new SimpleInverseModel("Position", subscriptions, im6dpd, new InverseModel3Functor());
			SimpleInverseModel im7model = new SimpleInverseModel("TargetReached", subscriptions, im7dpd, new InverseModel1Functor());
			SimpleForwardModel fm1model = new SimpleForwardModel("FM1", subscriptions, null, new ForwardModel1Functor());
			/*
			HammerDemo.registerModelPair(im1model, fm1model,HammerDemo.getCore());
			HammerDemo.registerModelPair(im2model, fm1model,HammerDemo.getCore());
			HammerDemo.registerModelPair(im3model, fm1model,HammerDemo.getCore());
			HammerDemo.registerModelPair(im4model, fm1model,HammerDemo.getCore());
			HammerDemo.registerModelPair(im5model, fm1model,HammerDemo.getCore());
			HammerDemo.registerModelPair(im6model, fm1model,HammerDemo.getCore());
			HammerDemo.registerModelPair(im7model, fm1model,HammerDemo.getCore());
			
		} catch (HammerException e1)
		{
		}

		HammerDemo.addSubscription("distance");					//Add subscriptions for Hammer
		HammerDemo.addSubscription("robotPosition");
		HammerDemo.stateInitialisation();//Initialise state and assign confidence function
		HammerDemo.setConfidenceFunction(new GlucoseLvConfidenceFunctor());
		//HammerDemo.start();	not in use now									//Run Hammer
		HammerDemo.printConfidenceMap();						//Print confidence map
	}

}
*/