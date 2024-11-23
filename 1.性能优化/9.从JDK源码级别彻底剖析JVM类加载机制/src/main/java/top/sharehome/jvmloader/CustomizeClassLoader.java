package top.sharehome.jvmloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义类加载器
 *
 * @author AntonyCheng
 */
public class CustomizeClassLoader extends ClassLoader {

    private final String classPath;

    public CustomizeClassLoader(String classPath) {
        this.classPath = classPath;
    }

    private byte[] loadByte(String name) throws IOException {
        name = name.replaceAll("\\.", "/");
        FileInputStream fileInputStream = new FileInputStream(classPath + "/" + name + ".class");
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        return bytes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = loadByte(name);
            // defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 这里需要把Math.class文件提前放在D:/Desktop/test/top/sharehome/jvmloader目录下
        // 而且要保证类路径、jre相关路径下没有top.sharehome.jvmloader.Math类文件
        CustomizeClassLoader customizeClassLoader1 = new CustomizeClassLoader("D:/Desktop/test1");
        Class<?> clazz1 = customizeClassLoader1.loadClass("top.sharehome.jvmloader.Math");
        System.out.println("加载top.sharehome.jvmloader.Math类的自定义加载器为：" + clazz1.getClassLoader().getClass().getName());
        Object obj1 = clazz1.newInstance();
        Method method1 = clazz1.getDeclaredMethod("computeAdditionAndMultiplyBy10", int.class, int.class);
        Object res1 = method1.invoke(obj1, 1, 1);
        System.out.println("调用Math对象的computeAdditionAndMultiplyBy10方法结果为：" + res1);

        System.out.println("=============================================================================");

        CustomizeClassLoader customizeClassLoader2 = new CustomizeClassLoader("D:/Desktop/test2");
        Class<?> clazz2 = customizeClassLoader2.loadClass("top.sharehome.jvmloader.Math");
        System.out.println("加载top.sharehome.jvmloader.Math类的自定义加载器为：" + clazz2.getClassLoader().getClass().getName());
        Object obj2 = clazz2.newInstance();
        Method method2 = clazz2.getDeclaredMethod("computeAdditionAndMultiplyBy10", int.class, int.class);
        Object res2 = method2.invoke(obj2, 1, 1);
        System.out.println("调用Math对象的computeAdditionAndMultiplyBy10方法结果为：" + res2);
    }

}
