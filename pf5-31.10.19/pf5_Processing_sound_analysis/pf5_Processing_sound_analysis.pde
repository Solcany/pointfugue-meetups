import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

import oscP5.*;
import netP5.*;

Minim minim;
AudioInput in;
FFT fft;

// AudioPlayer player;

OscP5 oscP5;
NetAddress remote;

int bufferSize = 512;

float [] spectrum = new float[bufferSize];
float volume;

void setup() {
 size( 10, 10 );
 frameRate(30);
 background(255);
 frameRate(30);

 remote = new NetAddress("127.0.0.1", 7000);
 
 minim = new Minim(this);
 in = minim.getLineIn(Minim.STEREO, bufferSize);

 fft = new FFT(in.bufferSize(), in.sampleRate() );
 
//player = minim.loadFile("name-of-your-mp3.mp3", bufferSize);
//player.play();
  
}

void draw() {
  fft.forward(in.mix);
  spectrum = fft.getSpectrumImaginary();
  volume = in.mix.level();
  
  OscMessage spectrum_msg = new OscMessage("/processing/spectrum");
  spectrum_msg.add(spectrum);
  oscP5.flush(spectrum_msg, remote);

  OscMessage volume_msg = new OscMessage("/processing/volume");
  volume_msg.add(volume);
  oscP5.flush(volume_msg, remote);
}

void keyPressed() {
  // player.pause();
}
