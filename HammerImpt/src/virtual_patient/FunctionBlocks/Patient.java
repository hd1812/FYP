package virtual_patient.FunctionBlocks;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import virtual_patient.paramGen.Params;

public class Patient
{
	
	public int counter=0;
	public int test;
	public BufferedWriter out;
	Random random = new Random();
	
	//Physical parameters
	public Params p;
	public Double Hunger=0.0;
	public Double Carb=0.0;
	public Double ToEat=0.0;
	
	//Psychological parameters
	public Double Depression=0.0;
	public Double Anxiety=0.0;
	public Double EmotionalAdjustment=0.0;
	
	//HEI. Health Education Impact parameters
	public Double PositiveAndActiveEngagementInLife=0.0;
	public Double HealthDirectedBehavior=0.0;
	public Double SkillAndTechniqueAcquisition=0.0;
	public Double ConstructiveAttitudesAndApproaches=0.0;
	public Double SelfMonitoringAndInsight=0.0;
	public Double SocialIntegrationAndSupport=0.0;
	public Double EmotionalWellbeing=0.0;
	
	//UE. User engagement parameters (target educational application)
	public Double AestheticAppeal=0.0;
	public Double Challenge=0.0;
	public Double Endurability=0.0;
	public Double Feedback=0.0;
	public Double Interactivity=0.0;
	public Double Pleasure=0.0;
	public Double SensoryAppeal=0.0;
	public Double VarietyNovelty=0.0;
	
	//----------------------Class declaration----------------------------------
	public Patient(){}
	public Patient(Params params)
	{
		p=params;
		try
		{
			out = new BufferedWriter(new FileWriter("/home/hao/Data/Temp/file.dat"));
		} catch (IOException e)
		{
		}
	}
	//----------------------Glucose Subsystem----------------------------------------------------------
	public Double G_p_d1(){
		p.G_p_d1 = EGP() + Ra() - U_ii() - E()-p.k_1 * G_p() + p.k_2 * G_t();
		return p.G_p_d1;
	}
	
	public Double G_p(){
		return p.G_p;
	}
	
	public Double G_t_d1(){
		p.G_t_d1=-U_id()+p.k_1*G_p()-p.k_2*G_t();
		return p.G_t_d1;
	}
	
	public Double G_t(){
		return p.G_t;
	}
	
	public Double G(){
		p.G=G_p()/p.V_G;
		return p.G;
	}
	
	//----------------------Insulin Subsystem----------------------------------------------------------
	
	public Double I_l_d1(){
		p.I_l_d1= - ( p.m_1 + m_3() ) * I_l() + m_2() * I_p() + S();
		return p.I_l_d1;
	}
	
	public Double I_l(){
		return p.I_l;
	}
	
	public Double I_p_d1(){
		p.I_p_d1= - (m_2() + m_4())*I_p() + m_1()*I_l() + p.k_a1*S_1()+p.k_a2*S_2();
		return p.I_p_d1;
	}
	
	public Double I_p(){
		return p.I_p;
	}
	
	public Double I(){
		p.I= I_p() / p.V_I;
		return p.I;
	}
	
	public Double HE(){
		p.HE= - p.m_5 * S() + m_6();
		return p.HE;
	}
	
	public Double m_1(){
		return p.m_1;
	}
	
	public Double m_5(){
		return p.m_5;
	}
	
	public Double m_3(){
		p.m_3=HE()*p.m_1/(1-HE());
		return p.m_3;
	}
	
	public Double m_2(){
		return p.m_2;
	}
	
	public Double m_4(){
		return p.m_4;
	}
	
	public Double m_6(){
		return p.m_6;
	}
	
	public Double HE_b(){
		return p.HE_b;
	}
	
	//----------------------1. Endogenous Glucose Production----------------------------------
	public Double EGP(){
		p.EGP= Math.max(p.k_p1 - p.k_p2 * G_p() - p.k_p3 * I_d() - p.k_p4 * p.I_po, 0.0);
		return p.EGP;
	}
	
	public Double I_1_d1(){
		p.I_1_d1= - p.k_i * ( I_1() - I() );
		return p.I_1_d1;
	}
	
	public Double I_1(){
		return p.I_1;
	}
	
	public Double I_d_d1(){
		p.I_d_d1= - p.k_i * ( I_d() - I_1() );
		return p.I_d_d1;
	}
	
	public Double I_d(){
		return p.I_d;
	}
	
	public Double k_p1(){
		return p.k_p1;
	}
	
	public Double k_p2(){
		return p.k_p2;
	}
	
	public Double k_p3(){
		return p.k_p3;
	}
	
	public Double k_p4(){
		return p.k_p4;
	}
	
	public Double EGP_b(){
		return p.EGP_b;
	}
	
