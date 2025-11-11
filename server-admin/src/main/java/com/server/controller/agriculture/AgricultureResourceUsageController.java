package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureResourceUsage;
import com.server.enums.BusinessType;
import com.server.enums.SeeMessageType;
import com.server.service.AgricultureResourceUsageService;
import com.server.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 农资使用记录Controller
 * 
 * @author server
 * @date 2025-01-XX
 */
@RestController
@RequestMapping("/agriculture/resource/usage")
@Api(tags = "农资使用管理")
public class AgricultureResourceUsageController extends BaseController {

    @Autowired
    private AgricultureResourceUsageService agricultureResourceUsageService;

    /**
     * 查询农资使用记录列表
     */
    @GetMapping("/list")
    @ApiOperation("查询农资使用记录列表")
    public TableDataInfo list(AgricultureResourceUsage agricultureResourceUsage) {
        startPage();
        return getDataTable(agricultureResourceUsageService.selectAgricultureResourceUsageList(agricultureResourceUsage));
    }

    /**
     * 根据批次ID查询使用记录
     */
    @GetMapping("/batch/{batchId}")
    @ApiOperation("根据批次ID查询使用记录")
    public AjaxResult getByBatchId(@PathVariable("batchId") Long batchId) {
        return success(agricultureResourceUsageService.selectByBatchId(batchId));
    }

    /**
     * 根据任务ID查询使用记录
     */
    @GetMapping("/task/{taskId}")
    @ApiOperation("根据任务ID查询使用记录")
    public AjaxResult getByTaskId(@PathVariable("taskId") Long taskId) {
        return success(agricultureResourceUsageService.selectByTaskId(taskId));
    }

    /**
     * 获取农资使用记录详细信息
     */
    @GetMapping("/{usageId}")
    @ApiOperation("获取农资使用记录详细信息")
    public AjaxResult getInfo(@PathVariable("usageId") Long usageId) {
        return success(agricultureResourceUsageService.getById(usageId));
    }

    /**
     * 新增农资使用记录（自动扣减库存）
     */
    @Log(title = "农资使用管理", businessType = BusinessType.INSERT)
    @PostMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("新增农资使用记录")
    public AjaxResult add(@RequestBody AgricultureResourceUsage agricultureResourceUsage) {
        return toAjax(agricultureResourceUsageService.addAgricultureResourceUsage(agricultureResourceUsage));
    }

    /**
     * 修改农资使用记录
     */
    @Log(title = "农资使用管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("修改农资使用记录")
    public AjaxResult edit(@RequestBody AgricultureResourceUsage agricultureResourceUsage) {
        return toAjax(agricultureResourceUsageService.updateAgricultureResourceUsage(agricultureResourceUsage));
    }

    /**
     * 删除农资使用记录
     */
    @Log(title = "农资使用管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{usageIds}")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("删除农资使用记录")
    public AjaxResult remove(@PathVariable Long[] usageIds) {
        return toAjax(agricultureResourceUsageService.removeByIds(java.util.Arrays.asList(usageIds)));
    }

    /**
     * 导出农资使用记录列表
     */
    @Log(title = "农资使用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出农资使用记录列表")
    public void export(HttpServletResponse response, AgricultureResourceUsage agricultureResourceUsage) {
        List<AgricultureResourceUsage> list = agricultureResourceUsageService.selectAgricultureResourceUsageList(agricultureResourceUsage);
        ExcelUtil<AgricultureResourceUsage> util = new ExcelUtil<AgricultureResourceUsage>(AgricultureResourceUsage.class);
        util.exportExcel(response, list, "农资使用记录数据");
    }
}

