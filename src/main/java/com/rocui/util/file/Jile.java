package com.rocui.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipFile;
import sun.io.ByteToCharBig5_Solaris;

public class Jile {

    private File file;
    protected static byte buf[] = new byte[1024];

    private Jile(File file) {
        this.file = file;
    }

    public static String classPath() {
        return Jile.class.getClassLoader().getResource("").getFile() + File.separator;
    }

    public static String contextPath() {
        File file = new File(Jile.classPath());
        return file.getParentFile().getParentFile().getAbsolutePath() + File.separator;
    }

    public static Jile with(String path) {
        return new Jile(new File(path));
    }

    public static Jile with(File file) {
        return new Jile(file);
    }

    public File file() {
        return this.file;
    }

    public String read() throws IOException {
        if (this.file.exists() && this.file.isFile()) {
            return new String(bytes(), "utf-8");
        } else {
            return null;
        }
    }

    public String read(String encoding) throws Exception {
        if (this.file.exists() && this.file.isFile()) {
            return new String(bytes(), encoding);
        } else {
            return null;
        }
    }

    public Jile read(OutputStream out) throws Exception {
        if (this.file.exists() && this.file.isFile()) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(this.file));
            int len;
            while ((len = in.read(buf)) >= 0) {
                out.write(buf, 0, len);
            }
            in.close();
        }
        return this;
    }

    public Jile read(Writer writer) throws Exception {
        if (this.file.exists() && this.file.isFile()) {
            writer.write(this.read());
            writer.close();
        }
        return this;
    }

    public Jile read(Writer writer, String encoding) throws Exception {
        if (this.file.exists() && this.file.isFile()) {
            writer.write(this.read(encoding));
            writer.close();
        }
        return this;
    }

    public Jile append(String content) throws Exception {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file, true), "utf-8");
                fout.write(content);
                fout.close();
            }
        } else {
            this.createNewFile();
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file, true), "utf-8");
            fout.write(content);
            fout.close();
        }
        return this;
    }

    public synchronized Jile write(String content) throws Exception {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file), "utf-8");
                fout.write(content);
                fout.close();
            }
        } else {
            this.createNewFile();
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file), "utf-8");
            fout.write(content);
            fout.close();
        }
        return this;
    }

    public synchronized Jile append(String content, String encoding) throws Exception {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file, true), encoding);
                fout.write(content);
                fout.close();
            }
        } else {
            this.createNewFile();
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file, true), encoding);
            fout.write(content);
            fout.close();
        }
        return this;
    }

    public synchronized Jile write(String content, String encoding) throws Exception {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file), encoding);
                fout.write(content);
                fout.close();
            }
        } else {
            this.createNewFile();
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(this.file), encoding);
            fout.write(content);
            fout.close();
        }
        return this;
    }

    public synchronized Jile write(InputStream input) throws Exception {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                int readLen;
                FileOutputStream output = new FileOutputStream(this.file);
                byte[] buffer = new byte[1024 * 8];
                while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1) {
                    output.write(buffer, 0, readLen);
                }
                input.close();
                output.flush();
                output.close();
            }
        } else {
            this.createNewFile();
            int readLen;
            FileOutputStream output = new FileOutputStream(this.file);
            byte[] buffer = new byte[1024 * 8];
            while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1) {
                output.write(buffer, 0, readLen);
            }
            input.close();
            output.flush();
            output.close();
        }
        return this;
    }

    public OutputStream toOutputStream() throws Exception {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                return new FileOutputStream(file);
            } else {
                return null;
            }
        } else {
            this.createNewFile();
            return new FileOutputStream(file);
        }
    }

    private synchronized void createNewFile() throws IOException {
        if (!this.file.getParentFile().exists()) {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
        }
        this.file.createNewFile();
    }

    public byte[] bytes() throws IOException {
        if (this.file.exists() && this.file.isFile()) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(this.file));
            ByteArrayOutputStream out = new ByteArrayOutputStream(10240);
            int len;
            while ((len = in.read(buf)) >= 0) {
                out.write(buf, 0, len);
            }
            in.close();
            return out.toByteArray();
        } else {
            return null;
        }
    }

    public void remove() {
        if (this.file.exists() && this.file.isFile()) {
            this.file.delete();
        } else {
            delFolder(this.file.getAbsolutePath());
        }
    }

    public Jile empty() {
        if (this.file.exists() && this.file.isDirectory()) {
            delAllFile(this.file.getAbsolutePath());
        }
        return this;
    }

    public Jile rename(String newname) {
        if (this.file.exists()) {
            File file = new File(newname);
            if (!file.exists()) {
                this.file.renameTo(file);
            }
        }
        return this;
    }

    public Jile each(JileEach each) throws Exception {
        if (this.file.exists() && this.file.isDirectory()) {
            String[] tempList = this.file.list();
            String path = this.file.getAbsolutePath();
            for (String tempList1 : tempList) {
                File temp;
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList1);
                } else {
                    temp = new File(path + File.separator + tempList1);
                }
                boolean isbreak = each.each(Jile.with(temp));
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public Jile browse(JileEach each) throws Exception {
        if (this.file.exists() && this.file.isDirectory()) {
            String[] tempList = this.file.list();
            String path = this.file.getAbsolutePath();
            for (String tempList1 : tempList) {
                File temp;
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList1);
                } else {
                    temp = new File(path + File.separator + tempList1);
                }
                if (temp.isDirectory()) {
                    Jile.with(temp).each(each);
                } else {
                    boolean isbreak = each.each(Jile.with(temp));
                    if (isbreak) {
                        break;
                    }
                }
            }
        }
        return this;
    }

    public Jile addFolder(String path) {
        if (this.file.exists()) {
            if (this.file.isDirectory()) {
                String pathx = this.file.getAbsolutePath() + File.separator + path;
                File x = new File(pathx);
                if (!x.exists()) {
                    x.mkdirs();
                }
            }
        } else {
            String pathx = this.file.getAbsolutePath() + File.separator + path;
            File x = new File(pathx);
            x.mkdirs();
        }
        return this;
    }

    public Jile copy(String newpath) throws IOException {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                File filex = new File(newpath);
                if (!filex.exists()) {
                    filex.getParentFile().mkdirs();
                    filex.createNewFile();
                }
                FileUtils.copyFile(this.file, filex);
            } else {
                copyFolder(this.file.getAbsolutePath(), newpath);
            }
        }
        return this;
    }

    public Jile move(String path) throws IOException {
        if (this.file.exists()) {
            if (this.file.isFile()) {
                this.copy(path);
                this.remove();
            } else {
                copyFolder(this.file.getAbsolutePath(), path);
                delFolder(this.file.getAbsolutePath());
            }
        }
        return new Jile(new File(path));
    }

    public Jile zip(String zipFileName) throws Exception {
        File filex = new File(zipFileName);
        if (!filex.exists()) {
            if (!filex.getParentFile().exists()) {
                filex.getParentFile().mkdirs();
            }
            filex.createNewFile();
        }
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filex));
        if (this.file.isDirectory()) {
            zip(out, this.file, "");
            out.close();
        } else {
            FileInputStream fis = new FileInputStream(this.file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] buf = new byte[1024];
            int len;
            FileOutputStream fos = new FileOutputStream(zipFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);
            ZipEntry ze = new ZipEntry(this.file.getName());
            zos.putNextEntry(ze);
            while ((len = bis.read(buf)) != -1) {
                zos.write(buf, 0, len);
                zos.flush();
            }
            bis.close();
            zos.close();
        }
        return this;
    }

    private void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + File.separator));
            base = base.length() == 0 ? "" : base + File.separator;
            for (File fl1 : fl) {
                zip(out, fl1, base + fl1.getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    public String getSuffix() {
        if (this.file.isFile()) {
            return this.file.getName().substring(this.file.getName().lastIndexOf("."));
        } else {
            return null;
        }
    }

    public String getNameWithoutSuffix() {
        if (this.file.isFile()) {
            return this.file.getName().substring(0, this.file.getName().lastIndexOf("."));
        } else {
            return null;
        }
    }

    public Jile unzip(String unzipDirectory) throws Exception {
        if (this.file.isFile()) {
            ZipFile zipFile = new ZipFile(this.file);
            ZipEntry entry = null;
            Enumeration zipEnum = zipFile.getEntries();
            while (zipEnum.hasMoreElements()) {
                entry = (ZipEntry) zipEnum.nextElement();
                String entryName = entry.getName();
                if (!entryName.endsWith("/")) {
                    String path = unzipDirectory + File.separator + entryName;
                    Jile.with(path).write(zipFile.getInputStream(entry));
                }
            }
        }
        return this;
    }

    public Jile unzip(String name, String unzipDirectory) throws Exception {
        if (this.file.isFile()) {
            ZipFile zipFile = new ZipFile(this.file);
            ZipEntry entry = null;
            Enumeration zipEnum = zipFile.getEntries();
            while (zipEnum.hasMoreElements()) {
                entry = (ZipEntry) zipEnum.nextElement();
                String entryName = entry.getName();
                if (!entryName.endsWith("/")) {
                    String path = unzipDirectory + File.separator + name + File.separator + entryName.substring(entryName.indexOf("/"));
                    Jile.with(path).write(zipFile.getInputStream(entry));
                }
            }
        }
        return this;
    }

    public Jile unzipCurrent() throws Exception {
        String p = this.file.getParentFile().getAbsolutePath();
        if (this.file.isFile()) {
            ZipFile zipFile = new ZipFile(this.file);
            ZipEntry entry = null;
            Enumeration zipEnum = zipFile.getEntries();
            while (zipEnum.hasMoreElements()) {
                entry = (ZipEntry) zipEnum.nextElement();
                String entryName = entry.getName();
                if (!entryName.endsWith("/")) {
                    String path = p + File.separator + entryName;
                    Jile.with(path).write(zipFile.getInputStream(entry));
                }
            }
        }
        return this;
    }

    public Jile unzipCurrent(String name) throws Exception {
        String p = this.file.getParentFile().getAbsolutePath();
        if (this.file.isFile()) {
            ZipFile zipFile = new ZipFile(this.file);
            ZipEntry entry = null;
            Enumeration zipEnum = zipFile.getEntries();
            while (zipEnum.hasMoreElements()) {
                entry = (ZipEntry) zipEnum.nextElement();
                String entryName = entry.getName();
                if (!entryName.endsWith("/")) {
                    String path = p + File.separator + name + File.separator + entryName.substring(entryName.indexOf("/"));
                    Jile.with(path).write(zipFile.getInputStream(entry));
                }
            }
        }
        return this;
    }

    private void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File xx = new File(filePath);
            xx.delete();
        } catch (Exception e) {
        }
    }

    private void delAllFile(String path) {
        File filex = new File(path);
        if (!filex.exists()) {
            return;
        }
        if (!filex.isDirectory()) {
            return;
        }
        String[] tempList = filex.list();
        File temp;
        for (String tempList1 : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList1);
            } else {
                temp = new File(path + File.separator + tempList1);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList1);
                delFolder(path + File.separator + tempList1);
            }
        }
    }

    private void copyFolder(String oldPath, String newPath) {
        try {
            new File(newPath).mkdirs();
            File a = new File(oldPath);
            String[] filet = a.list();
            File temp;
            for (String file1 : filet) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file1);
                } else {
                    temp = new File(oldPath + File.separator + file1);
                }
                if (temp.isFile()) {
                    FileUtils.copyFile(temp, new File(newPath + File.separator + (temp.getName()).toString()));
                }
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file1, newPath + File.separator + file1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}