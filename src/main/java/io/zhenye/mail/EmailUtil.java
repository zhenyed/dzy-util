package io.zhenye.mail;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * 邮件工具类
 *
 * @author dingzhenye
 * @date 2021/12/03
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailUtil {

    @Data
    public static class EmailConfig {
        private String host;
        private String pass;
        private String port;
        private String auth;
        private String protocol;
        private String from;
    }

    /**
     * 发送邮件
     */
    public static boolean sendEmail(EmailConfig emailConfig, List<String> receiveList, List<String> ccList,
                                    String subject, String content,
                                    String embeddedImageName, String embeddedImagePath,
                                    String attachFileName, String attachFilePath) {
        try {
            EmailDTO emailDTO = new EmailDTO(emailConfig)
                    .setEmailAddress(emailConfig.getFrom(), receiveList, ccList)
                    .setMessage(subject, content);
            // 文本内嵌图片
            if (StringUtils.isNotBlank(embeddedImageName) && StringUtils.isNotBlank(embeddedImagePath)) {
                emailDTO.addImageInContent(embeddedImageName, embeddedImagePath);
            }
            // 附件
            if (StringUtils.isNotBlank(attachFileName) && StringUtils.isNotBlank(attachFilePath)) {
                emailDTO.addAttachment(attachFileName, attachFilePath);
            }
            emailDTO.send();
            return true;
        } catch (Exception e) {
            log.error("Send report email error.[Subject={}]", subject, e);
            return false;
        }
    }

    public static class EmailDTO {
        private final MimeMessage message;
        private final MimeMultipart multipart;

        public EmailDTO(EmailConfig emailConfig) throws MessagingException {
            Properties properties = System.getProperties();
            properties.put("mail.transport.protocol", emailConfig.getProtocol());
            properties.put("mail.smtp.host", emailConfig.getHost());
            properties.put("mail.smtp.port", emailConfig.getPort());
            properties.put("mail.smtp.auth", emailConfig.getAuth());

            // 获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailConfig.getFrom(), emailConfig.getPass());
                }
            });
            this.message = new MimeMessage(session);
            this.multipart = new MimeMultipart();
            this.message.setContent(multipart);
        }

        /**
         * 设置邮箱
         *
         * @param from        发件邮箱
         * @param receiveList 收件邮箱
         * @param ccList      抄送邮箱
         */
        public EmailDTO setEmailAddress(String from, List<String> receiveList, List<String> ccList) throws MessagingException {
            // 发送邮箱
            message.setFrom(new InternetAddress(from));
            // 收件邮箱
            for (String receive : receiveList) {
                message.addRecipients(Message.RecipientType.TO, receive);
            }
            // 抄送邮箱
            for (String cc : ccList) {
                message.addRecipients(Message.RecipientType.CC, cc);
            }
            return this;
        }

        /**
         * 设置主题 & 文本内容
         *
         * @param subject 主题
         * @param content 文本内容
         */
        public EmailDTO setMessage(String subject, String content) throws MessagingException {
            // 主题
            this.message.setSubject(subject);
            // 文本
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(content, "text/html;charset=UTF-8");
            this.multipart.addBodyPart(text);
            return this;
        }

        /**
         * 添加内容图片
         *
         * @param imageId  图片 ID（唯一标识）
         * @param imageUrl 图片路径
         */
        public EmailDTO addImageInContent(String imageId, String imageUrl) throws MessagingException {
            DataHandler dh = new DataHandler(new FileDataSource(imageUrl)); // 读取本地文件
            // DataHandler dh = new DataHandler(new ClassPathResource(imageUrl).getURL()); // 读取 Resource 的文件
            MimeBodyPart image = new MimeBodyPart();
            image.setDataHandler(dh);
            image.setContentID(imageId);
            this.multipart.addBodyPart(image);
            return this;
        }

        /**
         * 添加附件
         *
         * @param attachName 附件文件名
         * @param attachUrl  附件路径
         */
        public EmailDTO addAttachment(String attachName, String attachUrl) throws MessagingException, UnsupportedEncodingException {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(attachUrl);
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setFileName(MimeUtility.encodeText(attachName));
            this.multipart.addBodyPart(messageBodyPart);
            return this;
        }

        /**
         * 发送邮件
         */
        public void send() throws MessagingException {
            Transport.send(this.message);
        }
    }

}