// This #include statement was automatically added by the Particle IDE.
#include <ledmatrix-max7219-max7221.h>

/*
 * Project Microcontroller
 * Description: Microcontroller for the Home-Security System
 * Author: Zain ALabdeen Mahfoud
 * Date: 05 - 01 - 2021
 */

LEDMatrix *led;
int bitmapWidth = 8;
// cycle counter (incremented after each heart drawing)
int counter = 0;
String text = "ZAIN";
int textLength = text.length();
// default position of the text is outside and then scrolls left
int textX = bitmapWidth;
int fontWidth = 5, space = 1;

int mic = D0;

/* Include the RFID library */
/* SEE RFID.h for selecting Hardware or Software SPI modes */
#include "RFID.h"

/* Define the pins used for the SS (SDA) and RST (reset) pins for BOTH hardware and software SPI */
/* Change as required */
#define SS_PIN      A2      // Same pin used as hardware SPI (SS)
#define RST_PIN     D7

/* Define the pins used for the DATA OUT (MOSI), DATA IN (MISO) and CLOCK (SCK) pins for SOFTWARE SPI ONLY */
/* Change as required and may be same as hardware SPI as listed in comments */
#define MOSI_PIN    D5      // hardware SPI: A5
#define MISO_PIN    D6      //     "     " : A4
#define SCK_PIN     D4      //     "     " : A3

/* Create an instance of the RFID library */
#if defined(_USE_SOFT_SPI_)
    RFID RC522(SS_PIN, RST_PIN, MOSI_PIN, MISO_PIN, SCK_PIN);    // Software SPI
#else
    RFID RC522(SS_PIN, RST_PIN);                                 // Hardware SPI
#endif

int PIR_SENSOR1_OUTPUT_PIN = 1;	/* PIR sensor 1 O/P pin */
int PIR_SENSOR2_OUTPUT_PIN = 2;	/* PIR sensor 2 O/P pin */
int PIR_SENSOR3_OUTPUT_PIN = 8;	/* PIR sensor 3 O/P pin */
int PIR_SENSOR4_OUTPUT_PIN = 13;	/* PIR sensor 4 O/P pin */
int warm_up1, warm_up2, warm_up3, warm_up4;

// setup() runs once, when the device is first turned on.
void setup() {
  // Put initialization like pinMode and begin functions here.
  pinMode(mic, OUTPUT);
  // setup pins and library
  // 1 display per row, 1 display per column
  // optional pin settings - default: CLK = A0, CS = A1, D_OUT = A2
  // (pin settings is independent on HW SPI)
  led = new LEDMatrix(1, 1, D9, D10, D12);
  // > add every matrix in the order in which they have been connected <
  // the first matrix in a row, the first matrix in a column
  // vertical orientation (-90°) and no mirroring - last three args optional
  led->addMatrix(0, 0, 90, false, false);

  pinMode(PIR_SENSOR1_OUTPUT_PIN, INPUT);
  pinMode(PIR_SENSOR2_OUTPUT_PIN, INPUT);
  pinMode(PIR_SENSOR3_OUTPUT_PIN, INPUT);
  pinMode(PIR_SENSOR4_OUTPUT_PIN, INPUT);
  delay(20000);	/* Power On Warm Up Delay */

  Serial.begin(115200);
  
#if !defined(_USE_SOFT_SPI_)
  /* Enable the HW SPI interface */
  SPI.setDataMode(SPI_MODE0);
  SPI.setBitOrder(MSBFIRST);
  SPI.setClockDivider(SPI_CLOCK_DIV8);
  SPI.begin();
#endif

  /* Initialise the RFID reader */
  RC522.init();

}

// loop() runs over and over again, as quickly as it can execute.
void loop() {
  // The core of your code will likely live here.
  readRFIDKarte();
  toggleSpeaker(true);
  delay(1000);
  toggleSpeaker(false);
  startAnimation();
  motionDetected();
}

void toggleSpeaker(bool status) {
    if(status){
        digitalWrite(mic, HIGH);
    }else{
        digitalWrite(mic, LOW);
    }
}

