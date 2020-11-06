package cn.com.qucl.common.utils;

import cn.com.qucl.common.exceptions.CheckArgumentException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.regex.Pattern;

/**
 * @author qucl
 * @date 2018/11/20 10:02
 */
public class EmailUtils {
    private final static String EMAIL_PATTERN = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private JavaMailSender mailSender;
    private Pattern pattern;

    private EmailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public static EmailUtils getInstance(JavaMailSender mailSender) {
        return new EmailUtils(mailSender);
    }

    /**
     * 发送简单文本邮件
     *
     * @param fromEmail   发件邮箱
     * @param sendToEmail 目标邮箱
     * @param theme       主题
     * @param content     内容
     */
    public void sendSimpleEmail(String fromEmail, String sendToEmail, String theme, String content) throws CheckArgumentException {
        checkEmail(fromEmail);
        checkEmail(sendToEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(sendToEmail);
        message.setSubject(theme);
        message.setText(content);

        mailSender.send(message);
    }

    /**
     * 发送html格式邮件
     *
     * @param fromEmail   发件邮箱
     * @param sendToEmail 目标邮箱
     * @param theme       主题
     * @param html        html内容
     */
    public void sendHtmlEmail(String fromEmail, String sendToEmail, String theme, String html) throws CheckArgumentException, javax.mail.MessagingException {
        checkEmail(fromEmail);
        checkEmail(sendToEmail);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(sendToEmail);
        helper.setSubject(theme);

        helper.setText(html, true);
        mailSender.send(message);
    }

    /**
     * 检查邮箱格式
     *
     * @param email 邮箱地址
     */
    private void checkEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new CheckArgumentException("邮箱地址为空");
        }
        if (!pattern.matcher(email).matches()) {
            throw new CheckArgumentException("邮箱地址格式不正确");
        }
    }
}
