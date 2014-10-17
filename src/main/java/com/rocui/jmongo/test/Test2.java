package test;

import com.rocui.jmongo.Jmongo;
import com.rocui.jmongo.base.Query;
import com.rocui.jmongo.base.Query;
import com.rocui.jmongo.service.JmongoDao;
import java.lang.reflect.Proxy;

public class Test2 {

    private String a = "aa";
    private String b = "bb";

    public static void main(String[] args) {
        try {
//            Jmongo.db("test").table("test").add("{\"cc\":\"cc\",\"dd\":[{\"aa\":\"aa\"}]}");
            String query = Query
                    .find("cc").lt("aa").gt("dd")
                    .find("nxxxn").eq("xxxx")
                    .find("dd").gt("dd")
                    .find("kk").mod(1, 10)
                    .find("xxx").all(new Object[]{1, 3, 5, 7})
                    .find("mmm").elemMatch(Query.find("xx").lt("aa")
                            .find("ddd").gt("xxx").query())
                    .limit(0, 1000)
                    .sort("aa").desc()
                    .sort("cc").asc()
                    .toString();
            System.out.println(query);
//            String xx = Query.with("{}").toString();
//            String query2 = Query.find("xx").gt("aa").toString();
//            System.out.println(xx);
//            System.out.println(query);
//            System.out.println(query2);
//            System.out.println(Objson.db("test").table("test").add("{\"cc\":\"cc\",\"dd\":{\"aa\":\"aa\"}}").find(Query.find("cc").eq("cc").query()).get(0));
//            System.out.println(Objson.db("test").table("test").find(Query.find("cc").eq("cc").query()).get(0).toString());
//            System.out.println(n);
//            List<Test2> list=new ArrayList<Test2>();
//            list.add(new Test2());
//            list.add(new Test2());
//            System.out.println(Jsonx.create(list).each(new JsonEach() {
//                @Override
//                public boolean each(String key, Jsonx value) {
//                    try {
//                        value.set("a", "xxxxxxx");
//                        value.remove("a");
//                    } catch (Exception ex) {
//                        Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    return true;
//                }
//            }).toString());
//            Jmongo.db("test").table("test").find().forEach(new RowEachArray() {
//                @Override
//                public boolean each(Jmongo.Row row, int index) {
//                    try {
//                        HashMap<String,Object> map=new HashMap<String,Object>();
//                        map.put("aacc", "ccc");
//                        System.out.println(row.get("dd").type());
//                        System.out.println(row.get("dd").add(map).save());
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    return false;
//                }
//            });
//            Jmongo.getTable("test").find().forEach(new RowEachArray() {
//                @Override
//                public boolean each(Jmongo.Row row, int index) {
//                    System.out.println(index);
//                    return false;
//                }
//            });
//            Jmongo.withTable("test2").add("{\"aa\":\"55\"}").close();
//            Jmongo.withTable("test2").find(Query.find("aa").eq("55").sort("aa").asc().query()).get(0).set("bcccccb","bxxxxxb").save();
//            System.out.println(Jmongo.withTable("test2").find(Query.find("aa").eq("55").query()).get(0).get("_id"));
//            System.out.println(Query.id("529477d819a28d3278f0f2fa"));
//            System.out.println(Jmongo.withTable("test2").find(Query.id("529477d819a28d3278f0f2fa").query()).get(0).id());
//            Jmongo.closeSession();
//            ITestProxy xx=new TestProxyImpt();
//            ProxyHandler hand=new ProxyHandler(xx);
//            ITestProxy kk=(ITestProxy)Proxy.newProxyInstance(xx.getClass().getClassLoader(), xx.getClass().getInterfaces(), hand);
//            kk.doIt();
            ITestProxy x=(ITestProxy)JmongoDao.getDao("test");
            x.doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the a
     */
    public String getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(String a) {
        this.a = a;
    }

    /**
     * @return the b
     */
    public String getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(String b) {
        this.b = b;
    }
}
