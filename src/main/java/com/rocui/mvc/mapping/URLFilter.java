package com.rocui.mvc.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLFilter {

    private final HashMap<String, String> map = new HashMap<>();
    private final List<Info> list = new ArrayList<>();
    private final Pattern hasDot = Pattern.compile("(\\{\\w*\\})");

    public void add(String path) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        Matcher a = hasDot.matcher(path);
        boolean has = false;
        int count = 0;
        int start = 0;
        List<String> pars = new ArrayList<>();
        while (a.find()) {
            has = true;
            if (count == 0) {
                start = a.start();
            }
            pars.add(path.substring(a.start() + 1, a.end() - 1));
            count++;
        }
        String pathx = path.replaceAll("(\\{\\w*\\})", "((?!/).)*");
        if (has) {
            Info info = new Info();
            info.originalpath = path;
            info.pattern = Pattern.compile("^" + pathx + "$");
            info.count = count;
            info.patternString = "^" + pathx + "/$";
            info.firstposition = start;
            info.keys = pars;
            String[] aStrings = path.split("\\.");
            if (aStrings.length > 1) {
                info.suffix = aStrings[1];
            }
            this.list.add(info);
        } else {
            map.put(path, path);
        }
    }

    public MatchResult check(String path) {
        String suffix = "";
        String[] bString = path.split("\\.");
        if (bString.length > 1) {
            suffix = bString[1];
            path = path + "/";
        } else {
            if (!bString[0].endsWith("/")) {
                path = bString[0] + "/";
            }
        }
        MatchResult result = new MatchResult();
        if (this.map.containsKey(path)) {
            result.path = path;
            result.matchpath = path;
            return result;
        } else {
            Info a = null;
            for (Info info : this.list) {
                if (info.pattern.matcher(path).find()) {
                    if (null == a) {
                        a = info;
                    } else if (info.suffix.equals(suffix)) {
                        if (info.count <= a.count) {
                            if (info.firstposition > a.firstposition) {
                                a = info;
                            }
                        }
                    }
                }
            }
            if (null != a) {
                String[] p = path.split("\\/");
                String[] pp = a.originalpath.split("\\/");
                int cd = 0;
                for (int i = 0; i < pp.length; i++) {
                    if (pp[i].startsWith("{")) {
                        result.map.put(a.keys.get(cd), p[i]);
                        cd++;
                    }
                }
                result.hasParas = true;
                result.path = a.originalpath;
                result.matchpath = path;
            }
            return result;
        }
    }

    public class Info {

        private List<String> keys = new ArrayList<String>();
        private String originalpath = "";
        private int count = 0;
        private Pattern pattern = null;
        private String patternString = "";
        private int firstposition = 0;
        private String suffix = "";
    }

    public class MatchResult {

        private String path;
        private String matchpath;
        private boolean hasParas = false;
        HashMap<String, String> map = new HashMap<>();

        public HashMap<String, String> getValueMap() {
            return this.map;
        }

        public String getOriginalPath() {
            return this.path;
        }

        public String getMatchPath() {
            return this.matchpath;
        }

        public boolean isHasParas() {
            return hasParas;
        }
    }

    public static void main(String[] args) {
        URLFilter test = new URLFilter();
        test.add("aa/bb/{cc}/{dd}/{ee}");
        test.add("aa/bb/cc/{dd}/{ee}.xhtml");
        test.add("aa/{bb}/cc/{dd}/ee");
        test.add("aa/{bb}/{cc}/{dd}/{ee}");
        test.add("{aa}/{bb}/{cc}/{dd}/{ee}");
        test.add("aa/bb/{cc}/{dd}");
        test.add("aa/bb/cc/ddfdfdfed/eeryttttyy.html");
        System.out.println(test.map);
        MatchResult result = test.check("aa/bb/cc/ddfdfdfed/eeryyy.html");
        System.out.println(result.path);
    }
}
