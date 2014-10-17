package com.rocui.JimageUploads;

import com.rocui.JimageUploads.ImageInfo.Info;
import com.rocui.util.file.Jile;
import com.rocui.util.file.image.Jimage;
import com.rocui.util.jsonx.JsonEach;
import com.rocui.util.jsonx.Jsonx;
import java.io.File;
import java.util.HashMap;
import java.util.List;
//import net.ymate.platform.mvc.web.IUploadFileWrapper;

public class Jgallery {

    private static Jgallery image = null;
    private HashMap<String, ImageInfo> list = new HashMap<String, ImageInfo>();
    private String basepath;
    private String localpath;

    public static void init(Jsonx option) throws Exception {
        if (Jgallery.image == null) {
            Jgallery.image = new Jgallery();
            Jgallery.image.localpath = Jile.contextPath();
            Jgallery.image.basepath = option.get("basepath").toString();
            option.get("images").each(new JsonEach() {
                @Override
                public boolean each(String key, Jsonx json) {
                    try {
                        ImageInfo info = new ImageInfo(key, json);
                        Jgallery.image.list.put(key, info);
                    } catch (Exception ex) {
                    }
                    return false;
                }
            });
        }
    }

    public static Jgallery gallery() {
        return Jgallery.image;
    }

    public static Jgallery gallery(String localpath) {
        Jgallery.image.localpath = localpath;
        return Jgallery.image;
    }

    public String save(String name, File file) throws Exception {
        ImageInfo info = this.list.get(name);
        if (info != null) {
            String path = this.localpath + this.basepath + File.separator + name;
            String filename = System.currentTimeMillis() + ".png";
            List<Info> xinfo = info.getList();
            for (Info x : xinfo) {
                String xpath = path + File.separator + (x.namespace().equals("") ? "" : x.namespace() + File.separator) + filename;
                if (x.width() != 0) {
                    Jimage.with(file).scaleWithMargin(x.width(), x.height()).png(xpath);
                } else {
                    Jimage.with(file).png(xpath);
                }
            }
            return filename;
        } else {
            return null;
        }
    }

//    public String save(String name, String path) throws Exception {
//        return this.save(name, new File(path));
//    }
//
//    public String save(String name, IUploadFileWrapper file) throws Exception {
//        File xfile = new File(this.localpath + this.basepath + File.separator + "__cache__" + File.separator + System.currentTimeMillis() + ".png");
//        if (!xfile.exists()) {
//            if (!xfile.getParentFile().exists()) {
//                xfile.getParentFile().mkdirs();
//            }
//            xfile.createNewFile();
//        }
//        file.writeTo(xfile);
//        String namex = this.save(name, xfile);
//        xfile.delete();
//        return namex;
//    }

    public String getBasePath(String name) {
        ImageInfo info = this.list.get(name);
        if (info != null) {
            return this.localpath + this.basepath + File.separator + name + File.separator;
        } else {
            return null;
        }
    }

    public String getBaseURI(String type, String size, String name) {
        ImageInfo info = this.list.get(type);
        if (info != null) {
            return this.basepath + "/" + type + "/" + (size == null || size.equals("") ? "" : size + "/") + name;
        } else {
            return null;
        }
    }

//    public static void main(String[] args) throws Exception {
//        System.out.println(Jile.contextPath());
//        String k = Jgallery.class.getClassLoader().getResource("bootservice.json").getFile();
//        Jsonx.create(new File(k)).get("services").each(new JsonEachArray() {
//            @Override
//            public boolean each(int index, Jsonx json) throws Exception {
//                if (json.get("name").toString().equals("JimageUploads")) {
//                    Jgallery.init(json.get("options"));
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//        System.out.println(Jgallery.gallery().getBasePath("cover"));
//        Jgallery.gallery().save("cover", "C:\\Users\\Jinliang\\Desktop\\images\\aa.jpg");
//    }
}
