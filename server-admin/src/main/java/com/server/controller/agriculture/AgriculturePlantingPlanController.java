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
import com.server.domain.AgriculturePlantingPlan;
import com.server.domain.AgricultureCropBatch;
import com.server.service.AgriculturePlantingPlanService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 种植计划Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/plantingplan")
public class AgriculturePlantingPlanController extends BaseController
{
    @Autowired
    private AgriculturePlantingPlanService agriculturePlantingPlanService;

    /**
     * 查询种植计划列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriculturePlantingPlan agriculturePlantingPlan)
    {
        startPage();
        List<AgriculturePlantingPlan> list = agriculturePlantingPlanService.selectAgriculturePlantingPlanList(agriculturePlantingPlan);
        return getDataTable(list);
    }

    /**
     * 导出种植计划列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:export')")
    @Log(title = "种植计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgriculturePlantingPlan agriculturePlantingPlan)
    {
        List<AgriculturePlantingPlan> list = agriculturePlantingPlanService.selectAgriculturePlantingPlanList(agriculturePlantingPlan);
        ExcelUtil<AgriculturePlantingPlan> util = new ExcelUtil<AgriculturePlantingPlan>(AgriculturePlantingPlan.class);
        util.exportExcel(response, list, "种植计划数据");
    }

    /**
     * 获取种植计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId)
    {
        return success(agriculturePlantingPlanService.selectAgriculturePlantingPlanByPlanId(planId));
    }

    /**
     * 新增种植计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:add')")
    @Log(title = "种植计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriculturePlantingPlan agriculturePlantingPlan)
    {
        return toAjax(agriculturePlantingPlanService.insertAgriculturePlantingPlan(agriculturePlantingPlan));
    }

    /**
     * 修改种植计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:edit')")
    @Log(title = "种植计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgriculturePlantingPlan agriculturePlantingPlan)
    {
        return toAjax(agriculturePlantingPlanService.updateAgriculturePlantingPlan(agriculturePlantingPlan));
    }

    /**
     * 删除种植计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:remove')")
    @Log(title = "种植计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds)
    {
        return toAjax(agriculturePlantingPlanService.deleteAgriculturePlantingPlanByPlanIds(planIds));
    }

    /**
     * 获取计划关联的批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:query')")
    @GetMapping("/{planId}/batches")
    public AjaxResult getPlantingPlanBatches(@PathVariable("planId") Long planId)
    {
        List<AgricultureCropBatch> batchList = agriculturePlantingPlanService.getPlantingPlanBatches(planId);
        return success(batchList);
    }

    /**
     * 将批次添加到计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:edit')")
    @Log(title = "种植计划", businessType = BusinessType.UPDATE)
    @PostMapping("/{planId}/batches")
    public AjaxResult addBatchToPlan(@PathVariable("planId") Long planId, @RequestBody Long[] batchIds)
    {
        return toAjax(agriculturePlantingPlanService.addBatchToPlan(planId, batchIds));
    }

    /**
     * 从计划中移除批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plantingplan:edit')")
    @Log(title = "种植计划", businessType = BusinessType.UPDATE)
    @DeleteMapping("/{planId}/batches/{batchId}")
    public AjaxResult removeBatchFromPlan(@PathVariable("planId") Long planId, @PathVariable("batchId") Long batchId)
    {
        return toAjax(agriculturePlantingPlanService.removeBatchFromPlan(planId, batchId));
    }
}
