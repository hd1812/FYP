package hammer.world;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import hammer.common.Point2D;
import hammer.dictionary.impl.Dictionary;
import hammer.exception.HammerException;
import hammer.state.State;

//This class simulates a case where robot is running towards a position.
//This should be replaced by the actual object we are observing.

public class WorldTest
{
	public State state_;
	public Dictionary<Object> publications_;
	public boolean	targetReached_;
	public Point2D	robotPosition_;
	public Point2D	goal_;
	public int		robotSpeed_;
	
	//Patient Parameters
	public int day_;
	public String name_;
	public int age_;
	public Double height_cm; //cm
	public Double weight_kg; //Kg
	public Double GlucoseLv_mmol;	//mlU/L
	public String GlucoseLv_description;// Very low, low, normal, high, very high
										//Actions: No aciton required, Take pills, Insulin injection, Exercise
	Random randomGenerator = new Random();
	
	final int		kMapSize	= 100;
	final Point2D[]	kGoals		= {new Point2D(60, 8), new Point2D(17, 24), new Point2D(36, 53), new Point2D(84, 76)};
	Lock Mainlock = new ReentrantLock();
	
	public WorldTest()
	{
		targetReached_ = true;
		robotPosition_ = new Point2D();
		goal_ = new Point2D();
		publications_=new Dictionary<Object>();
		state_=new State();
		//Object initialisation
		randomGenerator = new Random();
		age_=randomGenerator.nextInt(80);
		name_="Daniel";
		day_=0;
		height_cm=randomGenerator.nextDouble()*40+150;
		weight_kg=randomGenerator.nextDouble()*30+50;
		GlucoseLv_mmol=randomGenerator.nextDouble()*8+4;//4-8-12
		if(GlucoseLv_mmol>11){
			GlucoseLv_description="Very High";
		}
		else if (GlucoseLv_mmol>9){
			GlucoseLv_description="High";
		}
		else if(GlucoseLv_mmol>7){
			GlucoseLv_description="Normal";
		}
		else if(GlucoseLv_mmol>6){
			GlucoseLv_description="Low";
		}
		else{
			GlucoseLv_description="Very Low";
		}
		state_.put("Name", name_);
		state_.put("Age", age_);
		state_.put("height_cm", height_cm);
		state_.put("weight_kg", name_);
		state_.put("GlucoseLv", GlucoseLv_mmol);
		state_.put("GlucoseLv_description", GlucoseLv_description);
		System.out.println("-----------------Patient State-----------------");
		System.out.println("Name: "+name_);
		System.out.println("Age: "+age_);
		System.out.println("height_cm: "+height_cm);
		System.out.println("weight_kg: "+weight_kg);
		System.out.println("GlucoseLv: "+GlucoseLv_mmol);
		System.out.println("GlucoseLv_description: "+GlucoseLv_description);
		System.out.println("-----------------------------------------------");
	}

