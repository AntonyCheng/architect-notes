package top.sharehome.jvmloader;

import com.sun.crypto.provider.AESKeyGenerator;
import sun.misc.Launcher;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类加载示例
 *
 * @author AntonyCheng
 */
public class LoadClassExample {

    public static void main(String[] args) {
        System.out.println("=======================加载器=======================");
        ClassLoader bootstrapClassLoader = String.class.getClassLoader();
        System.out.println("String类属于核心类库，使用" + bootstrapClassLoader + "加载器");
        ClassLoader extClassLoader = AESKeyGenerator.class.getClassLoader();
        System.out.println("AESKeyGenerator类属于扩展类库，使用" + extClassLoader + "加载器");
        ClassLoader appClassLoader = LoadClassExample.class.getClassLoader();
        System.out.println("LoadClassExample类属于类路径类库，使用" + appClassLoader + "加载器");

        System.out.println("\n===================系统默认加载器====================");
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println("系统默认加载器：" + systemClassLoader);

        System.out.println("\n======================父加载器======================");
        Object bootstrapClassLoaderParent = Objects.isNull(bootstrapClassLoader) ? "无父加载器" : bootstrapClassLoader.getParent();
        System.out.println("引导类加载器父类是：" + bootstrapClassLoaderParent);
        ClassLoader extClassLoaderParent = extClassLoader.getParent();
        System.out.println("扩展类加载器父类是：" + extClassLoaderParent);
        ClassLoader appClassLoaderParent = appClassLoader.getParent();
        System.out.println("应用类加载器父类是：" + appClassLoaderParent);

        System.out.println("\n=====================加载文件夹=====================");
        String bootstrapClassLoaderPath = Arrays.stream(Launcher.getBootstrapClassPath().getURLs()).map(URL::toString).collect(Collectors.joining(","));
        System.out.println("引导类加载器加载文件夹如下：" + bootstrapClassLoaderPath);
        String extClassLoaderPath = System.getProperty("java.ext.dirs");
        System.out.println("扩展类加载器加载文件夹如下：" + extClassLoaderPath);
        String appClassLoaderPath = System.getProperty("java.class.path");
        System.out.println("应用类加载器加载文件夹如下：" + appClassLoaderPath);
    }

}
