/*
package com.floreantpos.util;

import com.floreantpos.config.AppConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

*/
/**
 * This example demonstrates how HttpAsyncClient can be used to upload or download files
 * without creating an intermediate content buffer in memory (zero copy file transfer).
 *//*

public class ReceiptUploader implements Runnable {

    public void run() {

        System.out.println("ReceiptUploader started");

        final File folder = new File("receipt");

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".txt");
            }
        };

        if (folder.exists()) {

            for (final String f : folder.list(filter)) {

                System.out.println(f);

                if (!f.endsWith("uploaded")) {

                    System.out.println("A");

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            upload(new File(folder,f).getAbsolutePath());
                        }
                    };

                    Thread t = new Thread(r);
                    t.setPriority(Thread.MIN_PRIORITY);
                    t.start();

                }

            }
        }

        try {Thread.sleep(60*1000);} catch(Exception ignored) {}

    }

    public void upload(String filePath) {

        System.out.println("Uploading "+filePath);

        File sourceFile = new File(filePath);

        URL networkUrl;

        try {

            networkUrl = new URL(AppConfig.getTmdUrl());

            String boundary = "*****";
            String lineEnd = "\r\n";
            String twoHyphens = "--";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 64 * 1024;

            InputStream bs = new FileInputStream(sourceFile);

            HttpURLConnection connection = (HttpURLConnection) networkUrl.openConnection();

            // Allow Inputs &amp; Outputs.
            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(64 * 1024);
            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");

            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());

            outputStream.write((twoHyphens + boundary + lineEnd).getBytes("UTF-8"));
            outputStream.write(("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + filePath + "\"" + lineEnd).getBytes("UTF-8"));
            outputStream.write(lineEnd.getBytes("UTF-8"));

            bytesAvailable = bs.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = bs.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = bs.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = bs.read(buffer, 0, bufferSize);
            }

            outputStream.write(lineEnd.getBytes("UTF-8"));
            outputStream.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();

            bs.close();

            // Responses from the server (code and message)
            connection.getResponseCode();
            String resp = connection.getResponseMessage();

            System.out.println(resp);

            if ("OK".equals(resp)) {
                onSucceed(sourceFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void onSucceed(File file) {

        Path source = file.toPath();

        try {

            File target = new File(file.getAbsolutePath()+".uploaded");

            if (target.exists()) {

                file.delete();

            } else {

                Files.move(source, source.resolveSibling(file.getName() + ".uploaded"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}*/
