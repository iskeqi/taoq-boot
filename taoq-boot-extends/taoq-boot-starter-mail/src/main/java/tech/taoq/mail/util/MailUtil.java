package tech.taoq.mail.util;

import tech.taoq.mail.domain.param.AttachmentMailParam;
import tech.taoq.mail.domain.param.InlineMailParam;
import tech.taoq.mail.domain.param.SimpleMailParam;
import tech.taoq.mail.exception.MailException;
import tech.taoq.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * MailUtil
 *
 * @author keqi
 */
@Slf4j
@Component
public class MailUtil {

    private static MailService mailService;

    @Autowired
    public static void setMailService(MailService mailService) {
        MailUtil.mailService = mailService;
    }

    /**
     * 发送简单的文本邮件
     *
     * @param simpleMailParam simpleMailParam
     */
    public static void sendSimpleMail(SimpleMailParam simpleMailParam) {
        String identifier = mailService.getOneByPriority();
        if (identifier == null) {
            throw new MailException("there is currently no service that can send mail");
        } else {
            sendSimpleMail(simpleMailParam, identifier);
        }
    }

    /**
     * 发送简单的文本邮件
     *
     * @param simpleMailParam simpleMailParam
     * @param identifier      identifiers
     */
    public static void sendSimpleMail(SimpleMailParam simpleMailParam, String identifier) {
        JavaMailSenderImpl javaMailSender = mailService.getOneByIdentifier(identifier);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(simpleMailParam.getTo());
        message.setSubject(simpleMailParam.getSubject());
        message.setText(simpleMailParam.getText());
        message.setCc(simpleMailParam.getCc());
        message.setBcc(simpleMailParam.getBcc());
        message.setSentDate(simpleMailParam.getSentDate());
        message.setReplyTo(simpleMailParam.getReplyTo());

        javaMailSender.send(message);
    }

    /**
     * 发送带有附件的简单文本电子邮件
     *
     * @param attachmentMailParam attachmentMailParam
     */
    public static void sendAttachmentMail(AttachmentMailParam attachmentMailParam) {
        String identifier = mailService.getOneByPriority();
        if (identifier == null) {
            throw new MailException("there is currently no service that can send mail");
        } else {
            sendAttachmentMail(attachmentMailParam, identifier);
        }
    }

    /**
     * 发送带有附件的简单文本电子邮件
     *
     * @param attachmentMailParam attachmentMailParam
     * @param identifier          identifier
     */
    public static void sendAttachmentMail(AttachmentMailParam attachmentMailParam, String identifier) {
        JavaMailSenderImpl javaMailSender = mailService.getOneByIdentifier(identifier);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(attachmentMailParam.getTo());
            helper.setSubject(attachmentMailParam.getSubject());
            helper.setText(attachmentMailParam.getText());
            helper.setCc(attachmentMailParam.getCc());
            helper.setBcc(attachmentMailParam.getBcc());
            helper.setSentDate(attachmentMailParam.getSentDate());
            helper.setReplyTo(attachmentMailParam.getReplyTo());

            for (AttachmentMailParam.Attachment attachment : attachmentMailParam.getAttachmentList()) {
                if (attachment.getFile() != null) {
                    helper.addAttachment(attachment.getAttachmentFilename(), attachment.getFile());
                    continue;
                }
                if (attachment.getDataSource() != null) {
                    helper.addAttachment(attachment.getAttachmentFilename(), attachment.getDataSource());
                    continue;
                }
                if (attachment.getInputStreamSource() != null) {
                    if (attachment.getContentType() != null) {
                        helper.addAttachment(attachment.getAttachmentFilename(),
                                attachment.getInputStreamSource(), attachment.getContentType());
                    } else {
                        helper.addAttachment(attachment.getAttachmentFilename(), attachment.getInputStreamSource());
                    }
                }
            }

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new MailException(e.getMessage());
        }

        javaMailSender.send(message);
    }

    /**
     * 发送带有附件和内联文件的简单文本电子邮件
     *
     * @param inlineMailParam inlineMailParam
     */
    public static void sendInlineMail(InlineMailParam inlineMailParam) {
        String identifier = mailService.getOneByPriority();
        if (identifier == null) {
            throw new MailException("there is currently no service that can send mail");
        } else {
            sendInlineMail(inlineMailParam, identifier);
        }
    }

    /**
     * 发送带有附件和内联文件的简单文本电子邮件
     *
     * @param inlineMailParam inlineMailParam
     * @param identifier      identifier
     */
    public static void sendInlineMail(InlineMailParam inlineMailParam, String identifier) {
        JavaMailSenderImpl javaMailSender = mailService.getOneByIdentifier(identifier);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(inlineMailParam.getTo());
            helper.setSubject(inlineMailParam.getSubject());
            helper.setText(inlineMailParam.getText());
            helper.setCc(inlineMailParam.getCc());
            helper.setBcc(inlineMailParam.getBcc());
            helper.setSentDate(inlineMailParam.getSentDate());
            helper.setReplyTo(inlineMailParam.getReplyTo());

            for (AttachmentMailParam.Attachment attachment : inlineMailParam.getAttachmentList()) {
                if (attachment.getFile() != null) {
                    helper.addAttachment(attachment.getAttachmentFilename(), attachment.getFile());
                    continue;
                }
                if (attachment.getDataSource() != null) {
                    helper.addAttachment(attachment.getAttachmentFilename(), attachment.getDataSource());
                    continue;
                }
                if (attachment.getInputStreamSource() != null) {
                    if (attachment.getContentType() != null) {
                        helper.addAttachment(attachment.getAttachmentFilename(),
                                attachment.getInputStreamSource(), attachment.getContentType());
                    } else {
                        helper.addAttachment(attachment.getAttachmentFilename(), attachment.getInputStreamSource());
                    }
                }
            }

            for (InlineMailParam.Inline inline : inlineMailParam.getInlineList()) {
                if (inline.getFile() != null) {
                    helper.addInline(inline.getContentId(), inline.getFile());
                    continue;
                }
                if (inline.getDataSource() != null) {
                    helper.addInline(inline.getContentId(), inline.getDataSource());
                    continue;
                }
                if (inline.getResource() != null) {
                    helper.addInline(inline.getContentId(), inline.getResource());
                    continue;
                }
                if (inline.getInputStreamSource() != null && inline.getContentType() != null) {
                    helper.addInline(inline.getContentId(),
                            inline.getInputStreamSource(), inline.getContentType());
                }
            }

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new MailException(e.getMessage());
        }

        javaMailSender.send(message);
    }

}
