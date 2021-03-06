package com.example.paranoid_effects;

import java.io.IOException;

import android.media.audiofx.EnvironmentalReverb;
import android.util.Log;

import com.example.paranoid_base.ParanoidEffect;

public class AcceleroFoot extends ParanoidEffect {
	private EnvironmentalReverb reverb;

	public AcceleroFoot() {
		super("AcceleroFootRecording.pcm");
		int decayTime = 5000;
		short density = 500;
		short diffusion = 500;
		short roomLevel = 0;
		short reverbLevel = 500;
		short reflectionsDelay = 100;
		short reflectionsLevel = 100;
		short reverbDelay = 0;

		reverb = new EnvironmentalReverb(0, 0);
		reverb.setDecayTime(decayTime);
		reverb.setDensity(density);
		reverb.setDiffusion(diffusion);
		reverb.setReverbLevel(reverbLevel);
		reverb.setRoomLevel(roomLevel);
		reverb.setReflectionsDelay(reflectionsDelay);
		reverb.setReflectionsLevel(reflectionsLevel);
		reverb.setReverbDelay(reverbDelay);
	}

	public void AcceleroFootLoop() {

		try {
			reverb.setEnabled(true);
			paranoidTrack.attachAuxEffect(reverb.getId());
			paranoidTrack.setAuxEffectSendLevel(100.0f);

			paranoidTrack.play();
			int i = 0;
			int o = 0;
			double wetSample;
			short convertedSample;
			float leftVolumeLow = (float) 0.25;
			float rightVolumeLow = (float) 0.25;
			float leftVolumeHigh = (float) 1.0;
			float rightVolumeHigh = (float) 1.0;
			double volume = 10.0;
			paranoidRecord.startRecording();
			while (recording) {
				o = paranoidRecord.read(audioData, 0, minBufferSize);
				for (i = 0; i < o; i++) {
					if (axisY < 3.0) {
						reverb.setEnabled(true);
						volume = 10.0;
						paranoidTrack.setStereoVolume(leftVolumeLow,
								rightVolumeLow);
					}
					if (axisY > 3.0 && axisY < 5.0) {
						reverb.setEnabled(false);
						volume = 45.0;
						paranoidTrack.setStereoVolume(leftVolumeLow,
								rightVolumeLow);
					}
					if (axisY > 5.0 && axisY < 7.0) {
						reverb.setEnabled(false);
						volume = 10.0;
						paranoidTrack.setStereoVolume(leftVolumeHigh,
								rightVolumeHigh);
					}
					wetSample = (short) audioData[i] * volume;
					if (wetSample < -32767.0f) {
						wetSample = -32767.0f;
					}
					if (wetSample > 32767.0f) {
						wetSample = 32767.0f;
					}
					convertedSample = (short) wetSample;
					audioData[i] = convertedSample;
					dataOutputStream.writeShort(audioData[i]);
					dataOutputStream.writeShort(audioData[i]);
				}
				paranoidTrack.write(audioData, 0, o);
				if (i == minBufferSize) {
					i = 0;
				}
			}
			turnOffEffectLoop();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("Erro", "Falha na inicializacao dos componentes do loop");
		}
	}

	public void enableReverb() {
		reverb.setEnabled(true);
	}

	public void disableReverb() {
		reverb.setEnabled(false);
	}
}
