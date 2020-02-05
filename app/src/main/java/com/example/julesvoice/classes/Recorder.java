package com.example.julesvoice.classes;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Recorder
{
    private static final int BITRATE = 16;
    private static String OUTPUT_FOLDER = Environment.getExternalStorageDirectory() + "/VoiceRecognition";
    private static final String TEMP_FILE = OUTPUT_FOLDER + "/temp.raw";
    public static final String OUTPUT_FILE = OUTPUT_FOLDER + "/test.wav";
    private static final int SAMPLERATE = 16000;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    short[] audioData;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private Context context;


    public Recorder(Context cont)
    {
        bufferSize = AudioRecord.getMinBufferSize(SAMPLERATE, CHANNEL, ENCODING) * 3;

        audioData = new short[bufferSize];

        context = cont;

        createInternalFolder();
    }

    private void createInternalFolder()
    {
        File file = new File(OUTPUT_FOLDER);
        if (!file.exists())
            file.mkdir();
    }

    private void deleteTempFile()
    {
        File tempFile = new File(TEMP_FILE);

        if (tempFile.exists())
            tempFile.delete();
    }

    public void startRecording()
    {
        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLERATE, CHANNEL, ENCODING, bufferSize);

        if (recorder.getState() == 1)
            recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                convertByteToAudioFile();
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }

    private void convertByteToAudioFile()
    {
        byte data[] = new byte[bufferSize];
        deleteTempFile();
        FileOutputStream os = null;

        try
        {
            os = new FileOutputStream(TEMP_FILE);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        int read = 0;

        if (null != os)
        {
            while (isRecording)
            {
                read = recorder.read(data, 0, bufferSize);

                if (AudioRecord.ERROR_INVALID_OPERATION != read)
                {
                    try
                    {
                        os.write(data);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            try
            {
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording(PingAndInternetListener cb)
    {
        if(null != recorder)
        {
            isRecording = false;

            if (recorder.getState() == 1)
                recorder.stop();

            recorder.release();

            recorder = null;
            recordingThread = null;
        }

        convertTempAudioToWavAudio(TEMP_FILE, OUTPUT_FILE);
        deleteTempFile();
        speechToText(cb);
    }

    private void convertTempAudioToWavAudio(String temp, String output)
    {
        FileInputStream in = null;
        FileOutputStream out = null;
        int channel = 1;
        long totalAudioLen = 0,
                totalDataLen = totalAudioLen + 36,
                byteRate = BITRATE * SAMPLERATE * channel / 8;
        byte[] data = new byte[bufferSize];

        try
        {
            in = new FileInputStream(temp);
            out = new FileOutputStream(output);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, SAMPLERATE, channel, byteRate);

            while (in.read(data) != -1)
                out.write(data);

            in.close();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException
    {
        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (16 /8); // block align
        header[33] = 0;
        header[34] = BITRATE; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    private void speechToText(PingAndInternetListener cb)
    {
        // https://stackoverflow.com/questions/21601278/how-to-convert-a-file-to-base-64-like-pdf-txt-in-android

        File files = new File(OUTPUT_FILE);

        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes, 0);

        ArrayList<ArgumentRequest> listeArguments = new ArrayList<>();
        listeArguments.add(new ArgumentRequest("wav", encoded));

        ServerRequest requestSpeechToText = new ServerRequest(context, cb);
        requestSpeechToText.getData(listeArguments, "http://pedago.univ-avignon.fr:3012/speechToText");
    }

    public boolean getIsRecording()
    {
        return isRecording;
    }
}
