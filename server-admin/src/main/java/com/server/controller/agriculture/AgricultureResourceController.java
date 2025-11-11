package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureResource;
import com.server.enums.BusinessType;
import com.server.enums.SeeMessageType;
import com.server.service.AgricultureResourceService;
import com.server.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 农资资源Controller
 * 
 * @author server
 * @date 2025-01-XX
 */
@RestController
@RequestMapping("/agriculture/resource")
@Api(tags = "农资资源管理")
public class AgricultureResourceController extends BaseController {

    @Autowired
    private AgricultureResourceService agricultureResourceService;

    /**
     * 查询农资资源列表
     */
    @GetMapping("/list")
    @ApiOperation("查询农资资源列表")
    public TableDataInfo list(AgricultureResource agricultureResource) {
        startPage();
        return getDataTable(agricultureResourceService.selectAgricultureResourceList(agricultureResource));
    }

    /**
     * 获取农资资源详细信息
     */
    @GetMapping("/{resourceId}")
    @ApiOperation("获取农资资源详细信息")
    public AjaxResult getInfo(@PathVariable("resourceId") Long resourceId) {
        return success(agricultureResourceService.getById(resourceId));
    }

    /**
     * 新增农资资源
     */
    @Log(title = "农资资源管理", businessType = BusinessType.INSERT)
    @PostMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("新增农资资源")
    public AjaxResult add(@RequestBody AgricultureResource agricultureResource) {
        return toAjax(agricultureResourceService.addAgricultureResource(agricultureResource));
    }

    /**
     * 修改农资资源
     */
    @Log(title = "农资资源管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("修改农资资源")
    public AjaxResult edit(@RequestBody AgricultureResource agricultureResource) {
        return toAjax(agricultureResourceService.updateAgricultureResource(agricultureResource));
    }

    /**
     * 删除农资资源
     */
    @Log(title = "农资资源管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resourceIds}")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("删除农资资源")
    public AjaxResult remove(@PathVariable Long[] resourceIds) {
        return toAjax(agricultureResourceService.removeByIds(java.util.Arrays.asList(resourceIds)));
    }

    /**
     * 导出农资资源列表
     */
    @Log(title = "农资资源管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出农资资源列表")
    public void export(HttpServletResponse response, AgricultureResource agricultureResource) {
        List<AgricultureResource> list = agricultureResourceService.selectAgricultureResourceList(agricultureResource);
        ExcelUtil<AgricultureResource> util = new ExcelUtil<AgricultureResource>(AgricultureResource.class);
        util.exportExcel(response, list, "农资资源数据");
    }
}

