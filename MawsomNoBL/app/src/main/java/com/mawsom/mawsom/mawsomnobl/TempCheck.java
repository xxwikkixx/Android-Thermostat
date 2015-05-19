package com.mawsom.mawsom.mawsomnobl;

/*
 * This Activities does the temperature check from the arduino data stream and returns
 * values to the main activity to control the thermostat
 * 
 * 
 * Also includes the Energy saving/checking algorithm used. 
 * 
 * 
 * File IO with the temperature data to the disk space save it in this format 
 * Time | temperature
 * 1-2  | 70
 * find the average of the temp from every hour and save it to auto temp "self learning"
 * 
 * 
 * When away mode: 
 * take the min temp and max temp from the user input and based on that use the
 * same average temp algorithm used for normal methods. 
 */

import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;

public class TempCheck {

    public void ACTemp(int TargetTemp, int RoomTemp, int OutsideTemp){
        /*
        ** if AC is on this function will do the AC temperature
         */
        if(RoomTemp != TargetTemp){
            //AC ON COMMAND
        }
        if(RoomTemp > TargetTemp){
            //AC ON COMMMAND
        }
        if(RoomTemp < TargetTemp){
            //AC OFF COMMAND
        }
        if(RoomTemp == TargetTemp){
            //AC OFF COMMAND
        }

    }

    public void HeatTemp(int TargetTemp, int RoomTemp, int OutsideTemp){
        /*
        ** if Heat is on this function will do the Heat function
         */
        if(RoomTemp != TargetTemp){
            //HEAT ON
        }
        if(RoomTemp > TargetTemp){
            //HEAT OFF
        }
        if(RoomTemp < TargetTemp){
            //HEAT ON
        }
        if(RoomTemp == TargetTemp){
            //HEAT OFF
        }
    }

    /*
    Set the temperature based on the history of the user using data from the RecordTemp function.
        Compare the temperature with the outside temperature and determine what an optimum temperature would be.
            if temperature:
            Outside: 80 (on that temperature date what was the temperature set inside when the outside was 80)
            Inside: 65, 63, 64, 62, 65,
            TargetTemp: Find the mean average of the inside temperature and then set that as the target temp.
            Season:
     */
    public void AutoTemp(int TargetTemp, int RoomTemp, int OutsideTemp){
        String season; //season of the year to determine AC or heat

    }

    public void AwayTemp(int TargetTemp, int RoomTemp, int OutsideTemp){
        String seasonAway;
        boolean winter;

    }

    /*
    ** Record: Month | Day | Date | Time | RoomTemp | TargetTemp | OutsideTemp | InHumid | OutHumid
    *   Record every hour for everyday for a month
    *       average the data with that day and save it for that one month
    *           check for abnormal data such as not being home for long periods of time or being on a vacation
    *
    ** Create Folder for each Month and inside for Each Day Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday
    ** Create CSV files for the data from step 1
     */
    public void RecordTemp(int TargetTemp, int RoomTemp, int OutsideTemp, int OutsideHumid, int InsideHumid){
        String Date;
        String Time;
    }

}
