package tech.taoq.rbac.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.taoq.rbac.domain.dto.MenuDto;
import tech.taoq.rbac.service.MenuService;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation("查询指定角色关联的菜单")
    @PostMapping("/listByRoleId/{roleId}")
    public List<MenuDto> listByRoleId(@PathVariable String roleId) {
        return menuService.listByRoleId(roleId);
    }

    @ApiOperation("查询指定用户关联的菜单")
    @PostMapping("/listByAccount/{account}")
    public List<MenuDto> listByAccount(@PathVariable String account) {
        return menuService.listByAccount(account);
    }

    @ApiOperation("查询所有菜单")
    @PostMapping("/listMenus")
    public List<MenuDto> listMenus() {
        return menuService.listMenus();
    }

}
