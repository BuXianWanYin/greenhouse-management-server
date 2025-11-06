package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgricultureRotationPlan;
import com.server.service.AgricultureRotationPlanService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;
import com.server.domain.dto.AgricultureCropBatchDTO;

/**
 * 轮作计划Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/rotationplan")
public class AgricultureRotationPlanController extends BaseController
{
    @Autowired
    private AgricultureRotationPlanService agricultureRotationPlanService;

    /**
     * 查询轮作计划列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationplan:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureRotationPlan agricultureRotationPlan)
    {
        startPage();
        List<AgricultureRotationPlan> list = agricultureRotationPlanService.selectAgricultureRotationPlanList(agricultureRotationPlan);
        return getDataTable(list);
    }

    /**
     * 导出轮作计划列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationplan:export')")
    @Log(title = "轮作计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureRotationPlan agricultureRotationPlan)
    {
        List<AgricultureRotationPlan> list = agricultureRotationPlanService.selectAgricultureRotationPlanList(agricultureRotationPlan);
        ExcelUtil<AgricultureRotationPlan> util = new ExcelUtil<AgricultureRotationPlan>(AgricultureRotationPlan.class);
        util.exportExcel(response, list, "轮作计划数据");
    }

    /**
     * 获取轮作计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationplan:query')")
    @GetMapping(value = "/{rotationId}")
    public AjaxResult getInfo(@PathVariable("rotationId") Long rotationId)
    {
        return success(agricultureRotationPlanService.selectAgricultureRotationPlanByRotationId(rotationId));
    }

    /**
     * 新增轮作计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationplan:add')")
    @Log(title = "轮作计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureRotationPlan agricultureRotationPlan)
    {
        return toAjax(agricultureRotationPlanService.insertAgricultureRotationPlan(agricultureRotationPlan));
    }

    /**
     * 修改轮作计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationplan:edit')")
    @Log(title = "轮作计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureRotationPlan agricultureRotationPlan)
    {
        return toAjax(agricultureRotationPlanService.updateAgricultureRotationPlan(agricultureRotationPlan));
    }

    /**
     * 删除轮作计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationplan:remove')")
    @Log(title = "轮作计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rotationIds}")
    public AjaxResult remove(@PathVariable Long[] rotationIds)
    {
        return toAjax(agricultureRotationPlanService.deleteAgricultureRotationPlanByRotationIds(rotationIds));
    }

    /**
     * 获取轮作计划关联的批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationplan:query')")
    @GetMapping("/{rotationId}/batches")
    public AjaxResult getRotationPlanBatches(@PathVariable("rotationId") Long rotationId)
    {
        List<AgricultureCropBatchDTO> batchList = agricultureRotationPlanService.getRotationPlanBatches(rotationId);
        return success(batchList);
    }
}

