package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    //For test
    //http://212.183.159.230/5MB.zip D:\1.zip
    //http://212.183.159.230/10MB.zip D:\2.zip
    private static DownloadManager manager = DownloadManager.getInstance();
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Validator validator = new Validator();

    public static void main(String[] args) {
        manager.start();
        showMenu();
    }

    private static void showMenu() {
        String choice;

        while (!manager.isInterrupted()) {
            System.out.println("----FILE DOWNLOADER----");
            System.out.println("1: Download file");
            System.out.println("2: Show progress");
            System.out.println("3: Exit");
            System.out.println("-----------------------");
            System.out.print("> ");

            try {
                choice = reader.readLine();

                switch (choice) {
                    case "1": {
                        System.out.print("URL> ");
                        String URL = reader.readLine().trim();
                        System.out.print("filename> ");
                        String filename = reader.readLine().trim();

                        boolean urlValid = validator.isURLValid(URL);
                        boolean filenameValid = validator.isFilenameValid(filename);

                        if (!urlValid || !filenameValid) {
                            break;
                        }

                        manager.addAndStart(new Download(URL, filename));

                        break;
                    }
                    case "2": {
                        System.out.println("---CURRENT DOWNLOADS---");
                        System.out.println(manager.getProgress());
                        System.out.println("-----------------------");
                        break;
                    }
                    case "3": {
                        manager.closeAllDownloads();
                        manager.interrupt();
                        break;
                    }

                    default:
                        break;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
