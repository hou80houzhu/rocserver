package com.rocui.util.file.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;

public class Jimage {

    public final static String IMAGE_TYPE_GIF = "gif";
    public final static String IMAGE_TYPE_JPG = "jpg";
    public final static String IMAGE_TYPE_PNG = "png";

    public final static String TYPE_POSITION_TOPLEFT = "topleft";
    public final static String TYPE_POSITION_TOPRIGHT = "topright";
    public final static String TYPE_POSITION_BOTTOMLEFT = "bottomleft";
    public final static String TYPE_POSITION_BOTTOMRIGHT = "bottomright";

    private File image;
    private BufferedImage imageis;

    public Jimage(File image) throws IOException {
        this.image = image;
        this.imageis = ImageIO.read(image);
    }

    public static Jimage with(File file) throws IOException {
        return new Jimage(file);
    }

    public static Jimage with(String imagePath) throws IOException {
        return new Jimage(new File(imagePath));
    }

    public Jimage copy(String fullName) throws IOException {
        File file = new File(fullName);
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        FileUtils.copyFile(this.image, file);
        return this;
    }

    public String type() {
        String name = image.getName();
        String type;
        if (name.endsWith(".jpg")) {
            type = "jpg";
        } else if (name.endsWith(".png")) {
            type = "png";
        } else if (name.endsWith(".gif")) {
            type = "gif";
        } else {
            type = null;
        }
        return type;
    }

    public String name() {
        return this.image.getName();
    }

    public String path() {
        return this.image.getPath();
    }

    public String absolutePath() {
        return this.image.getAbsolutePath();
    }

    public int width() {
        return this.imageis.getWidth();
    }

    public int height() {
        return this.imageis.getHeight();
    }

    public void delete() {
        if (this.image.exists()) {
            this.image.delete();
        }
    }

    public Jimage zoomIn(int scale) {
        return this.zoom(scale, true);
    }

    public Jimage zoomOut(int scale) {
        return this.zoom(scale, false);
    }

    public Jimage scaleWithMargin(int width, int height) {
        return this.scale(width, height, true);
    }

    public Jimage scale(int width, int height) {
        return this.scale(width, height, false);
    }

