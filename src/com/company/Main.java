package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        DownloadManager manager = DownloadManager.getInstance();
        manager.start();

        //FileDownloader fileDownloader1 = new FileDownloader("http://212.183.159.230/5MB.zip", "D:\\1.zip");
        //FileDownloader fileDownloader2 = new FileDownloader("http://212.183.159.230/10MB.zip", "D:\\2.zip");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String choice = "";

        while (!manager.isInterrupted()) {
            System.out.println("1: Download file");
            System.out.println("2: Exit");
            //System.out.println(manager.getProgress());
            System.out.println(">");
            choice = reader.readLine();

            switch (choice) {
                case "1": {
                    System.out.println("URL> ");
                    String URL = reader.readLine().trim();
                    System.out.println("filename> ");
                    String filename = reader.readLine();
                    manager.addAndStart(new FileDownloader(URL, filename));
                    break;
                }
                case "2": {
                    manager.closeAllDownloads();
                    manager.interrupt();
                    break;
                }
            }
        }
    }
}
