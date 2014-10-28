package com.example.thermostat;

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


public class TempCheck {

}
