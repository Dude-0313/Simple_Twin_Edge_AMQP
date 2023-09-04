package com.springTwin.springtwinClient;


public class jetMotor {
	
	private int motor_id = 0;
	
	private int NUM_SENSORS = 21;
	private int NUM_SETTINGS = 3;
	
	
	private  jetSensor sensors[];
	private  jetSetting settings[];
	
	public jetMotor(int id, int numSettings, int numSensors) {
		
		motor_id = id;
		NUM_SENSORS = numSensors;
		NUM_SETTINGS = numSettings;
		
		sensors = new jetSensor[NUM_SENSORS];
		int x=0;
		for(x=0;x<NUM_SENSORS;x++) {
		sensors[x] = new jetSensor();
		}

		settings = new jetSetting[NUM_SETTINGS];
		for(x=0;x<NUM_SETTINGS;x++) {
		settings[x] = new jetSetting();
		}
	}

  public boolean load_values(double[][] settings_data,double[][] sensors_data ) {

		int x=0;
		int y=0;
		int num_samples=0;
		
		num_samples = settings_data.length;
		for(x=0;x<NUM_SETTINGS;x++) {
			double sim_data[] = new double[num_samples];
			for(y=0;y<num_samples;y++) {
				sim_data[y]= settings_data[y][x];
			}
			settings[x].load_values(sim_data);
		}
		num_samples = sensors_data.length;
		for(x=0;x<NUM_SENSORS;x++) {
			double sim_data[] = new double[num_samples];
			for(y=0;y<num_samples;y++) {
				sim_data[y]= sensors_data[y][x];
			}
			sensors[x].load_values(sim_data);
		}
	  return true;
  }
  
  public double get_sensor_value(int no) {
	if(no <0 || no>= NUM_SENSORS) return -1;
	return sensors[no].get_value(); 
  }
	
  public double get_setting(int no) {
	  if(no <0 || no>= NUM_SETTINGS) return -1;
	  return settings[no].get_setting(); 
	}

  	public int get_motor_id() {
  		return motor_id;
  	}
  	
    public boolean startSimulation() {

  		int x=0;
  		int y=0;
  		int interval_time=7; 

  		for(x=0;x<NUM_SETTINGS;x++) {
  			settings[x].set_interval(interval_time);
  			settings[x].startSimulation();
  		}

  		for(y=0;y<NUM_SENSORS;y++) {
  			sensors[y].set_interval(interval_time);
  			sensors[y].startSimulation();
  		}
  	  
  	  return true;
    }	
  }
