package effectivejava.chapter6.item34;


/**
 * @author R.oldmee
 * @date 2019/2/19 5:59 PM
 */
public class ElseIfTest {
    @org.junit.Test
    public void test() {
        int a = 52;
        if (a > 10 && a < 30) {
            System.out.println("1"); //  一旦满足一个条件，后面的全部不执行
        } else if (a > 15 && a < 50) {
            System.out.println("2"); // 前面的不满足才会执行到这里，一旦这里满足，后面的判断全都不执行
        } else if (a > 20 && a < 55) {
            System.out.println("3");
        } else {
            System.out.println("else");
        }
    }

    @org.junit.Test
    public void test2() {
        int a = 3;
        switch (a) {
            case 0 :
                System.out.println(0);
                break;
            case 1 :
                System.out.println(1);
            case 2 :
                System.out.println(2);
            case 3 :
                System.out.println(3);
        }
    }
}
