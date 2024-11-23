package top.sharehome.jvmloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 帮助打破双亲委派机制的类加载器
 *
 * @author AntonyCheng
 */
public class GodClassLoader extends ClassLoader {

    private final String classPath;

    public GodClassLoader(String classPath) {
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

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                // 打破双亲委派机制，选择直接注释掉这个部分的代码
                // try {
                //     if (parent != null) {
                //         c = parent.loadClass(name, false);
                //     } else {
                //         c = findBootstrapClassOrNull(name);
                //     }
                // } catch (ClassNotFoundException e) {
                //     // ClassNotFoundException thrown if class not found
                //     // from the non-null parent class loader
                // }

                long t1 = System.nanoTime();
                // 由于JVM安全校验机制存在，除了目标类的包下的类，其他类加载依旧遵循双亲委派机制
                if (name.startsWith("top.sharehome.jvmloader")) {
                    c = this.findClass(name);
                } else {
                    c = super.getParent().loadClass(name);
                }
                sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                sun.misc.PerfCounter.getFindClasses().increment();
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 这里需要把Math.class文件提前放在D:/Desktop/test/top/sharehome/jvmloader目录下
        GodClassLoader godClassLoader = new GodClassLoader("D:/Desktop/test");
        Class<?> clazz = godClassLoader.loadClass("top.sharehome.jvmloader.Math");
        System.out.println("加载top.sharehome.jvmloader.Math类的自定义加载器为：" + clazz.getClassLoader().getClass().getName());
        Object obj = clazz.newInstance();
        Method computeAdditionAndMultiplyBy10 = clazz.getDeclaredMethod("computeAdditionAndMultiplyBy10", int.class, int.class);
        Object res = computeAdditionAndMultiplyBy10.invoke(obj, 1, 1);
        System.out.println("调用Math对象的computeAdditionAndMultiplyBy10方法结果为：" + res);
    }
}
