# 从JDK源码级别彻底剖析JVM类加载机制

## 类加载器运行全过程

当`java`命令运行某个类的main()主函数启动程序时，首先会通过**类加载器**把主类加载到JVM中。例如下面这个类：

```java
package top.sharehome.jvmloader;

/**
 * 数学类
 *
 * @author AntonyCheng
 */
public class Math {
    public static final int INIT_NUM = 1900;
    /**
     * 外部类实例化
     */
    public static User user = new User();
    /**
     * 加法方法
     */
    public int computeAdditionAndMultiplyBy10(int a, int b) {
        int numA = a;
        int numB = b;
        int res = (numA + numB) * 10;
        return res;
    }
    /**
     * 主函数
     */
    public static void main(String[] args) {
        Math math = new Math();
        math.computeAdditionAndMultiplyBy10(10,10);
    }
}
```

通过`java`命令执行代码的大致流程如下：

![JVM加载类大致流程图](./assets/JVM加载类大致流程图.jpg)

其中最重要的一个方法就是loadClass()，它是加载类的核心方法，其中的运行过程有如下几步：

**加载 ==> 验证 ==> 准备 ==> 解析 ==> 初始化** ==> 使用 ==> 卸载

1. 加载：就是在硬盘上查找并通过磁盘I/O读取字节码文件。要注意**只有使用到类的时候才会加载，并且每个类只会被加载一次**，例如调用类的main()方法或者new新对象等。在加载阶段会在内存中生成一个代表这个类的java.lang.Class对象作为这个类在方法区上各种数据的访问入口。

2. 验证：校验字节码文件的正确性。

3. 准备：给类的静态变量分配内存并赋予默认值。

4. 解析：将**符号引用替换为直接引用**，该阶段会把一些静态方法，比如main()方法，替换为指向数据所存内存的指针或句柄，这就是所谓的**静态链接过程**（类加载期间完成）。还有一个**动态链接过程**，即在程序运行期间将符号引用替换为直接引用（这些下一章节会进行讨论）。如何理解这段话？我们需要前往字节码文件内容中具体看看，使用`javap -v Math.class`命令查看字节码文件部分内容：

   ```java
   ......
   public class top.sharehome.jvmloader.Math
     minor version: 0
     major version: 52
     flags: (0x0021) ACC_PUBLIC, ACC_SUPER
     this_class: #2                          // top/sharehome/jvmloader/Math
     super_class: #8                         // java/lang/Object
     interfaces: 0, fields: 2, methods: 4, attributes: 1
   Constant pool:
      #1 = Methodref          #8.#37         // java/lang/Object."<init>":()V
      #2 = Class              #38            // top/sharehome/jvmloader/Math
      #3 = Methodref          #2.#37         // top/sharehome/jvmloader/Math."<init>":()V
      #4 = Methodref          #2.#39         // top/sharehome/jvmloader/Math.computeAdditionAndMultiplyBy10:(II)I
      #5 = Class              #40            // top/sharehome/jvmloader/User
      #6 = Methodref          #5.#37         // top/sharehome/jvmloader/User."<init>":()V
      #7 = Fieldref           #2.#41         // top/sharehome/jvmloader/Math.user:Ltop/sharehome/jvmloader/User;
      #8 = Class              #42            // java/lang/Object
      #9 = Utf8               INIT_NUM
     #10 = Utf8               I
     #11 = Utf8               ConstantValue
     #12 = Integer            1900
     #13 = Utf8               user
     #14 = Utf8               Ltop/sharehome/jvmloader/User;
     #15 = Utf8               <init>
     #16 = Utf8               ()V
     #17 = Utf8               Code
     #18 = Utf8               LineNumberTable
     #19 = Utf8               LocalVariableTable
     #20 = Utf8               this
     #21 = Utf8               Ltop/sharehome/jvmloader/Math;
     #22 = Utf8               computeAdditionAndMultiplyBy10
     #23 = Utf8               (II)I
     #24 = Utf8               a
     #25 = Utf8               b
     #26 = Utf8               numA
     #27 = Utf8               numB
      ......
   {
     public static final int INIT_NUM;
       descriptor: I
       flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
       ConstantValue: int 1900
   
     public static top.sharehome.jvmloader.User user;
       descriptor: Ltop/sharehome/jvmloader/User;
       flags: (0x0009) ACC_PUBLIC, ACC_STATIC
   
     public top.sharehome.jvmloader.Math();
       descriptor: ()V
       flags: (0x0001) ACC_PUBLIC
       Code:
         stack=1, locals=1, args_size=1
            0: aload_0
            1: invokespecial #1                  // Method java/lang/Object."<init>":()V
            4: return
         LineNumberTable:
           line 8: 0
         LocalVariableTable:
           Start  Length  Slot  Name   Signature
               0       5     0  this   Ltop/sharehome/jvmloader/Math;
     ......
   }
   SourceFile: "Math.java"
   ```

   从上面内容可以看出每个所谓的符号（"numA"、"numB"等）都会被映射称为一个个代号（"#21"、"#22"等），而JVM把这个字节码文件加载进入内存之后就是通过一个个代号去查询可以直接访问的实际数据所在内存位置，这就是将符号引用转换成直接引用。

5. 初始化：对类的静态变量初始化为指定的值（例如"INIT_NUM = 1900"），并执行静态代码块。

loadClass()方法具体运行流程图如下：

