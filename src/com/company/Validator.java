package com.company;


public class Validator {
    private int URL_MAX_LENGTH = 2000;
    private int URL_MIN_LENGTH = 10; // http://i.i
    private int FILENAME_MAX_LENGTH = 255;
    private int FILENAME_MIN_LENGTH = 6; // X:\1.t
    private String URL_PATTERN = "^(http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private String FILENAME_PATTERN = "([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.[\\w+])$";

    public boolean isURLValid(String url) {
        if (url == null) {
            System.out.println("URL cannot be null.");
            return false;
        }

        if (url.isEmpty() || url.length() < URL_MIN_LENGTH || url.length() > URL_MAX_LENGTH) {
            System.out.println("Incorrect url length.");
            return false;
        }


        if (!url.matches(URL_PATTERN)) {
            System.out.println("Incorrect url format.");
            return false;
        }

        return true;
    }

    public boolean isFilenameValid(String filename) {
        if (filename == null) {
            System.out.println("Filename cannot be null.");
            return false;
        }

        if (filename.isEmpty() || filename.length() < FILENAME_MIN_LENGTH ||  filename.length() > FILENAME_MAX_LENGTH) {
            System.out.println("Incorrect filename length.");
            return false;
        }

        if (!filename.matches(FILENAME_PATTERN)) {
            System.out.println("Incorrect filename format.");
            return false;
        }

        return true;
    }
}