	public void next(Dictionary<String> actions)
	{
		String GlucoseLvAction_=actions.get("GlucoseLv");
		//System.out.println("Actions:::::::::"+GlucoseLvAction_);
		Random randomGenerator = new Random();

		if (targetReached_)
		{
			//Re-initialise simulation
			System.out.println("Start a new simulation");
			day_=0;
			age_=randomGenerator.nextInt(80);
			height_cm=randomGenerator.nextDouble()*40+150;
			weight_kg=randomGenerator.nextDouble()*30+50;
			GlucoseLv_mmol=randomGenerator.nextDouble()*8+4;//4-8-12
			if(GlucoseLv_mmol>11){
				GlucoseLv_description="Very High";
			}
			else if (GlucoseLv_mmol>8.5){
				GlucoseLv_description="High";
			}
			else if(GlucoseLv_mmol>7.5){
				GlucoseLv_description="Normal";
			}
			else if(GlucoseLv_mmol>6){
				GlucoseLv_description="Low";
			}
			else{
				GlucoseLv_description="Very Low";
			}
			
			targetReached_ = false;
			
			state_.put("Name", name_);
			state_.put("Age", age_);
			state_.put("height_cm", height_cm);
			state_.put("weight_kg", weight_kg);
			state_.put("GlucoseLv", GlucoseLv_mmol);
			state_.put("GlucoseLv_description", GlucoseLv_description);
			
			//Robot simulation
			robotPosition_.x = randomGenerator.nextInt(kMapSize) % kMapSize;
			robotPosition_.y = randomGenerator.nextInt(kMapSize) % kMapSize;
			goal_ = kGoals[randomGenerator.nextInt(4) % 4];
			robotSpeed_ = randomGenerator.nextInt(3) % 3 + 1;
			

			System.out.println("-----------------Patient State-----------------");
			System.out.println("Name: "+name_);
			System.out.println("Age: "+age_);
			System.out.println("height_cm: "+height_cm);
			System.out.println("weight_kg: "+weight_kg);
			System.out.println("GlucoseLv: "+GlucoseLv_mmol);
			System.out.println("GlucoseLv_description: "+GlucoseLv_description);
			System.out.println("-----------------------------------------------");
			
		} else
		{
			//Patient Simulation
			day_++;
			weight_kg+=randomGenerator.nextDouble()-0.5;
			if(GlucoseLvAction_.equals("Insulin injection")){
				GlucoseLv_mmol+=0.2;
			}
			else if(GlucoseLvAction_.equals("Take pills")){
				GlucoseLv_mmol+=0.1;
			}
			else if(GlucoseLvAction_.equals("Exercise")){
				GlucoseLv_mmol-=0.1;
			}
			if(GlucoseLv_mmol>11){
				GlucoseLv_description="Very High";
			}
			else if (GlucoseLv_mmol>8.5){
				GlucoseLv_description="High";
			}
			else if(GlucoseLv_mmol>7.5){
				GlucoseLv_description="Normal";
			}
			else if(GlucoseLv_mmol>6){
				GlucoseLv_description="Low";
			}
			else{
				GlucoseLv_description="Very Low";
			}
			
			state_.put("Name", name_);
			state_.put("Age", age_);
			state_.put("height_cm", height_cm);
			state_.put("weight_kg", weight_kg);
			state_.put("GlucoseLv", GlucoseLv_mmol);
			state_.put("GlucoseLv_description", GlucoseLv_description);
			
			//Robot Simulation
			int xDistance = goal_.x - robotPosition_.x;
			int yDistance = goal_.y - robotPosition_.y;
			int absXDistance = Math.abs(xDistance);
			int absYDistance = Math.abs(yDistance);

			// Move on direction on most distant axis (without going past goal)
			if (absXDistance > absYDistance)
			{
				if (absXDistance < robotSpeed_)
					robotPosition_.x = goal_.x;
				else if (xDistance < 0)
					robotPosition_.x = robotPosition_.x - robotSpeed_;
				else
					robotPosition_.x = robotPosition_.x + robotSpeed_;
			} else
			{
				if (absYDistance < robotSpeed_)
					robotPosition_.y = goal_.y;
				else if (yDistance < 0)
					robotPosition_.y = robotPosition_.y - robotSpeed_;
				else
					robotPosition_.y = robotPosition_.y + robotSpeed_;

			}
			

			System.out.println("-----------------Patient State-----------------");
			System.out.println("Name: "+name_);
			System.out.println("Age: "+age_);
			System.out.println("height_cm: "+height_cm);
			System.out.println("weight_kg: "+weight_kg);
			System.out.println("GlucoseLv: "+GlucoseLv_mmol);
			System.out.println("GlucoseLv_description: "+GlucoseLv_description);
			System.out.println("-----------------------------------------------");
			// Check whether we have reached our target
			if (GlucoseLv_description.equals("Normal")){
				targetReached_ = true;
				System.out.println("+++++++++++++++++Target Reached in Day "+day_+" +++++++++++++++++++++++++");
			}
		}
	}

