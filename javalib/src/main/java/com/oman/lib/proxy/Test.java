package com.oman.lib.proxy;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.crypto.Cipher;

import sun.misc.ProxyGenerator;

/**
 * @author:ZhouJiang
 * @date:2020/5/17 22:29
 * @email:zhoujiang2012@163.com
 */
public class Test {
    public static void main(String[] args) throws Exception {
        // static agent
        Service s = new Agent();
        s.service(5);
        System.out.println("----------------");

        // dynamic agent
        Service s2 = new BeautyService();
        OnClickListener listener = new MyButton();
        Object o = Proxy.newProxyInstance(Test.class.getClassLoader(), new Class[]{Service.class, OnClickListener.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("service"))
                    method.invoke(s2, args);
                if (method.getName().equals("onClick"))
                    method.invoke(listener, args);
                return null;
            }
        });

        Service service = (Service) o;
        service.service(6);
        OnClickListener click = (OnClickListener) o;
        click.onClick("name");


        proxy();
        proxy1();
    }

    private static void proxy() throws Exception {
        String name = Service.class.getName() + "$proxy";
        byte[] bytes = ProxyGenerator.generateProxyClass(name, new Class[]{Service.class});
        FileOutputStream fos = new FileOutputStream("javalib/" + name + ".class");
        fos.write(bytes);
        fos.close();
    }

    private static void proxy1() throws Exception {
        String name = OnClickListener.class.getName() + "$proxy1";
        byte[] bytes = ProxyGenerator.generateProxyClass(name, new Class[]{OnClickListener.class});
        FileOutputStream fos = new FileOutputStream("javalib/" + name + ".class");
        fos.write(bytes);
        fos.close();
    }

}

interface OnClickListener {
    void onClick(String v);
}

class MyButton implements OnClickListener {

    @Override
    public void onClick(String v) {
        System.out.println("print onclick:" + v);
    }
}