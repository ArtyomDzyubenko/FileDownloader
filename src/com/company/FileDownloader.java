package com.company;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader extends Thread {
    private static final int BUFFER_SIZE = 1024;

    private String URL;
    private String Path;
    private String progress;
    private boolean completed = false;

    public void download() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(Path)) {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("GET");

            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());

            int size = connection.getContentLength();
            int currentSize = 0;
            byte[] dataBuffer = new byte[BUFFER_SIZE];
            int bytesReadCount;
            String fileName = URL.substring(URL.lastIndexOf('/') + 1);

            while ((bytesReadCount = inputStream.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesReadCount);

                currentSize += bytesReadCount;
                progress = "| " + fileName + " completed: " + currentSize + " " + " size: " + size + " |";
                //System.out.println(progress);


            }

            inputStream.close();
            connection.disconnect();
            completed = true;
        } catch (IOException e) {
            completed = true;
            e.printStackTrace();
        }
    }

    public String getProgress() {
        return progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setPath(String path) {
        Path = path;
    }

    @Override
    public void run() {
        download();
    }
}
