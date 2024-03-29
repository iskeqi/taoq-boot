package tech.taoq.rbac.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import tech.taoq.rbac.domain.db.MenuDO;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuDto extends MenuDO {

    @ApiModelProperty("是否占用[true:占用 false:未占用]")
    private Boolean occupy;

    @ApiModelProperty("子级菜单列表")
    private List<MenuDto> childList;
}
