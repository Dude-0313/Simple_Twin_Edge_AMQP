package com.springTwin.springtwinClient;
import java.io.FileReader;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.opencsv.*;
import org.json.*;

public class twinMotor {

	jetMotor motor;
	
	public enum Mode{
		SIMULATED,
		REAL
	};
	
	public static final String[] jsonKeys = {
	     "TIMESTAMP" ,
	     "MOTOR_NO" ,
	     "CYCLE_NO" ,
	     "SETTING_1" ,
	     "SETTING_2" ,
	     "SETTING_3" ,
	     "SENSOR_1" ,
	     "SENSOR_2" ,
	     "SENSOR_3" ,
	     "SENSOR_4" ,
	     "SENSOR_5" ,
	     "SENSOR_6" ,
	     "SENSOR_7" ,
	     "SENSOR_8" ,
	     "SENSOR_9" ,
	     "SENSOR_10" ,
	     "SENSOR_11" ,
	     "SENSOR_12" ,
	     "SENSOR_13" ,
	     "SENSOR_14" ,
	     "SENSOR_15" ,
	     "SENSOR_16" ,
	     "SENSOR_17" ,
	     "SENSOR_18" ,
	     "SENSOR_19" ,
	     "SENSOR_20" ,
	     "SENSOR_21" 
	};
	
	
	public enum MotorFields {
	     TIMESTAMP ,
	     MOTOR_NO ,
	     CYCLE_NO ,
	     SETTING_1 ,
	     SETTING_2 ,
	     SETTING_3 ,
	     SENSOR_1 ,
	     SENSOR_2 ,
	     SENSOR_3 ,
	     SENSOR_4 ,
	     SENSOR_5 ,
	     SENSOR_6 ,
	     SENSOR_7 ,
	     SENSOR_8 ,
	     SENSOR_9 ,
	     SENSOR_10 ,
	     SENSOR_11 ,
	     SENSOR_12 ,
	     SENSOR_13 ,
	     SENSOR_14 ,
	     SENSOR_15 ,
	     SENSOR_16 ,
	     SENSOR_17 ,
	     SENSOR_18 ,
	     SENSOR_19 ,
	     SENSOR_20 ,
	     SENSOR_21 
	};
	
	private static final int NUM_SETTINGS = 3;
	private static final int NUM_SENSORS = 21;
	
	public int cycle_no;
	private Mode mode = Mode.SIMULATED;
	
	public twinMotor(Mode inMode, int id) {
		motor = new jetMotor(id,NUM_SETTINGS,NUM_SENSORS);
		mode = inMode;
	}
	
	public boolean setup_simulated(String filename) {

		double[][] settings;
		double[][] sensors;
		
		if(filename.length() < 1 ) return false;
		
		try {
			FileReader filereader= new FileReader(filename) ;
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List <String[]> dataDump = csvReader.readAll();
			int num_rows = dataDump.size();

			settings = new double[num_rows][NUM_SETTINGS];
			sensors = new double[num_rows][NUM_SENSORS];
			
			//Read File Data 
			int row_cnt = 0;
				for (String[] row : dataDump) {
					int sensor_cnt = 0;
					int settings_cnt =0;
					int fieldno= 0;
					for (String cell : row) {
						if( fieldno > MotorFields.SETTING_3.ordinal() ) {
							sensors[row_cnt][sensor_cnt++]= Double.parseDouble(cell);
							sensor_cnt = (sensor_cnt) % NUM_SENSORS; 
						}
						if( fieldno > MotorFields.CYCLE_NO.ordinal() && fieldno < MotorFields.SENSOR_1.ordinal() ) {
							settings[row_cnt][settings_cnt++]= Double.parseDouble(cell);
							settings_cnt = (settings_cnt) % NUM_SETTINGS; 
						}
						fieldno++;
					}
					row_cnt++;
				}
				filereader.close();
			} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return motor.load_values(settings,sensors);
	}
	
	public boolean setup_real(String deviceid) {
		
		return false;
	}
	
	public boolean setup_motor(String desc) {
		if(mode == Mode.SIMULATED) 
			return setup_simulated(desc);
		else
			return setup_real(desc);
	}
	
  public double get_sensor_value(int no) {
		if(no <1 || no> NUM_SENSORS) return -1;
		return motor.get_sensor_value(no-1); 
	  }
		
  public double get_setting(int no) {
		  if(no <1 || no> NUM_SETTINGS) return -1;
		  return motor.get_setting(no-1); 
		}	

  public JSONObject get_telemetry() throws JSONException {
	JSONObject	jsonObj = new JSONObject();
	String timestamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	jsonObj.put(jsonKeys[0],timestamp);
	jsonObj.put(jsonKeys[1],motor.get_motor_id());
	jsonObj.put(jsonKeys[2],cycle_no++);
	for(int i=0;i<NUM_SETTINGS;i++) {
		jsonObj.put(jsonKeys[3+i],motor.get_setting(i));
	}
	for(int i=0;i<NUM_SENSORS;i++) {
		jsonObj.put(jsonKeys[3+NUM_SETTINGS+i],motor.get_sensor_value(i));
	}

	cycle_no = cycle_no % Integer.MAX_VALUE;
	
	return jsonObj;
  }
   public int getMotorId() {
	   return motor.get_motor_id();
   }
   
   public boolean startSimulation() {
   
	   return motor.startSimulation();
   }

   

}
