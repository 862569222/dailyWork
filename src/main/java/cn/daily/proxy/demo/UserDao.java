package cn.daily.proxy.demo;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 15:38
 */
public interface UserDao {
    void addUser(User user);

    void deleteUser(String name);

    void updateUser(String name);
}
