package patientSimulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import hammer.dictionary.impl.Dictionary;
import virtual_patient.FunctionBlocks.Patient;
import virtual_patient.paramGen.Params;
import org.ejml.simple.SimpleMatrix;
import org.ejml.ops.RandomMatrices;

public class patientSimu
{

	public ArrayList<Integer> GlucoseCounterArray=new ArrayList<Integer>();
	public ArrayList<Integer> InsulinCounterArray=new ArrayList<Integer>();
	public ArrayList<Double> GlucoseAmountArray=new ArrayList<Double>();
	public ArrayList<Double> InsulinAmountArray=new ArrayList<Double>();
	public ArrayList<Double> LAInsulinUnitArray=new ArrayList<Double>();
	public ArrayList<Double> LAInsulinDurationArray=new ArrayList<Double>();
	public ArrayList<Integer> RecoTime=new ArrayList<Integer>();
	public ArrayList<Double> rfccArray=rfccArrayGen();
	public int N=rfccArray.size();
	public ArrayList<Double> poster=new ArrayList<Double>();
	public ArrayList<ArrayList<Double>> posters=new ArrayList<ArrayList<Double>>();
	public int time=0;	//In minute
	public int timeInDay=0;
	
	public Double GlucoseIndex=0.0;
	public Double InsulinIndex=0.0;
	public Params p = new Params("type1"); //patient type is default to be T1DM
	public Patient patient1=new Patient(p);
	public BufferedWriter out;
	public BufferedWriter actionWriter;
	public Double LAu=0.0;
	public Double FAu=0.0;
	public String[] tokens;
	
	public SimpleMatrix GRecord=new SimpleMatrix(0,0);
	public SimpleMatrix GOb=new SimpleMatrix(0,0);
	public SimpleMatrix GTrain=new SimpleMatrix(0,0);
	public SimpleMatrix FAInsulinTrain=new SimpleMatrix(0,0);
	public SimpleMatrix LAInsulinTrain=new SimpleMatrix(0,0);
	public SimpleMatrix CarbTrain=new SimpleMatrix(0,0);
	public Integer predInteveval=60;//predict 60 min from now
	public Double CurrentOb=0.0;
	public Integer predInterval=60;
	public String mode="Train";
	
	//Morning injection and glucose
	//1 unit of insulin = 34.7 ug
	//1 ug insulin = 172.1 pmol
	//u(t) (pmol/kg/min)
	public Double unit2ug=34.7;
	public Double ug2pmol=172.1;
	
	public String LAKey="";
	public String FAKey="";
	public String CarbKey="";
	public Integer levelSel=1;
	public ArrayList<Integer> timeArray=new ArrayList<Integer>();
	
	public patientSimu(){
		
	}
	public patientSimu(String type, Double age){
		p = new Params(type);
	}
	
	public void next(Dictionary<String> actions){
		mode="Run";
		Double ToEat=0.0;
		
		timeInDay=time%(60*24);
		
		//==========================Process long-acting insulin=========================================================
		//Select the right level of actions to take
		Double LAInsulin=0.0;
		for(String s:actions.data.keySet()){
			tokens=s.split("-");
			if(Integer.parseInt(tokens[0])==levelSel&&s.contains("LAInsulin")){
				LAKey=s;
			}
		}
		//only add long-acting insulin at 10 pm
		if(timeInDay==(22*60)){
			if(actions.get(LAKey)!=null){
				LAInsulin=Double.parseDouble(actions.get(LAKey));
			}
			addTrainingData(actions);
			addLAInsulin(LAInsulin,23*60.0);
		}
		updateLAInsulin();
		
		//==========================Process fast-acting insulin and carb=========================================================
		//Select the right level of actions to take
		Double FAInsulin=0.0;
		Double Carb=0.0;
		Double u=0.0;
		
		for(String s:actions.data.keySet()){
			tokens=s.split("-");
			if(Integer.parseInt(tokens[0])==levelSel&&s.contains("FAInsulin")){
				FAKey=s;
			}			
			if(Integer.parseInt(tokens[0])==levelSel&&s.contains("Carb")){
				CarbKey=s;
			}
		}
		if(timeInDay==(8*60-30)||timeInDay==(12*60-30)||timeInDay==(18*60-30)){
			if(actions.get(FAKey)!=null){
				FAInsulin=Double.parseDouble(actions.get(FAKey));
			}
			addTrainingData(actions);

		}
		if(timeInDay==(8*60)||timeInDay==(12*60)||timeInDay==(18*60)){
			if(actions.get(CarbKey)!=null){
				Carb=Double.parseDouble(actions.get(CarbKey));
			}
			addTrainingData(actions);
		}
		FAu=FAInsulin*unit2ug*ug2pmol/patient1.p.BW;
		u=LAu+FAu;
		patient1.injectInsulin(u);
		patient1.ingestGlucose(Carb*1000);
		
	    patient1.next();
	    checkObData();
		//==========================Update training data=========================================================
	    SimpleMatrix Gmat=new SimpleMatrix(1,1);
		Gmat.set(patient1.G());
		GRecord=GRecord.combine(GRecord.numRows(), 0, Gmat);
	    
		//System.out.println("-------------------"+Carb+" "+u);
		//print actions
		if(RecoTime.contains(timeInDay)){
			printDic(actions);
		}
		
	    updateIndex();
	    udpatePoster();
	    recordPatientSimu();
	    resetHammerOutput(actions);
	    time++;
	    
	}
	