	//----------------------2. Glucose Rate of Appearance----------------------------------
	public Double Ra(){
		p.Ra= p.f * p.k_abs * Q_gut() / (p.BW);
		return p.Ra;
	}
	
	public Double Q_sto1_d1(){
		p.Q_sto1_d1= - p.k_gri * Q_sto1() + d_();
		return p.Q_sto1_d1;
	}
	
	public Double Q_sto2_d1(){
		p.Q_sto2_d1= - k_gut() * Q_sto2() + p.k_gri * Q_sto1();
		return p.Q_sto2_d1;
	}
	
	public Double Q_gut_d1(){
		p.Q_gut_d1 = -p.k_abs * Q_gut() + k_gut() * Q_sto2();
		return p.Q_gut_d1;
	}
	public Double Q_sto1(){
		return p.Q_sto1;
	}
	public Double Q_sto2(){
		return p.Q_sto2;
	}
	public Double Q_gut(){
		return p.Q_gut;
	}
	public Double Q_sto(){
		p.Q_sto= Q_sto1() + Q_sto2();
		return p.Q_sto;
	}
	
	//TODO
	public Double k_gut(){
		if(D_()==0){
			p.k_gut=p.k_max;
		}
		else{
			p.k_gut=p.k_min+ 0.5*(p.k_max-p.k_min) * (Math.tanh(AlphaK()*(Q_sto()-p.b*D_()))-Math.tanh(BetaK()*(Q_sto()-p.a*D_()))+2);
		}
		return p.k_gut;
	}
	
	//To avoid confusion, this AlphaK is used in glucose-insulin dynamics
	public Double AlphaK(){
		return 5/(2*D_()*(1-p.b));
	}
	public Double BetaK(){
		return 5/(2*D_()*p.a);
	}
	
	//TODO
	//Approximation
	public Double D_(){
		if(d_()!=0.0){
			p.D_=Q_sto()+d_();
		}
		return p.D_;
	}
	
	//TODO avoid confusion, d() does not return constant d
	public Double d_(){
		return p.d_;
	}
	
	//----------------------3. Glucose Utilisation----------------------------------
	public Double U_ii(){
		p.U_ii = p.F_cns;
		return p.U_ii;
	}
	
	public Double U_id(){
		p.U_id=V_m()*G_t()/(k_m()+G_t());
		return p.U_id;
	}
	
	public Double V_m(){
		p.V_m= V_m0() + p.V_mx * X();
		return p.V_m;
	}
	
	public Double k_m(){
		p.K_m=K_m0();// p.K_mx * X()
		return p.K_m;
	}
	
	public Double X_d1(){
		p.X_d1= - p.p_2U *X()+ p.p_2U * (p.I - p.I_b);
		return p.X_d1;
	}
	
	public Double X(){
		return p.X;
	}
	
	public Double U(){
		p.U=U_id()+U_ii();
		return p.U;
	}
	
	public Double V_m0(){
		return p.V_m0;
	}
	
	public Double K_m0(){
		return p.K_m0;
	}
	
	//----------------------4. Insulin Secretion----------------------------------

	public Double S(){
		p.S = p.gamma * I_po();
		return p.S;
	}

	public Double I_po_d1(){
		p.I_po_d1= -p.gamma * I_po() + S_po();
		return p.I_po_d1;
	}
	
	public Double I_po(){
		return p.I_po;
	}
	
	public Double S_po(){
		if (G_p_d1()/p.V_G>0){
			p.S_po=Y() + p.K*G_p_d1()/p.V_G + p.S_b;
		}
		else {
			p.S_po=Y() + p.S_b;
		}
		return p.S_po;
	}
	
	public Double Y_d1(){
		if(p.beta * (G()-p.h) >= - p.S_b){
			p.Y_d1 = - p.alpha * (p.Y - p.beta*(G()-p.h));
		}
		else {
			p.Y_d1 = - p.alpha * p.Y - p.alpha * p.S_b;
		}
		return p.Y_d1;
	}
	
	public Double Y(){
		return p.Y;
	}
	
	//---------------------- 5. Glucose Renal Excretion----------------------------------
	
	public Double E(){
		if(G_p()>p.k_e2){
			p.E= p.k_e1*(G_p() - p.k_e2);
		}
		else{
			p.E=0.0;
		}
		return p.E;
	}
	
	//-----------------------Subcutaneous Insulin Kinetics-------------------------------
	public Double S_1(){
		return p.S_1;
	}
	public Double S_2(){
		return p.S_2;
	}
	public Double S_1_d1(){
		p.S_1_d1=-(p.k_a1+p.k_d)*S_1()+p.u_;
		return p.S_1_d1;
	}
	public Double S_2_d1(){
		p.S_2_d1=p.k_d*S_1()-p.k_a2*S_2();
		return p.S_2_d1;
	}
	
	public void ingestGlucose(Double n){
		p.d_=n;
	}
	
