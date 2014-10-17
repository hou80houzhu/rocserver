package com.rocui.util.file.image;

import java.awt.Color;
import java.awt.Font;

public class Jtext {

    private String fontName = "Consolas";
    private int fontStyle = Font.PLAIN;
    private Color color = new Color(255, 255, 255);
    private int fontSize = 16;
    private float alpha = 1;
    private String text = "test...";

    private Jtext() {
    }

    public static Jtext getText() {
        return new Jtext();
    }

    public String text() {
        return this.text;
    }

    public Jtext text(String text) {
        this.text = text;
        return this;
    }

    public String fontName() {
        return fontName;
    }

    public Jtext fontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public int fontStyle() {
        return fontStyle;
    }

    public Jtext fontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public Color color() {
        return color;
    }

    public Jtext color(Color color) {
        this.color = color;
        return this;
    }

    public Jtext color(int red, int green, int blue) {
        this.color = new Color(red, green, blue);
        return this;
    }

    public Jtext color(String color16) {
        this.color = new Color(Integer.parseInt(color16, 16));
        return this;
    }

    public int fontSize() {
        return fontSize;
    }

    public Jtext fontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public float alpha() {
        return alpha;
    }

    public Jtext alpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

}
