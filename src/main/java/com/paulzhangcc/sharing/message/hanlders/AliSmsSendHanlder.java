package com.paulzhangcc.sharing.message.hanlders;

import com.paulzhangcc.sharing.annotation.NotThreadSafe;
import com.paulzhangcc.sharing.message.model.AliSms;
import com.paulzhangcc.sharing.message.model.InsertDB;
import com.paulzhangcc.sharing.message.queue.TaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.paulzhangcc.sharing.message.queue.QueueEnum.SMS_INSERTDB;

/**
 * 阿里云短信发送处理类
 */
@NotThreadSafe
public class AliSmsSendHanlder extends AbstractSmsSendHanlder {

    private static Logger logger = LoggerFactory.getLogger(AliSmsSendHanlder.class);
    private AliSms aliSms;

    public AliSmsSendHanlder(AliSms sms) {
        this.aliSms = sms;
    }

    @Override
    protected String name() {
        return "阿里云";
    }

    @Override
    protected Object content() {
        return aliSms;
    }

    @Override
    public boolean send() {
        return sendToAiyun();
    }

    @Override
    protected void success() {
        super.success();
        if (aliSms instanceof InsertDB) {
            TaskQueue.put(new SmsPutDbHandler(aliSms.insert()), SMS_INSERTDB);
        }
    }

    private boolean sendToAiyun(){
        /**
         * TODO
         */
        logger.info("短信发送往阿里云请求:"+ aliSms);
        return true;
    }
}