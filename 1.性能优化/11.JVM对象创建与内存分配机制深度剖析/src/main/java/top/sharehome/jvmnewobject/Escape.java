package top.sharehome.jvmnewobject;

/**
 * 演示对象逃逸
 *
 * @author AntonyCheng
 */
public class Escape {

    /**
     * new User()对象逃逸成功
     */
    public User escape() {
        User user = new User();
        user.setId(1);
        user.setName("AntonyCheng");
        return user;
    }

    /**
     * new User()对象逃逸失败
     */
    public void noEscape(){
        User user = new User();
        user.setId(1);
        user.setName("AntonyCheng");
    }

    public static void main(String[] args) {
        Escape escape = new Escape();
        // 一定要被引用才能被视为逃逸成功
        User escapedUser = escape.escape();
        escape.noEscape();;
    }

}
