#include <ArduinoJson.h>

#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiAP.h>
#include <ESP8266WiFiGeneric.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266WiFiScan.h>
#include <ESP8266WiFiSTA.h>
#include <ESP8266WiFiType.h>
#include <ESP8266WebServer.h>

//ESP8266WiFiMulti wifiMulti;     // Create an instance of the ESP8266WiFiMulti class, called 'wifiMulti'
ESP8266WebServer server(80);    // Create a webserver object that listens for HTTP request on port 80
void handleRoot();              // function prototypes for HTTP handlers
int numberOfNetworks;
String nrEsp = "3";
char * eSPname = "FLODA eg.3";
String networks;
String host = "http://serwer1727017.home.pl";
int statusArd = -1;
bool httpGet();
void handleLogin();

// INCOMING INFO
String temperature = "0";
String humidity = "0";
String ph = "0";
String soil = "0";
String sun = "0";
int wifion = 0;

String awaryjnewifiSSID = "";
String awaryjnewifiPASS = "";
String haslo = "dowolnosc";
StaticJsonBuffer<512> jsonbuffer;
void setup() {

  Serial.begin(115200);
  wifion = 1;
  WiFi.mode(WIFI_AP_STA);
  //dodac opcje wczesniejsze
  if (WiFi.status() != WL_CONNECTED) {
    WiFi.softAP(eSPname);
    //WiFi.enableAP(1);
    //Only Access point
    //WiFi.disconnect();
    ESP.eraseConfig();

  }
  Serial.println("ESP is starting");
  String pass = "1";


  server.on("/", HTTP_GET, handleRoot);
  //server.on("/sd", HTTP_GET, handleSuc);
  //server.on("/ds", HTTP_GET, handleFail);
  server.on("/login", HTTP_POST, handleLogin); // Call the 'handleLogin' function when a POST request is made to URI "/login"

  server.begin();                           // Actually start the server
  Serial.println("HTTP server started");

}

void loop() {

  if (WiFi.status() != WL_CONNECTED && wifion == 0) {
    wifion = 1;
  }
  if (WiFi.status() != WL_CONNECTED) {
    server.handleClient();
    WiFi.enableAP(1);

  } else {

  WiFi.enableAP(0);

    switch (statusArd) {


      case -1: { //checking and apply last settings

          if (!httpGet()) {
            Serial.println("nie pobrano danych");
          } else {
            Serial.println("dalo rade w case -1");
            statusArd++;
          }


        } break;
      case 0: //inicjacja

        while ((char)Serial.read() != '$') {
          Serial.write("*");
          delay(1000);
        }
        Serial.println();
        Serial.println("-------------------");
        Serial.println("Polaczono z arduino");
        Serial.println("-------------------");
        statusArd = 1;
        break;

      case 1: { //wymiana podstawowych informacji 1.SSID

          statusArd++;
          delay(10000);

          wifion = 0;
        } break;

      case 2: {//co jakis czas wznawianie pobierania informacji z arduino i wstawianie do zmiennych
          do {
            while ((char)Serial.read() != '*') {
              if (WiFi.status() != WL_CONNECTED) break;
              delay(10000);
              Serial.write('!');
            }
            Serial.write('9');
            temperature = Serial.readStringUntil('@');
            Serial.write('!');
            Serial.println(temperature);

            humidity = Serial.readStringUntil('@');
            Serial.write('!');
            Serial.println(humidity);
            ph = Serial.readStringUntil('@');
            Serial.write('!');
            Serial.println(ph);
            soil = Serial.readStringUntil('@');
            Serial.write('!');
            Serial.println(soil);
            sun = Serial.readStringUntil('@');
            Serial.write('!');
            Serial.println(sun);

            if (!httppost()) {
              Serial.println("FATAL ERROR");
            }
          } while (WiFi.status() == WL_CONNECTED);
          delay(1000);
        } break;


    }
  }

}










