package tech.taoq.mail.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tech.taoq.common.exception.client.ParamIllegalException;
import tech.taoq.common.pojo.PageDto;
import tech.taoq.common.util.JsonUtil;
import tech.taoq.mail.domain.db.MailDO;
import tech.taoq.mail.mapper.MailMapper;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MailService
 *
 * @author keqi
 */
@Slf4j
@Service
//@CacheConfig(cacheNames = "sys:mail")
public class MailService {

    @Autowired
    private MailMapper mailMapper;
    @Autowired
    private MailService mailService;

    public static final Map<String, JavaMailSenderImpl> JAVA_MAIL_SENDERS = new ConcurrentHashMap<>();

    public MailDO insert(MailDO param) {
        MailDO t = mailMapper.selectOne(Wrappers.query(new MailDO().setIdentifier(param.getIdentifier())));
        if (t != null) {
            throw new ParamIllegalException("A record with identifier " + param.getIdentifier() + " already exists");
        }

        mailMapper.insert(param);
        return param;
    }

    // @CacheEvict(key = "#identifier")
    public void deleteByIdentifier(String identifier) {
        mailMapper.delete(Wrappers.query(new MailDO().setIdentifier(identifier)));

        JAVA_MAIL_SENDERS.remove(identifier);
    }

    // @CacheEvict(key = "#param.identifier")
    public void updateByIdentifier(MailDO param) {
        MailDO t = new MailDO().setIdentifier(param.getIdentifier());

        param.setId(null);
        param.setIdentifier(null);
        mailMapper.update(param, Wrappers.query(t));

        JAVA_MAIL_SENDERS.remove(param.getIdentifier());
    }

    // @Cacheable(key = "#identifier")
    public MailDO getByIdentifier(String identifier) {
        return mailMapper.selectOne(Wrappers.query(new MailDO()
                .setIdentifier(identifier)
                .setDisable(true)));
    }

    public PageDto<MailDO> page(Page<MailDO> param) {
        Page<MailDO> page = mailMapper.selectPage(param, Wrappers.lambdaQuery(MailDO.class)
                .orderByDesc((SFunction<MailDO, Object>) MailDO::getPriority));
        return new PageDto<>(page.getTotal(), page.getRecords());
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param identifier identifier
     * @return r
     */
    public MailDO isConnect(String identifier) {
        JavaMailSenderImpl javaMailSender = this.getOneByIdentifier(identifier);

        boolean isConnect = true;
        try {
            javaMailSender.testConnection();
        } catch (MessagingException e) {
            isConnect = false;
            log.error("Mail server is not available " + e.getMessage(), e);
        }

        mailService.updateByIdentifier(new MailDO()
                .setConnect(isConnect ? MailDO.Connect.CONNECT.getCode() : MailDO.Connect.NOT_CONNECT.getCode())
                .setIdentifier(identifier));

        return mailService.getByIdentifier(identifier);
    }

    /**
     * ?????? identifier ??????????????? JavaMailSenderImpl ??????
     *
     * @param identifier identifier
     * @return r
     */
    public JavaMailSenderImpl getOneByIdentifier(String identifier) {
        JavaMailSenderImpl javaMailSender = JAVA_MAIL_SENDERS.get(identifier);
        if (javaMailSender == null) {
            javaMailSender = this.createJavaMailSender(identifier);
            JAVA_MAIL_SENDERS.put(identifier, javaMailSender);
        }

        return javaMailSender;
    }

    /**
     * ?????? JavaMailSenderImpl ??????
     *
     * @param identifier identifier
     * @return r
     */
    public JavaMailSenderImpl createJavaMailSender(String identifier) {
        MailDO mailDO = mailMapper.selectOne(Wrappers.query(new MailDO().setIdentifier(identifier)));
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        MailProperties mailProperties = new MailProperties();
        mailProperties.setHost(mailDO.getHost());
        mailProperties.setPort(mailDO.getPort());
        mailProperties.setUsername(mailDO.getUsername());
        mailProperties.setPassword(mailDO.getPassword());

        javaMailSender.setHost(mailProperties.getHost());
        if (mailProperties.getPort() != null) {
            javaMailSender.setPort(mailProperties.getPort());
        }
        javaMailSender.setUsername(mailProperties.getUsername());
        javaMailSender.setPassword(mailProperties.getPassword());
        javaMailSender.setProtocol(mailProperties.getProtocol());
        javaMailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().name());
        if (mailDO.getProperties() != null) {
            Properties properties = new Properties();
            properties.putAll(JsonUtil.readValue(JsonUtil.writeValueAsString(mailDO.getProperties()), HashMap.class));
            javaMailSender.setJavaMailProperties(properties);
        }

        return javaMailSender;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @return r
     */
    public String getOneByPriority() {
        List<MailDO> mailDOList = mailMapper.selectList(Wrappers.lambdaQuery(MailDO.class)
                .orderByDesc((SFunction<MailDO, Integer>) MailDO::getPriority));
        for (MailDO mailDO : mailDOList) {
            MailDO connect = isConnect(mailDO.getIdentifier());
            if (MailDO.Connect.CONNECT.getCode().equals(connect.getConnect())) {
                return mailDO.getIdentifier();
            }
        }
        return null;
    }
}
