package cn.daily.juc.volatiles;

public class V2 {
    private static volatile V2 vo = null;
    private V2(){}

    public static V2 getInstance() {

        //双重检查
        if(vo == null){//过滤掉大部分请求，减少申请锁消耗的资源 提高效率
            synchronized (V2.class){
                if(vo == null){
                    vo = new V2();
                }
            }

        }

        return vo;
    }


    public static void main(String[] args) {
        for (int i=0;i<100;i++){
            new Thread(()->{
                System.out.println(V2.getInstance().hashCode());
            }).start();
        }
    }
}