	public void train(){
	
		mode="Train";
		Double i=0.0;
		Double n=0.0;
		Random ran=new Random();
		Dictionary<String> actions=new Dictionary<String>();
		Double Carb=0.0;
		Double LAInsulin=0.0;
		Double FAInsulin=0.0;
		FAInsulin=0.0;
		Boolean addData=false;
		
		//FAINsulin and Carb run together
		Double G=patient1.G();
		timeInDay=time%(60*24);
		if(timeInDay==(8*60-30)||timeInDay==(12*60-30)||timeInDay==(18*60-30)){
			if(G>=70&&G<100){
				FAInsulin=5.0;
			}
			else if(G>=100&&G<150){
				FAInsulin=6.0;
			}
			else if(G>=150&&G<200){
				FAInsulin=7.0;
			}
			else if(G>=200&&G<250){
				FAInsulin=8.0;
			}
			else if(G>=250&&G<300){
				FAInsulin=9.0;
			}
			else if(G>=300){
				FAInsulin=10.0;
			}
			addData=true;
		}
		if(timeInDay==60*22){
			if(G>=200&&G<250){
				FAInsulin=1.0;
			}
			else if(G>=250&&G<300){
				FAInsulin=2.0;
			}
			else if(G>=300){
				FAInsulin=3.0;
			}	
			addData=true;
		}
		
		//Carb, not run
		if(timeInDay==(8*60)||timeInDay==(12*60)||timeInDay==(18*60)){
			//Carb=10.0+patient1.Hunger*33;
			Carb=30.0;
			addData=true;
		}
		
		//LAInsulin
		if(timeInDay==(22*60)){
			LAInsulin=20.0;
			addData=true;
		}
		addLAInsulin(LAInsulin,23*60.0);
		updateLAInsulin();//find LAu
		FAu=FAInsulin* unit2ug*ug2pmol/patient1.p.BW;//find FAu
		patient1.injectInsulin(LAu+FAu);				
		patient1.ingestGlucose(Carb*1000);
		
		//Data processing
		if(addData){
			actions.put("Carb", Carb.toString()); CarbKey="Carb";
			actions.put("LAInsulin", LAInsulin.toString()); LAKey="LAInsulin";
			actions.put("FAInsulin", FAInsulin.toString()); FAKey="FAInsulin";
			addTrainingData(actions);
		}
			
		patient1.next();
	    checkObData();
	    SimpleMatrix Gmat=new SimpleMatrix(1,1);
		Gmat.set(patient1.G());
		GRecord=GRecord.combine(GRecord.numRows(), 0, Gmat);
		
		updateIndex();
	    udpatePoster();
	    recordPatientSimu();
	    time++;
	    
	}
	

	public void sampleRun(){
	
		mode="Dummy";
		Double i=0.0;
		Double n=0.0;
		Random ran=new Random();
		Dictionary<String> actions=new Dictionary<String>();
		Double Carb=0.0;
		Double LAInsulin=0.0;
		Double FAInsulin=0.0;
		FAInsulin=0.0;
		Boolean addData=false;
		
		//FAINsulin and Carb run together
		Double G=patient1.G();
		timeInDay=time%(60*24);
		if(timeInDay==(8*60-30)){
			patient1.injectInsulin(45*2*70/7.17);	
		}
		if(timeInDay==(12*60-30)){
			patient1.injectInsulin(70*1*70/7.17);	
		}
		if(timeInDay==(20*60-30)){
			patient1.injectInsulin(70*1.5*70/7.17);	
		}
		
		if(timeInDay==(8*60)){
			patient1.ingestGlucose(45*1000.0);
		}
		if(timeInDay==(12*60)){
			patient1.ingestGlucose(70*1000.0);
		}
		if(timeInDay==(20*60)){
			patient1.ingestGlucose(70*1000.0);
		}
		patient1.next();
		updateIndex();
	    udpatePoster();
	    recordPatientSimu();
	    time++;
	    
	}
	