	public void injectInsulin(Double n){
		p.u_=n;
	}
	
	//---------------------- Other functions---------------------------------------------
	
	//Variable initialisation
	public void init(){
		Hunger=random.nextDouble();
		p.D_=0.0;
		p.d_=0.0;
		p.u_=0.0;
		/*		
		p.u_=0.0;		//
				//
		p.E=0.0;		//
				//Calculated from integrating d_
		p.Ra=0.0;		//
		*/
		
		//compute and apply derivatives
		
		p.S_b=(p.m_6-HE_b())/p.m_5;	//from formula 6 dalla man
		p.I_pb=2.0/5.0 * p.S_b/p.m_4* (1-HE_b()); // from formula 9 dalla man
		p.I_lb=((p.S_b-p.m_4*p.I_pb)*(1-HE_b())) /( HE_b()*p.m_1); //from formula 7 and 8 
		p.I_b=p.I_pb/p.V_I;
		p.G_tb= p.K_m0 *(EGP_b() -p.F_cns)/(p.V_m0 + p.F_cns - EGP_b() ); //inverse formula 22 dalla man
		p.G_pb=(p.k_2*p.G_tb - p.F_cns + EGP_b())/p.k_1; //inverse the formula 20, dalla man
		p.I_pob=(p.k_p1 -  EGP_b() - p.k_p2 *p.G_pb - p.k_p3 * p.I_b)/p.k_p4; //from formula 12 dalla man

		p.h=p.G_pb/p.V_G;
		
		p.G_p=p.G_pb;
		p.G_t=p.G_tb;
		p.I_l=p.I_lb;
		p.I_p=p.I_pb;
		p.I_po=p.I_pob;
		p.I=p.I_b;
		p.I_1=p.I_b;
		p.I_d=p.I_b;
		p.X=p.X_b;
		p.Y=p.Y_b;
		p.HE=p.HE_b;
		p.EGP=p.EGP_b;
		
		p.I_l_d1=0.0;
		p.I_d_d1=0.0;
		p.I_po_d1=0.0;
		p.I_p_d1=0.0;
		p.I_1_d1=0.0;

		p.X_d1=0.0;
		p.Y_d1=0.0;
		
		p.Q_sto1_d1=0.0;
		p.Q_sto2_d1=0.0;
		p.Q_gut_d1=0.0;
		
		p.G_p_d1=0.0;
		p.G_t_d1=0.0;
		
		p.S_1_d1=0.0;
		p.S_2_d1=0.0;
		
		try
		{
			out.write(counter+","+String.format( "%.4f", G())  +","+ String.format( "%.4f", I()) +","+ String.format( "%.4f", EGP()) +","+ String.format( "%.4f", Ra()) +","+ String.format( "%.4f", U()) +","+ String.format( "%.4f", S()) +","+ String.format( "%.4f", HE()));
			counter++;
		} catch (IOException e)
		{
		}

	}
	
	public void next(){
		//additional params
		Hunger=random.nextDouble();
		
		
		//compute and apply derivatives
		G_p_d1();
		G_t_d1();
		I_1_d1();
		I_d_d1();
		X_d1();
		I_po_d1();
		Y_d1();
		I_l_d1();
		I_p_d1();
		
		S_1_d1();
		S_2_d1();
		Q_sto1_d1();
		Q_sto2_d1();
		Q_gut_d1();
		
		p.G_p=p.G_p+p.G_p_d1;
		p.G_t=p.G_t+p.G_t_d1;
		p.I_l=p.I_l+p.I_l_d1;
		p.I_d=p.I_d+p.I_d_d1;
		p.I_p=p.I_p+p.I_p_d1;
		p.I_1=p.I_1+p.I_1_d1;
		p.I_po=p.I_po+p.I_po_d1;
		
		p.X=p.X+p.X_d1;
		p.Y=p.Y+p.Y_d1;

		
		p.S_1=p.S_1+p.S_1_d1;
		p.S_2=p.S_2+p.S_2_d1;
		
		p.Q_sto1=p.Q_sto1+p.Q_sto1_d1;
		p.Q_sto2=p.Q_sto2+p.Q_sto2_d1;
		p.Q_gut=p.Q_gut+p.Q_gut_d1;
		
		p.d_=0.0;
		p.u_=0.0;
		
		try
		{
			out.write(counter+","+String.format( "%.4f", G())  +","+ String.format( "%.4f", I()) +","+ String.format( "%.4f", EGP()) +","+ String.format( "%.4f", Ra()) +","+ String.format( "%.4f", U()) +","+ String.format( "%.4f", S()) +","+ String.format( "%.4f", HE()));
			counter++;
		} catch (IOException e)
		{
		}
	}
	
	public void writeClose(){
		try
		{
			out.close();
		} catch (IOException e)
		{
		}
	}
	
}
