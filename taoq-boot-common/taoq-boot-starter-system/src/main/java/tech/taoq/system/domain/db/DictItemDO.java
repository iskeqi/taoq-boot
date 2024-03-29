package tech.taoq.system.domain.db;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tech.taoq.mp.pojo.BaseDO;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_item")
public class DictItemDO extends BaseDO {

    @ApiModelProperty("字典项编码")
    private String itemCode;

    @ApiModelProperty("字典项名称")
    private String itemName;

    @ApiModelProperty("字典项值")
    private String itemValue;

    @ApiModelProperty("字典类型id")
    private String dictTypeId;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("排序字段")
    private Integer orderNum;

    @ApiModelProperty("是否系统内置[false:否 true:是]")
    @TableField(value = "is_internal")
    private Boolean internal;
}