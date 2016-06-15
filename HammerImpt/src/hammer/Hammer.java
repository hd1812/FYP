package hammer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import com.rits.cloning.Cloner;

import hammer.common.Pair;
import hammer.common.T1Functor;
import hammer.common.T2Functor;
import hammer.common.T3Functor;
import hammer.confidenceEvaluation.StateMap;
import hammer.confidencemap.ConfidenceMap;
import hammer.core.Core;
import hammer.core.Core.RankedWeaver;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;
import hammer.forwardmodel.ForwardModel;
import hammer.inversemodel.InverseModel;
import hammer.state.State;
import hammer.world.World;

//This class defines the main body of HAMMER and includes functions to set up and run Hammer.
public class Hammer
{
	private Core 	myCore = Core.make();
	private Cloner	cloner	= new Cloner();
	private ArrayList<String> subscriptions = new ArrayList<String>();
	private State newState = State.make();
	private State prevState = State.make();
	private State target_;
	public BufferedWriter out;
	public ConfidenceMap confidenceMap_;
	
	//The world needs to change
	public World	myWorld;
	
	public Hammer(){}
	public Hammer(World newWorld){
		myWorld=newWorld;
	}
	
	//Register a pair of models to hammer core
	public void registerModelPair(InverseModel inverseModel, ForwardModel forwardModel,Core core){
		try
		{
			myCore.registerInverseForwardPair(inverseModel, forwardModel,core,myCore.pimpl_.sortedWeavers_);
		} catch (HammerException e)
		{
		}
	}
	public void registerModelPair(ArrayList<InverseModel> imArray, ArrayList<ForwardModel> fmArray,Core core){
		myCore.registerInverseForwardPair(imArray, fmArray,core,myCore.pimpl_.sortedWeavers_);
	}

	//Update hammer core subscriptions
	public void addSubscription(String sub){
		subscriptions.add(sub);
	}
	public void addSubscription(ArrayList<String> sub){
		for(int i=0;i<sub.size();i++){
			subscriptions.add(sub.get(i));
		}
	}
	public void removeSubscription(String sub){
		subscriptions.remove(sub);
	}
	public void clearSubscriptions(){
		subscriptions.clear();
	}
	
	//Initialise and update state information
	public void stateInitialisation(){
		newState.setTimestamp(0);
		myWorld.init(subscriptions);
		myCore.addState(newState, target_);
		try
		{
			out = new BufferedWriter(new FileWriter("/home/hao/Data/Temp/HammerHistory.dat"));
		} catch (IOException e)
		{
		}
	}
	public void stateUpdate(){
		recordHammer(findConfidenceValue());
		for(String str: myWorld.state_.pimpl_.aspects_){
			try
			{
				newState.put(str, myWorld.state_.get(str));
			} catch (HammerException e)
			{
			}
		}
	}
	
	public void setTargetState(State target){
		target_=target;
	}
	
	//Start running Hammer
	/*
	public void start(){
		int i=0;
		String command="t";
		Scanner user_input=new Scanner(System.in);
		//myCore.addState(newState, target_);
		
		System.out.println("Please enter command:");
		System.out.println("Enter 't' to train Hammer; Enter 'r' to run Hammer");
		command=user_input.next();
		System.out.println("Please enter the number of iterations:");
		i=user_input.nextInt();	
		
		do
		{
			if(command.equals("r")){
				System.out.println("Hammer is running. Remaining iteration: "+i);
				myWorld.next(myCore.pimpl_.actions_);
			}
			else if(command.equals("t")){
				System.out.println("Start training Hammer. Remaining iteration: "+i);
				myWorld.train();
			}
			else{
				//run the world without inputs from inverse models
				System.out.println("Just let it go. Remaining iteration: "+i);
				myWorld.train();
			}
			
			
			//Deep clone current state
			prevState = cloner.deepClone(newState);
			//myWorld.updatePublications(prevState);
			
			// A shallow copy of prevState with timestamp incremented
			newState = State.next(prevState);
			
			// update the aspects in new state from the real world
			stateUpdate();
			
			myCore.addState(newState, target_);
			
			//Update action suggestions from Hammer core
			System.out.println("Suggested actions: "+myCore.pimpl_.actions_.data+"selected by model: "+myCore.pimpl_.selectedModels_.data);
			System.out.println("===============================================================================================");
			
			i--;
			
			if(i==0){
				System.out.println("Please enter command:");
				System.out.println("Enter 't' to train Hammer; Enter 'r' to run Hammer");
				command=user_input.next();
				System.out.println("Please enter the number of iterations:");
				i=user_input.nextInt();	
			}
		} while (i!=0);
		
		myWorld.worldEnd();
		/*
		try
		{
			myCore.ThreadWait();
		} catch (InterruptedException e)
		{
		}
		
	}
	*/
	
