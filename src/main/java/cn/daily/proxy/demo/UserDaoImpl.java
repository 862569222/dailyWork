package cn.daily.proxy.demo;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 15:39
 */
public class UserDaoImpl implements UserDao {
    @Override
    public void addUser(User user) {
        System.out.println("add user named " + user.getName() + "...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String name) {
        System.out.println("delete user named " + name + "...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(String name) {
        System.out.println("update user named " + name + "...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