	//Run the simulation without any insulin or recommendation
	public void dummyRun(){
		patient1.next();
	    udpatePoster();
	    recordPatientSimu();
	    time++;
	}
	//Run the simulation with random insulin injection and glucose ingestion
	public void randomRun(){
		mode="Ran";
		Double i=0.0;
		Double n=0.0;
		Random ran=new Random();
		Dictionary<String> actions=new Dictionary<String>();
		Double Carb=0.0;
		Double LAInsulin=0.0;
		Double FAInsulin=0.0;
		FAInsulin=0.0;
		if(time%(24*60)==6*60){
			i=ran.nextDouble();
			if(i<0.5){
				n=ran.nextDouble();
				Carb=n*100;
			}
			i=ran.nextDouble();
			if(i<0.5){
				n=ran.nextDouble();
				FAInsulin=n*10;
				FAu=FAInsulin* unit2ug*ug2pmol/patient1.p.BW;
			}
			i=ran.nextDouble();
			if(i<0.5){
				n=ran.nextDouble();
				LAInsulin=40*n;
				addLAInsulin(LAInsulin,23*60.0);
			}
			
			updateLAInsulin();//find LAu
			patient1.injectInsulin(LAu+FAu);				
			patient1.ingestGlucose(Carb*1000);
			actions.put("Carb", Carb.toString()); CarbKey="Carb";
			actions.put("LAInsulin", LAInsulin.toString()); LAKey="LAInsulin";
			actions.put("FAInsulin", FAInsulin.toString()); FAKey="FAInsulin";
			//System.out.println("-------------------"+Carb+" "+LAInsulin+" "+FAInsulin);
			addTrainingData(actions);
			
		}
		
		patient1.next();
	    checkObData();
	    SimpleMatrix Gmat=new SimpleMatrix(1,1);
		Gmat.set(patient1.G());
		GRecord=GRecord.combine(GRecord.numRows(), 0, Gmat);
		
		updateIndex();
	    udpatePoster();
	    recordPatientSimu();
	    time++;
	}
	
	public void udpatePoster(){
		poster.clear();
		
		poster.add(patient1.G());
		poster.add(patient1.I());
		poster.add(patient1.EGP());
		poster.add(patient1.Ra());
		poster.add(patient1.U());
		poster.add(patient1.S());
		poster.add(patient1.HE());
		poster.add(InsulinIndex);
		poster.add(GlucoseIndex);
		poster.add(patient1.Hunger);
		//Dummy
		poster.add(patient1.ToEat);
		poster.add(patient1.p.BW);
		
		//13-15 PHYCHOLOGIAL
		poster.add(patient1.Depression);
		poster.add(patient1.Anxiety);
		poster.add(patient1.EmotionalAdjustment);
		
		//16-21 HEI
		poster.add(patient1.PositiveAndActiveEngagementInLife);
		poster.add(patient1.HealthDirectedBehavior);
		poster.add(patient1.SkillAndTechniqueAcquisition);
		poster.add(patient1.ConstructiveAttitudesAndApproaches);
		poster.add(patient1.SelfMonitoringAndInsight);
		poster.add(patient1.EmotionalWellbeing);
		
		//22-29 UE
		poster.add(patient1.AestheticAppeal);
		poster.add(patient1.Challenge);
		poster.add(patient1.Endurability);
		poster.add(patient1.Feedback);
		poster.add(patient1.Interactivity);
		poster.add(patient1.Pleasure);
		poster.add(patient1.SensoryAppeal);
		poster.add(patient1.VarietyNovelty);
		
		posters.add(poster);
	}
	
