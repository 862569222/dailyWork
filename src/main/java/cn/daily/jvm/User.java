package cn.daily.jvm;


public class User {
    private int id ;
    private String name;

    public User(int id, String name) {
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
    public void finalize(){
        System.out.println("回收资源 user:"+id+"即将被回收。");
    }
}
