package com.unicom.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unicom.admin.dto.UmsAdminLoginVo;
import com.unicom.admin.service.IUmsAdminService;
import com.unicom.common.api.ResultUtils;
import com.unicom.generator.entity.UmsAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ctf
 * @since 2020-05-07
 */
@RestController
@Api(tags = "UmsAdminController",description = "后台用户管理")
@RequestMapping("/umsAdmin")
public class UmsAdminController {
        @Autowired
        private IUmsAdminService iUmsAdminService;

        @Value("${jwt.tokenHead}")
        private  String tokenHead;
        @ApiOperation(value = "测试分页")
        @RequestMapping(value = "/{current}/{size}",method = RequestMethod.GET)
        public ResultUtils test(@PathVariable long current,@PathVariable long size){
                Page<UmsAdmin> page=new Page<>(current,size);
                QueryWrapper<UmsAdmin> queryWrapper=new QueryWrapper<>();
                IPage<UmsAdmin> pages=iUmsAdminService.page(page,queryWrapper);
                return  ResultUtils.success(pages);
        }

        @ApiOperation(value = "登录获得token")
        @RequestMapping(value = "/login",method = RequestMethod.POST)
        public ResultUtils login(@Valid @RequestBody(required = false) UmsAdminLoginVo umsAdminLoginVo){
                String token=iUmsAdminService.login(umsAdminLoginVo.getUsername(),umsAdminLoginVo.getPassword());
                Map<String,Object> tokenMap=new HashMap<>(2);
                tokenMap.put("token",token);
                tokenMap.put("tokenHead",tokenHead);
                return ResultUtils.success(tokenMap);
        }

        @ApiOperation(value = "按列来筛选字段")
        @RequestMapping(value = "/selectByColumn",method = RequestMethod.POST)
        public ResultUtils selectByColumn(@RequestBody List<String> ips){
                List<Map<String,Object>> umsAdmins=iUmsAdminService.selectByColumn(ips);

                return ResultUtils.success(umsAdmins);
        }
        @ApiOperation(value = "按列来筛选字段")
        @RequestMapping(value = "/selectByColumnObject",method = RequestMethod.POST)
        public ResultUtils selectByColumnObject(@RequestBody List<String> ips){
                List<Object> umsAdmins=iUmsAdminService.selectByColumnObject(ips);

                return ResultUtils.success(umsAdmins);
        }
}