	//apply fir filter to update insulin index and glucose index
	public void updateIndex(){
	    //FIR approximations
	    for(int j=0;j<InsulinCounterArray.size();j++){
	    	if(InsulinCounterArray.get(j)>(N*5)){
	    		InsulinCounterArray.remove(j);
	    		InsulinAmountArray.remove(j);
	    	}
	    }
	    
	    for(int j=0;j<GlucoseCounterArray.size();j++){
	    	if(GlucoseCounterArray.get(j)>(N*5)){
	    		GlucoseCounterArray.remove(j);
	    		GlucoseAmountArray.remove(j);
	    	}
	    }
	    
		InsulinIndex=0.0;
		GlucoseIndex=0.0;
	    for(int j=0;j<InsulinCounterArray.size();j++){
	    	InsulinIndex+=InsulinAmountArray.get(j)*rfccArray.get((InsulinCounterArray.get(j)-1)/5);
	    	InsulinCounterArray.set(j, InsulinCounterArray.get(j)+1);
	    }
	    for(int j=0;j<GlucoseAmountArray.size();j++){
	    	GlucoseIndex+=GlucoseAmountArray.get(j)*rfccArray.get((GlucoseCounterArray.get(j)-1)/5);
	    	GlucoseCounterArray.set(j, GlucoseCounterArray.get(j)+1);
	    }
	    
	}
	
	public void init()
	{
		patient1.init();
		updateIndex();
		udpatePoster();
		try
		{
			out = new BufferedWriter(new FileWriter("/home/hao/Data/Temp/PatientSimuHistory.dat"));
		} catch (IOException e)
		{
		}
		RecordPatientSimuNames();
		recordPatientSimu();
	}
	
	public static ArrayList<Double> rfccArrayGen(){
		Double d=4.0;
		Double u=12.0;
		Double sigma=9.0;
		int N=36;
		Double Gsum=0.0;
		Double item=0.0;
		ArrayList<Double> rfccArrayUnNorm= new ArrayList<Double>();
		ArrayList<Double> rfccArrayNorm= new ArrayList<Double>();
		for(int k=1;k<=N;k++){
			item=rfccUnNormGen(d,u,sigma,k,N);
			rfccArrayUnNorm.add(item);
			Gsum+=item;
		}

		for(int k=1;k<=N;k++){
			rfccArrayNorm.add(k-1, rfccArrayUnNorm.get(k-1)/Gsum);
		}
		return rfccArrayNorm;
	}
	
