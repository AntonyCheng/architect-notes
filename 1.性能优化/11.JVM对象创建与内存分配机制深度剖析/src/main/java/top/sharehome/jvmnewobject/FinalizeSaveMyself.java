package top.sharehome.jvmnewobject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * finalize()方法进行自救
 *
 * @author AntonyCheng
 */
public class FinalizeSaveMyself {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (true) {
            list.add(new Me(i++, UUID.randomUUID().toString()));
            new Me(j--, UUID.randomUUID().toString());
        }
    }

}

class Me {
    private int id;
    private String name;

    public Me(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "id=" + id + ",name=" + name;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println(this);
    }

}