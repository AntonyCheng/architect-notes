# JVM调优工具详解及调优实战

## 基础命令

在真正调优测试之前需要启动一个Java程序应用，在此使用[spring-boot-init-template项目的v1-jdk8&11分支](https://github.com/AntonyCheng/spring-boot-init-template/tree/v1-jdk8&11)将项目启动起来，如下图所示：

![image-20241213192855542](./assets/image-20241213192855542.png)

### jps

该命令用于查看该机器上所有的Java程序或者应用的线程ID：

![image-20241213193544918](./assets/image-20241213193544918.png)

### jmap

**内存信息**：

该命令可以用来查看内存信息、实体个数以及占用内存的大小，由于该命令输出内容巨多，现将其输出到`.txt`文件里：

![image-20241213194743507](./assets/image-20241213194743507.png)

1、num：序号

2、#instances：实例数量

3、#bytes：占用空间大小（单位：B）

4、class name：类名称，其中`[C`表示`char[]`，`[S`表示`short[]`，`[I`表示`int[]`，`[B`表示`byte[]`，`[[I`表示`int[][]`。

**堆信息**：

该命令可以用来查看堆信息：

![image-20241213195326979](./assets/image-20241213195326979.png)

**堆内存dump文件**：

该命令还可以辅助导出堆内存dump文件，这个文件可以用来分析说明一些问题，比如内存溢出。下面有一段可能触发内存溢出的代码：

```java
package top.sharehome.jvmcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 内存溢出示例代码
 * 运行命令参数：-Xms5M -Xmx5M -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:/jvm.dump
 *
 * @author AntonyCheng
 */
public class OomSample {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (true) {
            list.add(new User(i++, UUID.randomUUID().toString()));
            new User(j--, UUID.randomUUID().toString());
        }
    }

}

```

此时加上`-XX:+HeapDumpOnOutOfMemoryError`和`-XX:HeapDumpPath=D:/jvm.dump`参数就能在程序内存溢出时自动向目标文件夹导出日志文件，现在按照上述命令参数将该程序运行起来：

![image-20241213201615903](./assets/image-20241213201615903.png)

最终如愿抛出OOM异常，此时在D盘就会形成一个jvm.dump文件：

![image-20241213202036492](./assets/image-20241213202036492.png)
