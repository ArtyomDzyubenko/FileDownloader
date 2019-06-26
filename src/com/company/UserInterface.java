package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface extends Thread {
    private final static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final static DownloadManager manager = DownloadManager.getInstance();

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            System.out.println("---User interface---");
            System.out.println("1. add download");
            System.out.println("2. exit");
            System.out.println(manager.getProgress());
            System.out.print("> ");

            try {
                String choice = reader.readLine();


            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
