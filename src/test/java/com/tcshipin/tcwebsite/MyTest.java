package com.tcshipin.tcwebsite;

import com.evsoft.AdminSystemApplication;
import com.evsoft.modules.sys.dao.SysUserDao;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminSystemApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyTest {

    @Autowired
    SysUserDao sysUserDao;

    @Test
    public void testb(){
        System.out.println("testb");
        System.out.println(sysUserDao.queryAllPerms(2L));
    }

    @Test
    public void testa(){
        System.out.println("testa");
        System.out.println(sysUserDao.queryAllPerms(1L));
    }
}
