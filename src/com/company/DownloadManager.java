package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DownloadManager extends Thread {
    private static DownloadManager instance;
    private List<Download> downloads = new ArrayList<>();
    private StringBuilder progress = new StringBuilder();

    private DownloadManager() {

    }

    private void deleteCompleted() {
        if (downloads.isEmpty()) {
            return;
        }

        Download download;
        Iterator<Download> iterator = downloads.iterator();

        while (iterator.hasNext()) {
            download = iterator.next();

            if (download != null && download.isCompleted()) {
                System.out.println("COMPLETED: " + download.getProgress());
                download.interrupt();
                iterator.remove();
            }
        }
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

    public void addAndStart(Download download) {
        downloads.add(download);
        download.start();
    }

    public void closeAllDownloads() {
        if (downloads.isEmpty()) {
            return;
        }

        Download download;
        Iterator<Download> iterator = downloads.iterator();

        while (iterator.hasNext()) {
            download = iterator.next();
            download.closeConnection();
            iterator.remove();
        }
    }

    public String getProgress() {
        if (downloads.isEmpty()) {
            return "No downloads";
        }

        progress.setLength(0);
        downloads.forEach(downloader -> progress.append(downloader.getProgress()));

        return progress.toString();
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            deleteCompleted();
        }
    }
}
