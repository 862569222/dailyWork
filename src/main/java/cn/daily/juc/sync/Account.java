package cn.daily.juc.sync;

/**
 * 模拟银行账户
 * 对业务写方法加锁
 * 对业务读方法不加锁
 * 这样行不行？
 *
 * 没有绝对的行不行，根据业务需求来定，如果允许短时间对数据的脏读就没有问题，对读方法加锁能够保证
 * 数据读取准确性 但是效率上大大降低 能不加最好
 */
public class Account {
    int account = 0;

    public synchronized int getAccount() {

        return account;
    }

    public synchronized void setAccount(int account) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.account = account;
    }

    public static void main(String[] args) {
        Account acc = new Account();
        new Thread(()->{
            acc.setAccount(100);
        }).start();


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("1查询余额"+Thread.currentThread().getName() + " :" + acc.getAccount());
        new Thread(()->{
            Thread.State state = Thread.currentThread().getState();
            System.out.println("1查询余额"+Thread.currentThread().getName() + " :" + acc.getAccount());
        }).start();
        System.out.println("当前线程状态："+Thread.currentThread().getState());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("查询余额"+Thread.currentThread().getName() + " :" + acc.getAccount());


    }
}
