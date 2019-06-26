package com.company;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader extends Thread {
    private static final int BUFFER_SIZE = 1024;

    private String URL;
    private String filePath;
    private StringBuilder progress;
    private boolean completed;
    private HttpURLConnection connection;

    private FileDownloader() {

    }

    public FileDownloader(String URL, String filePath) {
        this.URL = URL;
        this.filePath = filePath;
        progress = new StringBuilder();
        completed = false;
        connection = getHttpConnection(URL);
    }

    private void download() {
        HttpURLConnection connection = getHttpConnection(URL);
        String fileName = URL.substring(URL.lastIndexOf('/') + 1);

        if (connection == null) {
            completed = true;
            return;
        }

        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

            int currentSize = 0;
            int bytesReadCount = 0;
            byte[] dataBuffer = new byte[BUFFER_SIZE];
            int size = connection.getContentLength();

            while ((bytesReadCount = inputStream.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesReadCount);

                currentSize += bytesReadCount;

                setProgressCurrentStatus(fileName, currentSize, size);
            }

            setProgressCurrentStatus(fileName, currentSize, size);
        } catch (IOException e) {
            String error = "| " + fileName + " download error |";
            progress.append(error);
        } finally {
            completed = true;
            connection.disconnect();
        }
    }

    private HttpURLConnection getHttpConnection(String URL) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("GET");
        } catch (IOException e) {
            System.out.println("Connection cannot be established. " + e.getMessage());
        }

        return connection;
    }

    public void closeConnection() {
        connection.disconnect();
    }

    private void setProgressCurrentStatus(String filename, int currentSize, int fullSize) {
        progress.setLength(0);
        progress.append("| filename: ").append(filename).append(" current size: ").append(currentSize).append(" ").append(" full size: ").append(fullSize).append(" |");
    }

    public String getProgress() {
        return progress.toString();
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void run() {
        download();
    }
}