	public void train()
	{

		Random randomGenerator = new Random();

		if (targetReached_)
		{
			//Re-initialise simulation
			System.out.println("+++++++++++++++++++++Start a new simulation++++++++++++++++++++++++");
			day_=0;
			height_cm=randomGenerator.nextDouble()*40+150;
			weight_kg=randomGenerator.nextDouble()*30+50;
			GlucoseLv_mmol=randomGenerator.nextDouble()*8+4;//4-8-12
			if(GlucoseLv_mmol>11){
				GlucoseLv_description="Very High";
			}
			else if (GlucoseLv_mmol>9){
				GlucoseLv_description="High";
			}
			else if(GlucoseLv_mmol>7){
				GlucoseLv_description="Normal";
			}
			else if(GlucoseLv_mmol>6){
				GlucoseLv_description="Low";
			}
			else{
				GlucoseLv_description="Very Low";
			}
			
			targetReached_ = false;
			
			state_.put("Name", name_);
			state_.put("Age", age_);
			state_.put("height_cm", height_cm);
			state_.put("weight_kg", weight_kg);
			state_.put("GlucoseLv", GlucoseLv_mmol);
			state_.put("GlucoseLv_description", GlucoseLv_description);
			
			//Robot simulation
			robotPosition_.x = randomGenerator.nextInt(kMapSize) % kMapSize;
			robotPosition_.y = randomGenerator.nextInt(kMapSize) % kMapSize;
			goal_ = kGoals[randomGenerator.nextInt(4) % 4];
			robotSpeed_ = randomGenerator.nextInt(3) % 3 + 1;
			
			Mainlock.lock();
			System.out.println("-----------------Patient State-----------------");
			System.out.println("Name: "+name_);
			System.out.println("Age: "+age_);
			System.out.println("height_cm: "+height_cm);
			System.out.println("weight_kg: "+weight_kg);
			System.out.println("GlucoseLv: "+GlucoseLv_mmol);
			System.out.println("GlucoseLv_description: "+GlucoseLv_description);
			System.out.println("-----------------------------------------------");
			Mainlock.unlock();
		} else
		{
			//Patient Simulation
			day_++;
			weight_kg+=randomGenerator.nextDouble()-0.5;
			if(GlucoseLv_mmol>8.5){
				GlucoseLv_mmol-=0.1;
			}
			else if(GlucoseLv_mmol<7.5&&GlucoseLv_mmol>6){
				GlucoseLv_mmol+=0.1;
			}
			else if(GlucoseLv_mmol<6){
				GlucoseLv_mmol+=0.2;
			}
			if(GlucoseLv_mmol>11){
				GlucoseLv_description="Very High";
			}
			else if (GlucoseLv_mmol>8.5){
				GlucoseLv_description="High";
			}
			else if(GlucoseLv_mmol>7.5){
				GlucoseLv_description="Normal";
			}
			else if(GlucoseLv_mmol>6){
				GlucoseLv_description="Low";
			}
			else{
				GlucoseLv_description="Very Low";
			}
			Mainlock.lock();
			System.out.println("-----------------Patient State-----------------");
			state_.put("Name", name_);
			state_.put("Age", age_);
			state_.put("height_cm", height_cm);
			state_.put("weight_kg", name_);
			state_.put("GlucoseLv", GlucoseLv_mmol);
			state_.put("GlucoseLv_description", GlucoseLv_description);

			System.out.println("Name: "+name_);
			System.out.println("Age: "+age_);
			System.out.println("height_cm: "+height_cm);
			System.out.println("weight_kg: "+weight_kg);
			System.out.println("GlucoseLv: "+GlucoseLv_mmol);
			System.out.println("GlucoseLv_description: "+GlucoseLv_description);
			System.out.println("-----------------------------------------------");
			Mainlock.unlock();
			
			
			//Robot Simulation
			int xDistance = goal_.x - robotPosition_.x;
			int yDistance = goal_.y - robotPosition_.y;
			int absXDistance = Math.abs(xDistance);
			int absYDistance = Math.abs(yDistance);

			// Move on direction on most distant axis (without going past goal)
			if (absXDistance > absYDistance)
			{
				if (absXDistance < robotSpeed_)
					robotPosition_.x = goal_.x;
				else if (xDistance < 0)
					robotPosition_.x = robotPosition_.x - robotSpeed_;
				else
					robotPosition_.x = robotPosition_.x + robotSpeed_;
			} else
			{
				if (absYDistance < robotSpeed_)
					robotPosition_.y = goal_.y;
				else if (yDistance < 0)
					robotPosition_.y = robotPosition_.y - robotSpeed_;
				else
					robotPosition_.y = robotPosition_.y + robotSpeed_;

			}
			// Check whether we have reached our target
			if (GlucoseLv_description.equals("Normal")){
				targetReached_ = true;
				System.out.println("+++++++++++++++++Target Reached in Day "+day_+" +++++++++++++++++++++++++");
			}
		}
	}
	
	public boolean targetReached()
	{
		return targetReached_;
	}

	public Point2D getRobotPosition()
	{
		return robotPosition_;
	}

	public void updatePublications(State prevState){
		int distance = 0;
		if(!prevState.empty()){
			try
			{
				distance=computeDistance(this.getRobotPosition(), (Point2D) prevState.get("robotPosition"));

			} catch (HammerException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		publications_.data.put("robotPosition", robotPosition_);
		publications_.data.put("distance",distance);

	}
	
	public static int computeDistance(final Point2D a, final Point2D b)
	{
		return Math.abs(a.x - b.x + a.y - b.y);
	}
}
