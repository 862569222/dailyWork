package cn.mq.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2021/7/8 15:59
 */

@Slf4j
@RestController
public class RocketMQController {

    @Autowired
    private Productor producer;

    private List<String> mesList;

    /**
     * 初始化消息
     */
    public RocketMQController() {
        mesList = new ArrayList<>();
        mesList.add("西门吹雪");
        mesList.add("叶孤城");
        mesList.add("陆小凤");
        mesList.add("李寻欢");
        mesList.add("楚留香");

    }

    @RequestMapping("/text/rocketmq")
    public Object callback() throws Exception {
        //总共发送五次消息
        for (String s : mesList) {
            //创建生产信息
            Message message = new Message(JMSconfig.TOPIC, "testtag", ("传说中的大侠:" + s).getBytes());
            //发送
            SendResult sendResult = producer.getProducer().send(message);
            log.info("输出生产者信息={}",sendResult);
        }
        return "成功";
    }
}