	public static Double rfccUnNormGen(Double d,Double u, Double sigma,int k, int N){
		Double result=0.0;
		if(k==1){
			result= 0.5*(gGen(d,u,sigma,1)+gGen(d,u,sigma,1));
		}
		else if(k>1 && k<N){
			result= (gGen(d,u,sigma,k-1)+gGen(d,u,sigma,k)+gGen(d,u,sigma,k+1))/3;
		}
		else if(k==N){
			result= (gGen(d,u,sigma,N-1)+gGen(d,u,sigma,N))/3;
		}
		return result;
	}
	public static Double gGen(Double d,Double u, Double sigma,int k){
		Double g;
		if(k<=d){
			g=0.0;
		}
		else{
			g=Math.exp(-Math.pow((k-u), 2)/(2*Math.pow(sigma, 2)))/(sigma*Math.sqrt(2*Math.PI));
		}
		return g;
	}
	public void RecordPatientSimuNames(){
		try
		{
			out.write("G,I,GI,II\n");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void recordPatientSimu(){
		try
		{
			out.write(String.format( "%.4f", patient1.G())  +","+ String.format( "%.4f", patient1.I())+","+String.format( "%.4f", GlucoseIndex)+","+String.format( "%.4f", InsulinIndex) +"\n");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void recordPatientSimuClose(){
		try
		{
			recordData();
			out.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Double updateLAInsulin(){
		Double LAunit_=0.0;
		Double LAlength_=23*60.0; //effective duration of long-acting insulin is 23 hours
		for(int i=0;i<LAInsulinUnitArray.size();i++){
			if(LAInsulinDurationArray.get(i)>=0){
				LAunit_+=LAInsulinUnitArray.get(i);
				LAInsulinDurationArray.set(i, LAInsulinDurationArray.get(i)-1);
			}
			else{
				LAInsulinUnitArray.remove(i);
				LAInsulinDurationArray.remove(i);
				i--;
			}
		}
		
		LAu=LAunit_*unit2ug*ug2pmol/(p.BW*LAlength_);
		return LAu;
	}
	
	//add long acting insulin plan plan, duration is in min
	public void addLAInsulin(Double unit, Double duration){
		LAInsulinUnitArray.add(unit);
		LAInsulinDurationArray.add(duration);
	}
	
	public void resetHammerOutput(Dictionary<String> actions){
		LAu=0.0;
		actions.empty();
	}
	public void setRecoTime(ArrayList<Integer> a){
		RecoTime=a;
	}
	/*
	public void updateGObData(Integer predInterval,SimpleMatrix Gdata){
		SimpleMatrix newData=new SimpleMatrix(1,1);
		if(RecoTime.contains(timeInDay%(24*60)+predInterval)){
			for(int i=0;i<Gdata.numRows();i++){
				if(RecoTime.contains(i%(24*60)+predInterval)){
					newData.set(0,0,Gdata.get(i,0));
					GOb=GOb.combine(GOb.numRows(), 0, newData);
				}
			}			
		}
	}*/
	
	public void addTrainingData(Dictionary<String> actions){
		SimpleMatrix Carbmat=new SimpleMatrix(1,1);
		SimpleMatrix FAInsulinmat=new SimpleMatrix(1,1);
		SimpleMatrix LAInsulinmat=new SimpleMatrix(1,1);
		SimpleMatrix Gmat=new SimpleMatrix(1,1);
		Double CarbTemp=0.0;
		Double FAInsulinTemp=0.0;
		Double LAInsulinTemp=0.0;
		if(actions.contains(CarbKey)){
			CarbTemp=Double.parseDouble(actions.get(CarbKey));
		}
		if(actions.contains(FAKey)){
			FAInsulinTemp=Double.parseDouble(actions.get(FAKey));
		}
		if(actions.contains(LAKey)){
			LAInsulinTemp=Double.parseDouble(actions.get(LAKey));
		}
		//System.out.println("=============================="+CarbKey+" "+FAKey+" "+LAKey);
		//System.out.println("=============================="+CarbTemp+" "+FAInsulinTemp+" "+LAInsulinTemp);
		Carbmat.set(0, 0, CarbTemp);
		FAInsulinmat.set(0, 0, FAInsulinTemp);
		LAInsulinmat.set(0, 0, LAInsulinTemp);
		Gmat.set(0, 0, patient1.G());
		CarbTrain=CarbTrain.combine(CarbTrain.numRows(), 0, Carbmat);
		FAInsulinTrain=FAInsulinTrain.combine(FAInsulinTrain.numRows(), 0, FAInsulinmat);
		LAInsulinTrain=LAInsulinTrain.combine(LAInsulinTrain.numRows(), 0, LAInsulinmat);
		GTrain=GTrain.combine(GTrain.numRows(), 0, Gmat);
		timeArray.add(time+predInterval);
	}
	
	public void checkObData(){
		if(!timeArray.isEmpty()){
			if(time>=timeArray.get(0)){
				SimpleMatrix GObmat=new SimpleMatrix(1,1);		
				GObmat.set(0, 0, patient1.G());
				GOb=GOb.combine(GOb.numRows(), 0, GObmat);
				timeArray.remove(0);
				CurrentOb=patient1.G();
			}			
		}
	}
	
	public void clearCommandCache(){
		LAu=0.0;
		FAu=0.0;
		LAInsulinDurationArray.clear();
		LAInsulinUnitArray.clear();
	}
	
	
	public void recordData(){
		try
		{	
			GOb.saveToFileCSV("/home/hao/Data/Temp/GOb.dat");
			GTrain.saveToFileCSV("/home/hao/Data/Temp/GTrain.dat");
			LAInsulinTrain.saveToFileCSV("/home/hao/Data/Temp/LAInsulinTrain.dat");
			FAInsulinTrain.saveToFileCSV("/home/hao/Data/Temp/FAInsulinTrain.dat");
			CarbTrain.saveToFileCSV("/home/hao/Data/Temp/CarbTrain.dat");
		} catch (IOException e)
		{
		}
	}
	
	public void printDic(Dictionary<String> actions){
		
		//debugging
		String filePathString="/home/hao/Data/Temp/actions.dat";
		
		try
		{
			
			File f = new File(filePathString);
			if(!f.exists()){
				f.createNewFile();
			}
			actionWriter = new BufferedWriter(new FileWriter(f, true));
			actionWriter.append("=======================================================\n");
			for(String s:actions.data.keySet()){
				actionWriter.append(s+": "+actions.get(s)+"\n");
			}
			
			actionWriter.close();
		
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
