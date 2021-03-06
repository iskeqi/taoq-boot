package tech.taoq.rbac.domain.db;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户-角色关联表
 *
 * @author keqi
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@TableName(value = "sys_account_role")
public class AccountRoleDO {

    @ApiModelProperty("用户唯一标识符")
    private String accountId;

    @ApiModelProperty("角色id")
    private String roleId;
}