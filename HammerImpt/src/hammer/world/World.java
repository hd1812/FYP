package hammer.world;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import hammer.common.Point2D;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;
import hammer.state.*;
import patientSimulation.patientSimu;

//This class simulates a case where robot is running towards a position.
//This should be replaced by the actual object we are observing.

public class World
{
	public State state_;
	public patientSimu patientSimu1=new patientSimu();
	public BufferedWriter out;
	public int time=0;
	
	public World(patientSimu patientSimu1_)
	{
		patientSimu1=patientSimu1_;
		state_=new State();
	}
	

	public void init(ArrayList<String> aspects){
		state_.pimpl_.aspects_=aspects;
		patientSimu1.init();
		publishAll();
		try
		{
			out=new BufferedWriter(new FileWriter("/home/hao/Data/Temp/WorldHistory.dat"));
			recordWorld();
		} catch (IOException e)
		{
		}
	}
	
	public void next(Dictionary<String> actions)
	{
		time++;
		patientSimu1.next(actions);
		publishAll();
		recordWorld();
	}
	public void train(){
		time++;
		patientSimu1.train();
		publishAll();
		recordWorld();
	}
	public void sampleRun(){
		time++;
		patientSimu1.sampleRun();
		publishAll();
		recordWorld();
	}
	public void dummyRun(){
		time++;
		patientSimu1.dummyRun();
		publishAll();
		recordWorld();
	}
	public void randomRun(){
		time++;
		patientSimu1.randomRun();
		publishAll();
		recordWorld();
	}

	public String fmt(Double d){
		return String.format( "%.2f", d);
	}
	
	//function to post features after feature engineering
	public void findFeature(){
		Double G1=0.0;
		
		//Find past G value to publish
		int size=patientSimu1.posters.size();
		if(size>1){
			G1=patientSimu1.posters.get(size-1).get(0);
		}
		state_.put("G1", G1);
		
	}
	
	public void publishAll(){
		int i=0;
		//patientSimu1.next();
		//Physical
		state_.put("Time", time);
		state_.put("G", patientSimu1.poster.get(i));i++;
		state_.put("I", patientSimu1.poster.get(i));i++;
		state_.put("EGP", patientSimu1.poster.get(i));i++;
		state_.put("Ra", patientSimu1.poster.get(i));i++;
		state_.put("U", patientSimu1.poster.get(i));i++;
		state_.put("S", patientSimu1.poster.get(i));i++;
		state_.put("He", patientSimu1.poster.get(i));i++;
		state_.put("II", patientSimu1.poster.get(i));i++;
		state_.put("GI", patientSimu1.poster.get(i));i++;
		state_.put("Hunger", patientSimu1.poster.get(i));i++;
		state_.put("ToEat", patientSimu1.poster.get(i));i++;
		state_.put("BW", patientSimu1.poster.get(i));i++;
		state_.put("rfcc", patientSimu1.rfccArray);
		
		//Psychological
		state_.put("Depression", patientSimu1.poster.get(i));i++;
		state_.put("Anxiety", patientSimu1.poster.get(i));i++;
		state_.put("EmotionalAdjustment", patientSimu1.poster.get(i));i++;
		
		//Health Education Impact
		state_.put("PositiveAndActiveEngagementInLife", patientSimu1.poster.get(i));i++;
		state_.put("HealthDirectedBehavior", patientSimu1.poster.get(i));i++;
		state_.put("SkillAndTechniqueAcquisition", patientSimu1.poster.get(i));i++;
		state_.put("ConstructiveAttitudesAndApproaches", patientSimu1.poster.get(i));i++;
		state_.put("SelfMonitoringAndInsight", patientSimu1.poster.get(i));i++;
		state_.put("EmotionalWellbeing", patientSimu1.poster.get(i));i++;
		
		//User Engagement
		state_.put("AestheticAppeal", patientSimu1.poster.get(i));i++;
		state_.put("Challenge", patientSimu1.poster.get(i));i++;
		state_.put("Endurability", patientSimu1.poster.get(i));i++;
		state_.put("Feedback", patientSimu1.poster.get(i));i++;
		state_.put("Interactivity", patientSimu1.poster.get(i));i++;
		state_.put("Pleasure", patientSimu1.poster.get(i));i++;
		state_.put("SensoryAppeal", patientSimu1.poster.get(i));i++;
		state_.put("VarietyNovelty", patientSimu1.poster.get(i));i++;
		state_.put("PredInternal", patientSimu1.predInteveval);

		//for training
		state_.put("RecoTime",patientSimu1.RecoTime);
		state_.put("GTrain", patientSimu1.GTrain);
		state_.put("LAInsulinTrain", patientSimu1.LAInsulinTrain);
		state_.put("FAInsulinTrain", patientSimu1.FAInsulinTrain);
		state_.put("CarbTrain",patientSimu1.CarbTrain);
		state_.put("GOb",patientSimu1.GOb);
		state_.put("CurrentOb",patientSimu1.CurrentOb);
		state_.put("Mode",patientSimu1.mode);
		
		findFeature();
	}
	
	public void recordWorld(){
		try
		{
			out.write(time+","+fmt(patientSimu1.poster.get(0))+","+fmt(patientSimu1.poster.get(1))+"\n");
		} catch (IOException e)
		{
		}
	}
	
	public void worldEnd(){
		try
		{
			out.close();
			patientSimu1.recordPatientSimuClose();
		} catch (IOException e)
		{
		}
	}
}
