package com.rocui.mvc.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileView extends View {

    private File file;

    public FileView(String path) {
        this.file = new File(path);
    }

    public FileView(File file) {
        this.file = file;
    }

    @Override
    public void out() throws Exception {
        String filename = file.getName();
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
        byte[] buffer;
        try (InputStream fis = new BufferedInputStream(new FileInputStream(file))) {
            buffer = new byte[fis.available()];
            fis.read(buffer);
        }
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
        response.addHeader("Content-Length", "" + file.length());
        try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
        }
    }
}
