package com.springTwin.springtwinClient;

import com.springTwin.springtwinClient.jetSensor.Mode;
import com.springTwin.springtwinClient.jetSensor.Simulation;

public class jetSetting {
   private double curr_setting;
   private double prev_setting;
	
	public enum Mode{
		SIMULATED,
		REAL
	};
	
	private double[] sim_values;
	private int interval_in_secs=1;
	private Mode mode=Mode.SIMULATED;
	
	
	public	jetSetting() {
		mode = Mode.REAL;
		curr_setting = 0.0;
		interval_in_secs=1;
	}

	//Setup a simulated Sensor
	public	jetSetting(double[] inValues,int interval) {
		mode= Mode.SIMULATED;
		load_values(inValues);
		set_interval(interval);
	}
	
	public void set_interval(int duration) {
		interval_in_secs=duration;
	}
	
	// Set simulated values
	public void load_values(double[] in) {
	   sim_values = in;
	}

	//Read value from an actual setting
	public void set_value() {
		   //value = in;
		}

	//Set simulated setting
	public void set_value(double in) {
		prev_setting =  curr_setting;
		curr_setting = in;
		}
	
	public void revert_setting() {
		curr_setting = prev_setting;
	}
	
	 public double get_setting() {
		   return curr_setting ;
	 }

	 public boolean startSimulation() {
		 Thread simSetting = new Thread(new Simulation());
		 simSetting.start();
		 return true;
	 }
	 
	 class Simulation implements Runnable{
		 
		 private boolean repeatable = false;
		 private static int index=0;
		 
		 public int max_items = 0;
		 
		 public void run() {
			 
			 if(index >= max_items) {
				 if(repeatable)
					 index=0;
			 }
			 else index++;
			 
			 set_value(sim_values[index]);
			 try {
				 Thread.sleep(interval_in_secs * 1000);
			 }catch(InterruptedException e) {}
		 }
	 }
   
}
