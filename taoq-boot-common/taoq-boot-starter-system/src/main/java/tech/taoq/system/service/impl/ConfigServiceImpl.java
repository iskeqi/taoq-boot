package tech.taoq.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.taoq.common.exception.client.ParamIllegalException;
import tech.taoq.common.pojo.PageDto;
import tech.taoq.system.domain.db.ConfigDO;
import tech.taoq.system.mapper.ConfigMapper;
import tech.taoq.system.service.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    public void insert(ConfigDO param) {
        ConfigDO t = configMapper.selectOne(Wrappers.query(new ConfigDO()
                .setConfigKey(param.getConfigKey())));
        if (t != null) {
            throw new ParamIllegalException("configKey：" + param.getConfigKey() + " 已经存在");
        }

        configMapper.insert(param);
    }

    public void deleteById(String id) {
        configMapper.deleteById(id);
    }

    public void updateById(ConfigDO param) {
        ConfigDO t1 = BeanUtil.copyProperties(param, ConfigDO.class);
        // configKey 是不能修改的
        t1.setConfigKey(null);
        t1.setCreateTime(null);
        configMapper.updateById(param);
    }

    public ConfigDO getById(String id) {
        return configMapper.selectById(id);
    }

    public PageDto<ConfigDO> page(Page<ConfigDO> param) {
        Page<ConfigDO> page = configMapper.selectPage(param, Wrappers.query());
        return new PageDto<>(page.getTotal(), page.getRecords());
    }
}
