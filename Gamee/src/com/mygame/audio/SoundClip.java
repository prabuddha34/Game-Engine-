package com.mygame.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundClip {

    private Clip clip;
    private FloatControl gainControl;

    public SoundClip(String path)
            throws UnsupportedAudioFileException,
            IOException,
            LineUnavailableException {

        InputStream audioSrc =
                SoundClip.class.getResourceAsStream(path);

        if (audioSrc == null) {
            throw new IOException("Sound file not found: " + path);
        }

        BufferedInputStream bufferedStream =
                new BufferedInputStream(audioSrc);

        AudioInputStream ais = AudioSystem.getAudioInputStream(
                getClass().getResource("/res/ass.wav")
        );

        AudioFormat baseFormat = ais.getFormat();

        AudioFormat decodeFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
        );

        AudioInputStream dis =
                AudioSystem.getAudioInputStream(decodeFormat, ais);

        clip = AudioSystem.getClip();
        clip.open(dis);

        gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    // ---------------- PLAY ----------------
    public void play() {

        if (clip == null) return;

        stop();

        clip.setFramePosition(0);
        clip.start();
    }

    // ---------------- STOP ----------------
    public void stop() {

        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // ---------------- CLOSE ----------------
    public void close() {

        if (clip == null) return;

        stop();
        clip.drain();
        clip.close();
    }

    // ---------------- LOOP ----------------
    public void loop() {

        if (clip == null) return;

        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // ---------------- VOLUME ----------------
    public void setVolume(float value) {

        if (gainControl == null) return;

        // clamp between 0 and 1
        value = Math.max(0.0f, Math.min(1.0f, value));

        float min = gainControl.getMinimum();
        float max = gainControl.getMaximum();

        float gain = (max - min) * value + min;

        gainControl.setValue(gain);
    }

    // ---------------- IS RUNNING ----------------
    public boolean isRunning() {

        return clip != null && clip.isRunning();
    }
}