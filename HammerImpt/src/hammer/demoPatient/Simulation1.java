package hammer.demoPatient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import forwardModels.Fm;
import forwardModels.GPFM;
import hammer.Hammer;
import hammer.demo1.GlucoseLvConfSelector;
import hammer.demo1.GlucoseLvConfidenceFunctor;
import hammer.demo1.GlucoseLvFm1;
import hammer.demo1.GlucoseLvIm1;
import hammer.demo1.GlucoseLvIm2;
import hammer.demo1.GlucoseLvIm3;
import hammer.demo1.GlucoseLvIm4;
import hammer.demo1.GlucoseLvIm5;
import hammer.exception.HammerException;
import hammer.forwardmodel.ForwardModel;
import hammer.forwardmodel.SimpleForwardModel;
import hammer.inversemodel.InverseModel;
import hammer.inversemodel.SimpleInverseModel;
import hammer.state.State;
import hammer.world.World;
import healthEduInverseModels.*;
import patientSimulation.patientSimu;
import physicalInverseModels.*;
import psychologicalInverseModels.*;
import userEngagementInverseModels.*;

public class Simulation1
{
	public static void main(String[] args)
	{
		//========================================Hammer Settings==================================================================================================
		patientSimu newPatient=new patientSimu();
		World myWorld=new World(newPatient);
		Hammer HammerDemo = new Hammer(myWorld);
		State target_=new State();	
		ArrayList<Integer> HammerRunTime=new ArrayList<Integer>();
		
		//Set hammer running time
		HammerRunTime.add(8*60-30);
		HammerRunTime.add(12*60-30);
		HammerRunTime.add(18*60-30);
		HammerRunTime.add(22*60);
		
		newPatient.setRecoTime(HammerRunTime);
		
		//Add subscriptions for models. Subscriptions vary for different models.
		ArrayList<String> subs = new ArrayList<String>();
		subs.add("G");
		subs.add("GI");
		subs.add("II");
		subs.add("ToEat");
		subs.add("Time");
		subs.add("Hunger");
		subs.add("rfcc");
		
		subs.add("Depression");
		subs.add("Anxiety");
		subs.add("EmotionalAdjustment");
		

		subs.add("PositiveAndActiveEngagementInLife");
		subs.add("HealthDirectedBehavior");
		subs.add("SkillAndTechniqueAcquisition");
		subs.add("ConstructiveAttitudesAndApproaches");
		subs.add("SelfMonitoringAndInsight");
		subs.add("EmotionalWellbeing");
		
		subs.add("AestheticAppeal");
		subs.add("Challenge");
		subs.add("Endurability");
		subs.add("Feedback");
		subs.add("Interactivity");
		subs.add("Pleasure");
		subs.add("SensoryAppeal");
		subs.add("VarietyNovelty");
		
		subs.add("RecoTime");
		subs.add("GTrain");
		subs.add("GOb");
		subs.add("LAInsulinTrain");
		subs.add("FAInsulinTrain");
		subs.add("CarbTrain");
		subs.add("CurrentOb");
		subs.add("Mode");
		
		//Specify target state
		target_.put("G", 112.5);
		
		//Specify arrays of models to pair up
		ArrayList<InverseModel> imArray=new ArrayList<InverseModel>();
		ArrayList<ForwardModel> fmArray=new ArrayList<ForwardModel>();
		
		// Dependency definition
		ArrayList<String> ccdpd1 = new ArrayList<String>();
		ArrayList<String> ccdpd2 = new ArrayList<String>();
		ArrayList<String> ccdpd3 = new ArrayList<String>();
		ArrayList<String> ccdpd4 = new ArrayList<String>();
		ccdpd1.add("NormalCarbIm");
		ccdpd2.add("LowCarbIm");
		ccdpd3.add("VeryLowCarbIm");
		ccdpd4.add("HighCarbIm");
		
		//type array
		ArrayList<String> LAInsulinArray = new ArrayList<String>();
		ArrayList<String> FAInsulinArray = new ArrayList<String>();
		ArrayList<String> CarbArray = new ArrayList<String>();
		ArrayList<String> CCArray=new ArrayList<String>();
		
		//Inverse and forward model declarations
		try
		{
			SimpleInverseModel CarbIm1 = new SimpleInverseModel("NormalCarbIm", subs, new ArrayList<String>(), new CarbPlanIm1());
			SimpleInverseModel CarbIm2 = new SimpleInverseModel("LowCarbIm", subs, new ArrayList<String>(), new CarbPlanIm2());
			SimpleInverseModel CarbIm3 = new SimpleInverseModel("VeryLowCarbIm", subs, new ArrayList<String>(), new CarbPlanIm3());
			SimpleInverseModel CarbIm4 = new SimpleInverseModel("HighCarbIm", subs, new ArrayList<String>(), new CarbPlanIm4());
			SimpleInverseModel FAInsulinIm1 = new SimpleInverseModel("FAInsulinIm1", subs, new ArrayList<String>(), new FAInsulinIm1());
			SimpleInverseModel FAInsulinIm2 = new SimpleInverseModel("FAInsulinIm2", subs, new ArrayList<String>(), new FAInsulinIm2());			
			SimpleInverseModel LAInsulinIm1 = new SimpleInverseModel("LAInsulinIm1", subs, new ArrayList<String>(), new LAInsulinIm1());
			SimpleInverseModel LAInsulinIm2 = new SimpleInverseModel("LAInsulinIm2", subs, new ArrayList<String>(), new LAInsulinIm2());
			
			//implementing carb counting insulin strategies separately
			/*
			SimpleInverseModel NormalCarbIm_FAInsulinIm3 = new SimpleInverseModel("NormalCarbIm+FAInsulinIm3", subs, ccdpd1, new FAInsulinIm3());
			SimpleInverseModel LowCarbIm_FAInsulinIm3 = new SimpleInverseModel("LowCarbIm+FAInsulinIm3", subs, ccdpd2, new FAInsulinIm3());
			SimpleInverseModel VeryLowCarbIm_FAInsulinIm3 = new SimpleInverseModel("VeryLowCarbIm+FAInsulinIm3", subs, ccdpd3, new FAInsulinIm3());
			SimpleInverseModel HighCarbIm_FAInsulinIm3 = new SimpleInverseModel("HighCarbIm+FAInsulinIm3", subs, ccdpd4, new FAInsulinIm3());
			*/
			
			CarbArray.add("HighCarbIm");
			CarbArray.add("NormalCarbIm");
			CarbArray.add("LowCarbIm");
			CarbArray.add("VeryLowCarbIm");

			LAInsulinArray.add("LAInsulinIm1");
			LAInsulinArray.add("LAInsulinIm2");
			FAInsulinArray.add("FAInsulinIm1");
			FAInsulinArray.add("FAInsulinIm2");
			
			SimpleForwardModel GPFm1 = new SimpleForwardModel("GPFm1", subs, new ArrayList<String>(), new GPFM());
			
			/*
			//Psychological
			SimpleInverseModel BehaviorTherapyIm = new SimpleInverseModel("BehaviorTherapyIm", subs, new ArrayList<String>(), new BehaviorTherapy());
			SimpleInverseModel BiofeedbackIm = new SimpleInverseModel("BiofeedbackIm", subs, new ArrayList<String>(), new Biofeedback());
			SimpleInverseModel CognitiveTheropyIm = new SimpleInverseModel("CognitiveTheropyIm", subs, new ArrayList<String>(), new CognitiveTheropy());
			SimpleInverseModel DietIm = new SimpleInverseModel("DietIm", subs, new ArrayList<String>(), new Diet());
			SimpleInverseModel ExerciseIm = new SimpleInverseModel("ExerciseIm", subs, new ArrayList<String>(), new Exercise());
			SimpleInverseModel GeneralDiscussionIm = new SimpleInverseModel("GeneralDiscussionIm", subs, new ArrayList<String>(), new GeneralDiscussion());
			SimpleInverseModel GeneralEducationIm = new SimpleInverseModel("GeneralEducationIm", subs, new ArrayList<String>(), new GeneralEducation());
			SimpleInverseModel MiscellaneousIm = new SimpleInverseModel("MiscellaneousIm", subs, new ArrayList<String>(), new Miscellaneous());
			SimpleInverseModel ProblemSolvingIm = new SimpleInverseModel("ProblemSolvingIm", subs, new ArrayList<String>(), new ProblemSolving());
			SimpleInverseModel RelaspsePreventionIm = new SimpleInverseModel("RelaspsePreventionIm", subs, new ArrayList<String>(), new RelaspsePrevention());
			SimpleInverseModel RelaxationIm = new SimpleInverseModel("RelaxationIm", subs, new ArrayList<String>(), new Relaxation());
			SimpleInverseModel SkillsTrainingIm = new SimpleInverseModel("SkillsTrainingIm", subs, new ArrayList<String>(), new SkillsTraining());
			SimpleInverseModel SocialSupportIm = new SimpleInverseModel("SocialSupportIm", subs, new ArrayList<String>(), new SocialSupport());
			
			//Health Education Impact
			SimpleInverseModel GamificationIm = new SimpleInverseModel("GamificationIm", subs, new ArrayList<String>(), new Gamification());
			SimpleInverseModel QuizIm = new SimpleInverseModel("QuizIm", subs, new ArrayList<String>(), new Quiz());
			SimpleInverseModel TargetedAdIm = new SimpleInverseModel("TargetedAdIm", subs, new ArrayList<String>(), new TargetedAd());
			
			//User Engagement
			SimpleInverseModel EmotionalThreadIm = new SimpleInverseModel("EmotionalThreadIm", subs, new ArrayList<String>(), new EmotionalThread());
			SimpleInverseModel SensualThreadIm = new SimpleInverseModel("SensualThreadIm", subs, new ArrayList<String>(), new SensualThread());
			SimpleInverseModel SpatiotemporalThreadIm = new SimpleInverseModel("SpatiotemporalThreadIm", subs, new ArrayList<String>(), new SpatiotemporalThread());
			*/

			imArray.add(CarbIm1);
			imArray.add(CarbIm2);
			imArray.add(CarbIm3);
			imArray.add(CarbIm4);
			imArray.add(LAInsulinIm1);
			imArray.add(LAInsulinIm2);
			imArray.add(FAInsulinIm1);			
			imArray.add(FAInsulinIm2);
			
			
			makeHigherLevelIms(CarbArray,LAInsulinArray,FAInsulinArray,imArray,subs);
			fmArray.add(GPFm1);
			
			//Since 
			/*
			imArray.add(NormalCarbIm_FAInsulinIm3);
			imArray.add(LowCarbIm_FAInsulinIm3);
			imArray.add(VeryLowCarbIm_FAInsulinIm3);
			imArray.add(HighCarbIm_FAInsulinIm3);
			
			CCArray.add("NormalCarbIm+FAInsulinIm3");
			CCArray.add("LowCarbIm+FAInsulinIm3");
			CCArray.add("VeryLowCarbIm+FAInsulinIm3");
			CCArray.add("HighCarbIm+FAInsulinIm3");
			makeHigherLevelIms(CCArray,LAInsulinArray,imArray,subs);
			*/
			
			HammerDemo.registerModelPair(imArray, fmArray,HammerDemo.getCore());
		} catch (HammerException e1)
		{
		}
		
		HammerDemo.setTargetState(target_);  						//Set target state
		HammerDemo.addSubscription(subs);							//Add subscriptions for Hammer
		HammerDemo.stateInitialisation();							//Initialise state and assign confidence function
		HammerDemo.setConfidenceFunction(new ConfidenceFunctor());
		HammerDemo.setSelector(new ParetoSelector());
		//==========================================================================================================================================
		//Clear Log Files
		File file = new File("/home/hao/Data/Temp/actions.dat");
		file.delete();
		//===========================================Interface to run hammer and virtual patient===============================================================================================
		int interval=1;//Run hammer every certain min
		int i=0;
		Integer timeInDay=0;
		String command="t";
		String resetCommand="0";
		Scanner user_input=new Scanner(System.in);
		Boolean runHammer=false;
		//myCore.addState(newState, target_);
		HammerDemo.recordHammer(HammerDemo.findConfidenceName());
		System.out.println("Please enter command:");
		System.out.println("Enter 't' to train Hammer; Enter 'r' to run Hammer");
		command=user_input.next();
		System.out.println("Please enter the length of the simulation in minutes:");
		i=Math.max(user_input.nextInt()/interval, 1);	
		
		do
		{
			//run hammer
			timeInDay=myWorld.time%(24*60);
			if(HammerRunTime.contains(timeInDay)&&runHammer){
				HammerDemo.HammerNext();
			}
			
			if(command.equals("r")){
				runHammer=true;
				System.out.println("Start training Hammer. Remaining iteration: "+i);
				//wait for hammer to complete recommendation before running virtual patient
				if(HammerRunTime.contains(timeInDay)){
					try {
					    Thread.sleep(100);//wait for 1 sec
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
				}
				
				//debugging
				//System.out.println("World is running. Remaining iteration: "+i);
				HammerDemo.runHammer(interval);
			}
			else if(command.equals("t")){
				runHammer=true;
				System.out.println("Start training Hammer. Remaining iteration: "+i);
				HammerDemo.trainHammer(interval);
			}
			else if(command.equals("s")){
				runHammer=true;
				System.out.println("Start training Hammer. Remaining iteration: "+i);
				HammerDemo.runSample(interval);
			}
			else if(command.equals("ran")){
				runHammer=false;
				System.out.println("Start World random simulation. Remaining iteration: "+i);
				HammerDemo.runRandom(interval);
			}
			else{
				runHammer=false;
				//run the world without inputs from inverse models
				System.out.println("Just let the world go. Remaining iteration: "+i);
				HammerDemo.runNoAction(interval);
			}
			
			i--;
			
			if(i==0){
				HammerDemo.printConfidenceMap();
				System.out.println("Please enter command:");
				System.out.println("Enter 't' to train Hammer; Enter 'r' to run Hammer");
				command=user_input.next();
				System.out.println("Please enter the length of the simulation in minutes:");
				i=Math.max(user_input.nextInt()/interval, 0);
				//System.out.println("reset?");
				//resetCommand=user_input.next();
				//if(resetCommand.equals("1")){
				//	HammerDemo.myWorld.patientSimu1.resetPatient();;
				//}
			}
		} while (i!=0);
		
		HammerDemo.endWorld();
		HammerDemo.endHammer();
		HammerDemo.printConfidenceMap();							//Print confidence map
	}
	
	public static void makeHigherLevelIms(ArrayList<String> CarbArray,ArrayList<String> LAInsulinArray,ArrayList<String> FAInsulinArray,ArrayList<InverseModel> ImArray,ArrayList<String> subs){
		ArrayList<String>dpd=new ArrayList<String>();
		for(int i=0;i<CarbArray.size();i++){
			for(int j=0;j<LAInsulinArray.size();j++){
				for(int k=0;k<FAInsulinArray.size();k++){
					dpd=new ArrayList<String>();
					dpd.add(CarbArray.get(i));
					dpd.add(LAInsulinArray.get(j));
					dpd.add(FAInsulinArray.get(k));
					try
					{
						System.out.println(dpd);
						ImArray.add(new SimpleInverseModel(CarbArray.get(i)+"+"+LAInsulinArray.get(j)+"+"+FAInsulinArray.get(k), subs, dpd, null));
					} catch (HammerException e)
					{
					}
				}
			}
		}
	}
	
	public static void makeHigherLevelIms(ArrayList<String> a1,ArrayList<String> a2,ArrayList<InverseModel> ImArray,ArrayList<String> subs){
		ArrayList<String>dpd=new ArrayList<String>();
		for(int i=0;i<a1.size();i++){
			for(int j=0;j<a2.size();j++){
				
				dpd=new ArrayList<String>();
				dpd.add(a1.get(i));
				dpd.add(a2.get(j));
				
				try
				{
					System.out.println(dpd);
					ImArray.add(new SimpleInverseModel(a1.get(i)+"+"+a2.get(j), subs, dpd, null));
				} catch (HammerException e)
				{
				}
				
			}
		}
	}
	
}