// strony internetowe
void handleRoot() {
  numberOfNetworks = WiFi.scanNetworks();
  networks = "<html> <head> <title>Setup Floda</title></head><body bgcolor='#222222' style='size:50px; color: #CCCCCC; face:Arial; margin:auto'>";
  networks += "<center><font size='50' color='#CCCCCC' face='Arial'><br><h1>";
  networks += eSPname;
  networks += "</h1></font>";
  networks += "<form action=\"/login\" method=\"POST\"><select name=\"wifi\" style='color: #CCCCCC; background-color: #AAAAAA; font-size: 300%; min-width:90%; max-width:90%; border-style: none'>";
  networks += "<option value=''>Wybierz SSID</option>";
  for (int i = 0; i < numberOfNetworks; i++) {
    networks += "<option value=";
    networks += WiFi.SSID(i);
    networks += "> ";
    networks += WiFi.SSID(i);
    networks += "</option>";
  }
  networks += "</select><br><br><input type=\"password\" placeholder='&nbsp;WIFI PASSWORD' name=\"password\" style='color: #CCCCCC; background-color: #AAAAAA; font-size: 300%; min-width:90%; max-width:90%; border-style: none'><br><br><input type=\"submit\" value=\"DONE\" style='color: #CCCCCC; background-color: #219734; font-size: 300%; min-width:90%; max-width:90%; border-style: none'></form>";
  /*networks += "<table align=\"center\" width = 'auto' border = \"1px solid black\">";
    networks += "<th>";
    networks += "WiFi name";
    networks += "</th>";
    networks += "<th>";
    networks += "WiFi strengh";
    networks += "</th>";
    networks += "<th>";
    networks += "Password?";
    networks += "</th>";
    for (int i = 0; i < numberOfNetworks; i++) {
    networks += "<tr>";
    networks += "<td>";
    networks += WiFi.SSID(i);
    networks += "</td>";
    networks += "<td>";
    networks += WiFi.RSSI(i);
    networks += "</td>";
    networks += "<td>";
    networks += (WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "Yes";
    networks += "</td>";
    networks += "</tr>";
    }
    networks += "</table>";*/
  networks += "</body></html>";
  server.send(200, "text/html", networks);
}
void handleLogin() {
  Serial.println(server.arg("wifi")); //problem z naza wtypu "siec rybaka"
  Serial.println(server.arg("password"));
  WiFi.begin(server.arg("wifi").c_str(), server.arg("password").c_str());
  int i = 0;
  while (WiFi.status() != WL_CONNECTED) { // Wait for the Wi-Fi to connect
    delay(1000);
    Serial.print(++i); Serial.print(' ');
    if (i > 7) {
      server.send(200, "text/html", "<p>Login unsuccessful, try again</p><a href=\"/\">Try Again!</a>");

      Serial.println("Connection failed");
      handleRoot();
      break;
    }
  }
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println(WiFi.localIP());// DHCP Hostname (useful for finding device for static lease)
    String output;
    output = "Polaczono \n";
    output += (String)WiFi.localIP();
    output += "\n";
    output += WiFi.SSID();
    server.send(200, "text/html", output);
    delay(500);
    WiFi.enableAP(0);


  }
}


bool httppost() {
  HTTPClient client;

  //http.clear();
  String g = "/2ti/floda/esp.php?nr=" + nrEsp + "&temperature=" + temperature + "&humidity=" + humidity + "&ph=" + ph + "&soil=" + soil + "&sun=" + sun + "&haslo=" + haslo;
  String temp = host + g;
  client.begin(temp);
  Serial.println(temp);
  int HttpCode = client.GET();            //Send the request

  Serial.println(HttpCode);
  if (HttpCode > 0)
  {
    String payload = client.getString();    //Get the response payload
  } else
    return false;

  client.end();  //Close connection
  return true;
  delay(5000);

}

bool httpGet() {
  HTTPClient client;

  client.begin(host + "/2ti/floda/espset.php?nr=" + nrEsp + "&haslo=" + haslo); //tutaj musisz w php polaczyc 2 tabele aby to sprawdzic
  int HttpCode = client.GET();            //Send the request


  if (HttpCode > 0)
  {
    String payload = client.getString();    //Get the response payload
    JsonObject& root = jsonbuffer.parseObject(payload);
    //char * ssidd = root["arg11"];
    // char * paswdd = root["arg12"]; //nie dziala

    /*  if (ssidd != "")
      {
    	Serial.println("httpGet:");
      //	Serial.println(ssidd);
      //	Serial.println(paswdd);
    	Serial.println(payload);
        // wifiMulti.addAP(ssidd, paswdd);
      }*/
  } else
    return false;

  client.end();  //Close connection


  return true;
}

