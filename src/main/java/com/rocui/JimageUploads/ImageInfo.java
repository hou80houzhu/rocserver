package com.rocui.JimageUploads;

import com.rocui.util.jsonx.JsonEachArray;
import com.rocui.util.jsonx.Jsonx;
import java.util.ArrayList;
import java.util.List;

public class ImageInfo {

    private List<Info> list = new ArrayList<Info>();
    private String name;

    protected ImageInfo(String name, Jsonx option) throws Exception {
        ImageInfo ths = this;
        this.name = name;
        option.each(new JsonEachArray(ths) {
            @Override
            public boolean each(int index, Jsonx json) {
                ImageInfo info = (ImageInfo) this.arguments[0];
                Info x = new Info();
                x.width = json.get("width").toInt();
                x.height = json.get("height").toInt();
                x.namespace = json.get("namespace").toString();
                info.list.add(x);
                return false;
            }
        });
    }

    protected String name(){
        return this.name;
    }
    protected List<Info> getList() {
        return this.list;
    }

    protected class Info {

        private int width;
        private int height;
        private String namespace;

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }

        public String namespace() {
            return this.namespace;
        }
    }
}
