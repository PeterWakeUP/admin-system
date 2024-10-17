package com.tcshipin.tcwebsite;

import com.evsoft.AdminSystemApplication;
import com.evsoft.modules.sys.dao.SysUserDao;
import org.jasypt.util.text.BasicTextEncryptor;
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

    @Test
    public void testDecrypt(){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("Bt%XJ^n1j8mz");
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("root");
        String password = textEncryptor.encrypt("admin");
        System.out.println("username: " + username);
        System.out.println("password:" + password);
    }
}
