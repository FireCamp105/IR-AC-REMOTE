#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>
#include <IRremoteESP8266.h>
#include <IRsend.h>  
IRsend irsend(19); 
uint64_t code = 0x88 ; //adresse dima fixe


 
/* INFRA ROUGE /!\
#include <IRremoteESP8266.h>
#include <IRsend.h>
*/

//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "reborn"
#define WIFI_PASSWORD "iwasreborn"

// Insert Firebase project API Key
#define API_KEY "AIzaSyD3Yv7CYZ_kXwgMn5qf8StR1QDnCbGGqqE"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://esp32bs-default-rtdb.europe-west1.firebasedatabase.app/" 

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;
unsigned long sendDataPrevMillis = 0,sendDataPrevMillis2 = 0;
int count = 0;
bool signupOK = false;
String LAUNCH;
int W,X,Y,Z,ALARME;
bool AL;
void setup(){
  analogReadResolution(12);
  pinMode(18,INPUT);
  irsend.begin();
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
  
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop(){
  int analogValue = analogRead(35)* (3300.0 / 4096.0)/10;
  printf("ADC analog value = %d\n",analogValue);
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 500 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    /* modification here vvvvvvvvv /!\ */
    Firebase.RTDB.getString(&fbdo, "LAUNCH", &LAUNCH);
    Firebase.RTDB.getInt(&fbdo, "W", &W);
    Firebase.RTDB.getInt(&fbdo, "X", &X);
    Firebase.RTDB.getInt(&fbdo, "Y", &Y);
    Firebase.RTDB.getInt(&fbdo, "Z", &Z);
    Serial.println(LAUNCH);
    if(LAUNCH =="SEND")
    {
      for (int i = 0; i < 4; i++) {
      code = 0x88;
      code =  (code<<4)+ W ; //0x880
      code = (code<<4) + X ; //0x8808
      code =  (code<<4) + Y ; // 0x88080
      code = (code<<4)+ Z ; //  0x880805 
      code = (code <<4) + ((W+X+Y+Z)%16) ;
      Serial.println(code);
      irsend.sendLG(code, 28);// Send a 28-bit LG message
      delay(100);
      
      }
      Firebase.RTDB.setString(&fbdo, "LAUNCH", "OKAY");
      
    }
      
  }
    if (digitalRead(18)==HIGH){
       AL=true;
      }   
    if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis2 > 5000 || sendDataPrevMillis2 == 0)){
      sendDataPrevMillis2 = millis();
      Firebase.RTDB.setString(&fbdo, "LAUNCH", "OKAY");
      Firebase.RTDB.setInt(&fbdo, "ROOMTEMP",analogValue);
      if (AL){
      Firebase.RTDB.getInt(&fbdo, "alarm", &ALARME);
      Firebase.RTDB.setInt(&fbdo,"alarm", ALARME + 1 );
      AL=false;
      }
      }
}