package test;

import com.rocui.jmongo.base.annotation.Dao;

@Dao(name = "test")
public class TestProxyImpt implements ITestProxy{
    @Override
    public void doIt() {
        System.out.println("------------xxxx------------");
    }
}
