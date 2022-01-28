package com.coisini.mq;

import com.coisini.config.MailConfig;
import com.coisini.config.RabbitMqConfig;
import com.coisini.model.MailMessage;
import com.coisini.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @Description 发送邮件的队列消费者
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Component
@RabbitListener(queues = RabbitMqConfig.MAIL_QUEUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailListener {

    private Logger logger = LoggerUtil.loggerFactory(this.getClass());

    private final JavaMailSender mailSender;

    private final MailMessage mailMessage;

    @RabbitHandler
    public void executeSms(Map<String, String> map) {
        String mail = map.get("mail");
        String code = map.get("code");

        try {
            this.sendMail(mail, code);
            logger.info(mail + "-" + code + "-发送成功");
        } catch (Exception e) {
            logger.error(mail + code + "发送失败-" + e.getMessage());
        }
    }

    private void sendMail(String mail, String code) {
        // TODO 发送邮件
        mailSender.send(mailMessage
                .create(mail, "邮箱验证码", "邮箱验证码：" + code + "，" + MailConfig.EXPIRED_TIME + "分钟内有效"));
    }
}
