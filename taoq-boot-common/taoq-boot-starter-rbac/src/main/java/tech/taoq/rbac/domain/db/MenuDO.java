package tech.taoq.rbac.domain.db;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import tech.taoq.mp.pojo.BaseDO;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class MenuDO extends BaseDO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("请求url地址")
    private String url;

    @ApiModelProperty("icon图标")
    private String icon;

    @ApiModelProperty("菜单类型[C:目录 M:菜单 B:按钮]")
    private String type;

    @ApiModelProperty("权限标识")
    private String permiss;

    @ApiModelProperty("父级id[根节点parent_id为0]")
    private String parentId;

    @ApiModelProperty("排序字段")
    private Integer orderNum;

    @ApiModelProperty("菜单功能类型[N:内置 Z:自定义]")
    private String funcType;

    public enum Type {
        C, M, B
    }
}