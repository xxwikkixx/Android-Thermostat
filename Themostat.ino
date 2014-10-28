int tempPin = 6;
int tempF;
int tempC;


//Relays to control the wires
int REfan = 3;
int REac = 4;
int REheat = 5;

void setup() {
  Serial.begin(9600);
  
  //relay pins output
  pinMode(REfan, OUTPUT);
  pinMode(REac, OUTPUT);
  pinMode(REheat, OUTPUT);
  
}

void loop() {
  
//set methods in here to run based on the commands sent from android
  if(Serial.available()){
    int input = Serial.read();
    switch(input){
      case 't': 
        temperature(); 
        break;
        
      case '1': 
        fanOn();
        //Serial.println("Fan On"); 
        break;
        
      case '2':
        fanOff();
        //Serial.println("Fan Off"); 
        break;
        
      case '3':
        ac();
        //Serial.println("AC On"); 
        break;
        
      case '4':
        acOff();
        //Serial.println("AC Off"); 
        break;
        
      case '5':
        heat();
        //Serial.println("Heat On"); 
        break;
        
      case '6':
        heatOff();
        //Serial.println("Heat Off"); 
        break;
        
      default:
        temperature();
    }    
  } 
}

void temperature(){
  tempC = analogRead(tempPin);
  tempC = (5.0 * tempC * 100.0)/1024.0; //convert the analog data to temperature
  tempF = ((tempC*9)/5) + 32;
  //Serial.print(tempF);
  
  int avgTemp;
  for(int i=1; i < 1000; i++){
    avgTemp += ((tempF - avgTemp)/ i);   
  }  
  Serial.print(avgTemp);
}

void fanOn(){
  digitalWrite(REfan, HIGH);
}
void fanOff(){
  digitalWrite(REfan, LOW);
}

void ac(){
  digitalWrite(REac, HIGH);
}
void acOff(){
  digitalWrite(REac, LOW);
}

void heat(){
  digitalWrite(REheat, HIGH);
}
void heatOff(){
  digitalWrite(REheat, LOW);
}
