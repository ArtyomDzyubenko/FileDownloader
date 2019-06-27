package com.company;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Download extends Thread {
    private int BUFFER_SIZE = 4096;
    private String URL;
    private String filename;
    private boolean completed = false;
    private StringBuilder progress = new StringBuilder();
    private HttpURLConnection connection;

    private Download() {

    }

    public Download(String URL, String filePath) {
        this.URL = URL;
        this.filename = filePath;
    }

    private void download() {
        connection = getHttpConnection(URL);

        if (connection == null) {
            completed = true;
            return;
        }

        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
             FileOutputStream outputStream = new FileOutputStream(filename)) {

            int currentSize = 0;
            int bytesCount;
            byte[] dataBuffer = new byte[BUFFER_SIZE];
            int fullSize = connection.getContentLength();

            while ((bytesCount = inputStream.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
                outputStream.write(dataBuffer, 0, bytesCount);

                currentSize += bytesCount;

                setProgressCurrentStatus(filename, currentSize, fullSize);
            }

            setProgressCurrentStatus(filename, currentSize, fullSize);
            createFileChecksum(filename);
        } catch (IOException e) {
            String error = "| " + filename + " download error |";
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


    private void createFileChecksum(String inFilename) { //https://howtodoinjava.com/java/io/how-to-generate-sha-or-md5-file-checksum-hash-in-java/
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return;
        }


        try (FileInputStream inputStream = new FileInputStream(inFilename)) {
            byte[] byteArray = new byte[BUFFER_SIZE];
            int bytesCount;

            while ((bytesCount = inputStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + inFilename);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        String outFilename = inFilename + ".check";
        try (FileOutputStream outputStream = new FileOutputStream(outFilename)) {
            outputStream.write(sb.toString().getBytes());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + outFilename);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void closeConnection() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private void setProgressCurrentStatus(String filename, int currentSize, int fullSize) {
        progress.setLength(0);
        progress.append("| filename: ").append(filename)
                .append(" current size: ").append(currentSize).append(" ")
                .append(" full size: ").append(fullSize).append(" |");
    }

    public String getProgress() {
        return progress.toString();
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void run() {
        if (!this.isInterrupted()) {
            download();
        }
    }
}
