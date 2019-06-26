package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DownloadManager extends Thread {
    private static DownloadManager instance;
    private static final List<FileDownloader> downloads = new ArrayList<>();
    private String progress = "";

    private DownloadManager() {

    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }

        return instance;
    }

    public void addAndStart(FileDownloader downloader) {
        downloads.add(downloader);
        downloader.start();
    }

    public void printDownloadProgress() {
        if (downloads.isEmpty()) {
            return;
        }

        downloads.forEach(downloader -> System.out.print(downloader.getProgress()));
        System.out.println();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteCompleted() {
        if (downloads.isEmpty()) {
            return;
        }

        FileDownloader downloader;
        Iterator<FileDownloader> iterator = downloads.iterator();

        while (iterator.hasNext()) {
            downloader = iterator.next();

            if (downloader.isCompleted()) {
                System.out.println(downloader.getProgress());
                downloader.interrupt();
                iterator.remove();
            }
        }
    }

    public boolean isAllCompleted() {
        for (FileDownloader downloader : downloads) {
            if (!downloader.isCompleted()) {
                return false;
            }
        }

        return true;
    }

    public void closeAllDownloads() {
        for (FileDownloader downloader : downloads) {
            downloader.closeConnection();
            downloader.interrupt();
        }
    }

    @Override
    public void run() {
        while (!isAllCompleted()) {
            printDownloadProgress();
            deleteCompleted();
        }
    }

    public String getProgress() {
        if (downloads.isEmpty()) {
            return "";
        }

        progress = "";
        downloads.forEach(downloader -> progress = progress.concat(downloader.getProgress()));

        return progress;
    }
}