    public Jimage cut(int x, int y, int width, int height) {
        try {
            BufferedImage bi = this.imageis;
            int srcWidth = bi.getHeight();
            int srcHeight = bi.getWidth();
            if (srcWidth > 0 && srcHeight > 0) {
                Image imagex = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imagex.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                this.imageis = tag;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Jimage gray() {
        try {
            BufferedImage src = this.imageis;
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            this.imageis = src;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Jimage pressText(Jtext text, String type) {
        return this.pressText(text, type, 5);
    }

    public Jimage pressText(Jtext text, String type, int offset) {
        int x = 0;
        int y = 0;
        if (type.equals(Jimage.TYPE_POSITION_TOPLEFT)) {
            x = x + offset;
            y = y + offset;
        } else if (type.equals(Jimage.TYPE_POSITION_TOPRIGHT)) {
            y = y + offset;
            x = (this.width() - (getLength(text.text()) * text.fontSize())) - offset;
        } else if (type.equals(Jimage.TYPE_POSITION_BOTTOMLEFT)) {
            x = x + offset;
            y = (this.height() - text.fontSize()) - offset;
        } else if (type.equals(Jimage.TYPE_POSITION_BOTTOMRIGHT)) {
            x = (this.width() - (getLength(text.text()) * text.fontSize())) - offset;
            y = (this.height() - text.fontSize()) - offset;
        }
        return this.pressText(text, x, y);
    }

    public Jimage pressText(Jtext text, int x, int y) {
        try {
            Image src = this.imageis;
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage imagex = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = imagex.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(text.color());
            g.setFont(new Font(text.fontName(), text.fontStyle(), text.fontSize()));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, text.alpha()));
            g.drawString(text.text(), x, y);
            g.dispose();
            this.imageis = imagex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Jimage pressImage(String imagepath, String type, float alpha) {
        return this.pressImage(new File(imagepath), type, 5, alpha);
    }

    public Jimage pressImage(File file, String type, float alpha) {
        return this.pressImage(file, type, 5, alpha);
    }

    public Jimage pressImage(String imagepath, String type, int offset, float alpha) {
        return this.pressImage(new File(imagepath), type, offset, alpha);
    }

    public Jimage pressImage(File file, String type, int offset, float alpha) {
        try {
            int x = 0;
            int y = 0;
            Image src_biao = ImageIO.read(file);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            if (type.equals(Jimage.TYPE_POSITION_TOPLEFT)) {
                x = x + offset;
                y = y + offset;
            } else if (type.equals(Jimage.TYPE_POSITION_TOPRIGHT)) {
                y = y + offset;
                x = (this.width() - wideth_biao) - offset;
            } else if (type.equals(Jimage.TYPE_POSITION_BOTTOMLEFT)) {
                x = x + offset;
                y = (this.height() - height_biao) - offset;
            } else if (type.equals(Jimage.TYPE_POSITION_BOTTOMRIGHT)) {
                x = (this.width() - wideth_biao) - offset;
                y = (this.height() - height_biao) - offset;
            }
            return this.pressImage(file, x, y, alpha);
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public Jimage pressImage(String imagepath, int x, int y, float alpha) {
        return this.pressImage(new File(imagepath), x, y, alpha);
    }

    public Jimage pressImage(File image, int x, int y, float alpha) {
        try {
            Image src = this.imageis;
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage imagex = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = imagex.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            Image src_biao = ImageIO.read(image);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(src_biao, x, y, wideth_biao, height_biao, null);
            g.dispose();
            this.imageis = imagex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private Jimage scale(int width, int height, boolean isbg) {
        try {
            BufferedImage bi = this.imageis;
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                double ratio;
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            BufferedImage imagex = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            if (isbg) {
                Graphics2D g = imagex.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                }
                g.dispose();
            }
            this.imageis = imagex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private Jimage zoom(int scale, boolean iszoomin) {
        try {
            BufferedImage src = this.imageis;
            int width = src.getWidth();
            int height = src.getHeight();
            if (iszoomin) {
                width = width * scale;
                height = height * scale;
            } else {
                width = width / scale;
                height = height / scale;
            }
            Image imagex = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(imagex, 0, 0, null);
            g.dispose();
            this.imageis = tag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Jimage png(String fullName) {
        try {
            File x = new File(fullName);
            if (!x.exists()) {
                if(!x.getParentFile().exists()){
                    x.getParentFile().mkdirs();
                }
                x.createNewFile();
            }
            ImageIO.write(this.imageis, Jimage.IMAGE_TYPE_PNG, x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void png(OutputStream out) {
        try {
            ImageIO.write(this.imageis, Jimage.IMAGE_TYPE_PNG, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Jimage jpg(String fullName) {
        try {
            File x = new File(fullName);
            if (!x.exists()) {
                if(!x.getParentFile().exists()){
                    x.getParentFile().mkdirs();
                }
                x.createNewFile();
            }
            ImageIO.write(this.imageis, Jimage.IMAGE_TYPE_JPG, x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void jpg(OutputStream out) {
        try {
            ImageIO.write(this.imageis, Jimage.IMAGE_TYPE_JPG, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Jimage gif(String fullName) {
        try {
            File x = new File(fullName);
            if (!x.exists()) {
                if(!x.getParentFile().exists()){
                    x.getParentFile().mkdirs();
                }
                x.createNewFile();
            }
            ImageIO.write(this.imageis, Jimage.IMAGE_TYPE_GIF, x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void gif(OutputStream out) {
        try {
            ImageIO.write(this.imageis, Jimage.IMAGE_TYPE_GIF, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    public static void main(String[] args) {
        try {
            Jimage.with("E:\\xx.jpg").gray().cut(0, 100, 400, 400).pressText(Jtext.getText()
                    .alpha(1)
                    .color(255, 255, 255)
                    .fontName("Consolas")
                    .fontSize(12)
                    .fontStyle(Font.BOLD).text("Fuck this girl"), Jimage.TYPE_POSITION_BOTTOMRIGHT).png("E:\\aaa\\xxx.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
