package com.company;


public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
        FileDownloader fileDownloader1 = new FileDownloader();
        FileDownloader fileDownloader2 = new FileDownloader();

        fileDownloader1.setURL("http://212.183.159.230/5MB.zip");
        fileDownloader1.setPath("D:\\1.zip");

        fileDownloader2.setURL("http://212.183.159.230/10MB.zip");
        fileDownloader2.setPath("D:\\2.zip");

        fileDownloader1.start();
        fileDownloader2.start();

        while (!fileDownloader1.isCompleted() || !fileDownloader2.isCompleted()) {
            Thread.sleep(1000);
            System.out.println(fileDownloader1.getProgress() + " " + fileDownloader2.getProgress());
        }
    }
}
