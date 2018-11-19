// CUBE MUSIC
// IHC
// UFPE Cin
// October 25, 2018

#include<Wire.h>
#include <SoftwareSerial.h>

const int MPU_addr=0x68;  // I2C address of the MPU-6050
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;
int16_t AcXo,AcYo,AcZo;

// Open serial and begin bluetooth
SoftwareSerial ble(10, 11); // RX, TX

//ble reader
byte incomingByte = 0;
void setup() {
  // MPU-6050
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);
  
  // Open serial port
  Serial.begin(9600);

  // begin bluetooth serial port communication
  ble.begin(9600);

  //Vibrador
  pinMode(2, OUTPUT);
}

// Cube music loop
void loop(){
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(MPU_addr,14,true);  // request a total of 14 registers
  AcX=Wire.read()<<8|Wire.read();  // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)    
  AcY=Wire.read()<<8|Wire.read();  // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
  AcZ=Wire.read()<<8|Wire.read();  // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
  Tmp=Wire.read()<<8|Wire.read();  // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
  GyX=Wire.read()<<8|Wire.read();  // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
  GyY=Wire.read()<<8|Wire.read();  // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
  GyZ=Wire.read()<<8|Wire.read();  // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)

  if (ble.available() > 0) {
      // read the incoming byte:
      incomingByte = ble.read();
      Serial.println(incomingByte);
      if (incomingByte == 10){
        digitalWrite(2, HIGH);   // turn the motor on (HIGH is the voltage level)
        delay(500);                       // wait for a second
        digitalWrite(2, LOW);    // turn the motor off by making the voltage LOW
        delay(200);
        digitalWrite(2, HIGH);   // turn the motor on (HIGH is the voltage level)
        delay(700);                       // wait for a second
        digitalWrite(2, LOW);    // turn the motor off by making the voltage LOW
        incomingByte = 0;
      }
  }
  //re-scale
  AcX=AcX/2500;
  AcY=AcY/2500;
  AcZ=AcZ/2500;
  
  //face detect 1,2,...,6
  if (abs(AcX-0)<2  && abs(AcY-0)<2 && abs(AcZ+6)<2) {
    Serial.println(" quadrado ");
    ble.write("quadrado");
  }
  else if (abs(AcX-0)<2  && abs(AcY+6)<2 && abs(AcZ-0)<2) {
    Serial.println(" triangulo ");
    ble.write("triangulo");
  }
  else if (abs(AcX-0)<2  && abs(AcY-0)<2 && abs(AcZ-6)<2) {
    Serial.println(" circulo ");
    ble.write("circulo");
  }
  else if (abs(AcX-0)<2  && abs(AcY-6)<2 && abs(AcZ-0)<2) {
    Serial.println(" hexagono ");
    ble.write("hexagono");
  }
  else if (abs(AcX-6)<2  && abs(AcY-0)<2 && abs(AcZ-0)<2) {
    Serial.println(" estrela ");
    ble.write("estrela");
  }
  else if (abs(AcX+6)<2  && abs(AcY-0)<2 && abs(AcZ-0)<2) {
    Serial.println(" quadrante ");
    ble.write("quadrante");
  }

  //other cases
  else{
    //motion detect X,Y,Z
    if (abs(AcX-AcXo)>2  && abs(AcY-AcYo)<=2 && abs(AcZ-AcZo)<=2) {
      Serial.println(" move-x ");
      ble.write("movex\n");
    }
    else if (abs(AcX-AcXo)<=2  && abs(AcY-AcYo)>2 && abs(AcZ-AcZo)<=2) {
      Serial.println(" move-y ");
      ble.write("movey\n");
    }
    else if (abs(AcX-AcXo)<=2  && abs(AcY-AcYo)<=2 && abs(AcZ-AcZo)>2) {
      Serial.println(" move-z ");
      ble.write("movez\n");
    }
    else {
      Serial.print(" AcX = "); Serial.print(AcX);
      Serial.print(" | AcY = "); Serial.print(AcY);
      Serial.print(" | AcZ = "); Serial.print(AcZ);
      Serial.print(" | Tmp = "); Serial.print(Tmp/340.00+36.53);  //equation for temperature in degrees C from datasheet
      Serial.print(" | GyX = "); Serial.print(GyX);
      Serial.print(" | GyY = "); Serial.print(GyY);
      Serial.print(" | GyZ = "); Serial.println(GyZ);
    }
  }
  delay(333);

  // memory
  AcXo=AcX;
  AcYo=AcY;
  AcZo=AcZ;
}