// draw text
void drawText(String s, int x)
{
  int y = 0;
  for(int i = 0; i < s.length(); i++) {
    // Adafruit_GFX method
    led->drawChar(x + i*(fontWidth+space), y, s.charAt(i), true, false, 1);
  }
}

// draw symbol of heart
void drawHeart() {
  int x = 1, y = 2;
  // methods from Adafruit_GFX
  led->fillCircle(x+1, y+1, 1, true);
  led->fillCircle(x+5, y+1, 1, true);
  led->fillTriangle(x+0, y+1, x+6, y+1, x+3, y+4, true);
  // direct access to the main bitmap (instance of LEDMatrix::Bitmap)
  led->bitmap->setPixel(x+3, y+1, false);
}

void startAnimation(){
      // do only two cycles of animations
  if(led != NULL && counter < 2) {
    drawText(text, textX--);
    // text animation is ending when the whole text is outside the bitmap
    if(textX < textLength*(fontWidth+space)*(-1)) {
      // set default text position
      textX = bitmapWidth;
      drawHeart();
      // show heart
      led->flush();
      delay(1000);
      // turn all pixels off (takes effect after led->flush())
      led->fillScreen(false);
      counter++;
    }
    // draw text
    led->flush();
    delay(150);
  }
  // animations end
  else if (led != NULL) {
    // shutdown all displays
    led->shutdown(true);
    // free memory
    delete led;
    led = NULL;
  }
}

void readRFIDKarte(){
    /* Temporary loop counter */
  uint8_t i;

  /* Has a card been detected? */
  if (RC522.isCard())
  {
    /* If so then get its serial number */
    RC522.readCardSerial();

    Serial.println("Card detected:");

    /* Output the serial number to the UART */
    for(i = 0; i <= 4; i++)
    {
      Serial.print(RC522.serNum[i],HEX);
      Serial.print(" ");
    }
    Serial.println();
  }
  else
      Serial.println("Card NOT detected:");
      
  delay(1000);
}

void motionDetected(){
    int32_t sensor1_output = digitalRead(PIR_SENSOR1_OUTPUT_PIN);
    int32_t sensor2_output = digitalRead(PIR_SENSOR2_OUTPUT_PIN);
    int32_t sensor3_output = digitalRead(PIR_SENSOR3_OUTPUT_PIN);
    int32_t sensor4_output = digitalRead(PIR_SENSOR4_OUTPUT_PIN);

    if(sensor1_output == LOW )
    {
        if(warm_up1 == 1 ){
         Serial.println("Warming Up\n\n");
         warm_up1 = 0;
         delay(2000);
         }
         Serial.println("No object in sight\n\n");
         delay(1000);
     }else{
             Serial.println("Object detected\n\n");    
             warm_up1 = 1;
             delay(1000);
             } 

    if(sensor2_output == LOW )
    {
        if(warm_up2 == 1 ){
         Serial.println("Warming Up\n\n");
         warm_up2 = 0;
         delay(2000);
         }
         Serial.println("No object in sight\n\n");
         delay(1000);
     }else{
             Serial.println("Object detected\n\n");    
             warm_up2 = 1;
             delay(1000);
             } 

    if(sensor3_output == LOW )
    {
        if(warm_up3 == 1 ){
         Serial.println("Warming Up\n\n");
         warm_up3 = 0;
         delay(2000);
         }
         Serial.println("No object in sight\n\n");
         delay(1000);
     }else{
             Serial.println("Object detected\n\n");    
             warm_up3 = 1;
             delay(1000);
             } 

    if(sensor4_output == LOW )
    {
        if(warm_up4 == 1 ){
         Serial.println("Warming Up\n\n");
         warm_up4 = 0;
         delay(2000);
         }
         Serial.println("No object in sight\n\n");
         delay(1000);
     }else{
             Serial.println("Object detected\n\n");    
             warm_up4 = 1;
             delay(1000);
             } 
}