	//Print confidence map
	public void printConfidenceMap(){
		String report = new String();
		try
		{
			ConfidenceMap result = myCore.getConfidenceMap();
			System.out.println("~Confidences~");
			for (Entry<String, Double> entry : result.pimpl_.dictionary_.data.entrySet())
			{
				report = report.concat(entry.getKey() + "\t" + entry.getValue() + "\n");
				System.out.println(entry.getKey() + "\t" + entry.getValue());
			}
			
		} catch (InterruptedException e)
		{
		}
	}
	
	public Core getCore(){
		return myCore;
	}
	
	public void setConfidenceFunction(final T2Functor<Double, StateMap,String> confidenceFunctor)
	{
		myCore.setConfidenceFunction(confidenceFunctor);
	}
	
	public void setSelector(final T3Functor<Pair<Dictionary<String>,Dictionary<String>>,TreeMap<String,RankedWeaver>,State,State> selector){
		myCore.setSelector(selector);
	}
	
	//different running method
	public void trainHammer(int n){
		for(int i=0;i<n;i++){
			myWorld.train();
		}
	}
	public void runSample(int n){
		for(int i=0;i<n;i++){
			myWorld.sampleRun();
		}
	}
	public void runHammer(int n){
		//debugging
		//System.out.println("-----------------------"+myCore.pimpl_.actions_.data);
		for(int i=0;i<n;i++){
			myWorld.next(myCore.pimpl_.actions_);
		}
	}
	public void runNoAction(int n){
		for(int i=0;i<n;i++){
			myWorld.dummyRun();
		}
	}
	public void runRandom(int n){
		for(int i=0;i<n;i++){
			myWorld.randomRun();
		}
		//clear command cache for next data generation
		myWorld.patientSimu1.clearCommandCache();
	}
	
	public void HammerNext(){
		
		//Deep clone current state
		prevState = cloner.deepClone(newState);
		//myWorld.updatePublications(prevState);
		
		// A shallow copy of prevState with timestamp incremented
		newState = State.next(prevState);
		
		// update the aspects in new state from the real world
		stateUpdate();
		
		myCore.addState(newState, target_);
		
		//Update action suggestions from Hammer core
		//System.out.println("Suggested actions: "+myCore.pimpl_.actions_.data+"selected by model: "+myCore.pimpl_.selectedModels_.data);
		//System.out.println("===============================================================================================");
		
	}
	public void endWorld(){
		myWorld.worldEnd();
	}
	public void endHammer(){
		try
		{
			out.close();
		} catch (IOException e)
		{
		}
	}
	public void recordHammer(String s){
		try
		{
			
			out.write(s);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String findConfidenceName(){
		String s="";
		try
		{
			confidenceMap_=myCore.getConfidenceMap();
			System.out.println("~Confidences~");
			for (Entry<String, Double> entry : confidenceMap_.pimpl_.dictionary_.data.entrySet())
			{
				s = s.concat(entry.getKey() + ",");
			}
			s = s.concat("\n");
		} catch (InterruptedException e)
		{
		}
		return s;
	}
	public String findConfidenceValue(){
		String s="";
		try
		{
			confidenceMap_=myCore.getConfidenceMap();
			System.out.println("~Confidences~");
			for (Entry<String, Double> entry : confidenceMap_.pimpl_.dictionary_.data.entrySet())
			{
				s = s.concat(entry.getValue() + ",");
			}
			s = s.concat("\n");
		} catch (InterruptedException e)
		{
		}
		return s;
	}
}
