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
import com.server.domain.AgricultureGrowthStage;
import com.server.service.AgricultureGrowthStageService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 生长阶段Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/growthstage")
public class AgricultureGrowthStageController extends BaseController
{
    @Autowired
    private AgricultureGrowthStageService agricultureGrowthStageService;

    /**
     * 查询生长阶段列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthstage:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureGrowthStage agricultureGrowthStage)
    {
        startPage();
        List<AgricultureGrowthStage> list = agricultureGrowthStageService.selectAgricultureGrowthStageList(agricultureGrowthStage);
        return getDataTable(list);
    }

    /**
     * 导出生长阶段列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthstage:export')")
    @Log(title = "生长阶段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureGrowthStage agricultureGrowthStage)
    {
        List<AgricultureGrowthStage> list = agricultureGrowthStageService.selectAgricultureGrowthStageList(agricultureGrowthStage);
        ExcelUtil<AgricultureGrowthStage> util = new ExcelUtil<AgricultureGrowthStage>(AgricultureGrowthStage.class);
        util.exportExcel(response, list, "生长阶段数据");
    }

    /**
     * 获取生长阶段详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthstage:query')")
    @GetMapping(value = "/{stageId}")
    public AjaxResult getInfo(@PathVariable("stageId") Long stageId)
    {
        return success(agricultureGrowthStageService.selectAgricultureGrowthStageByStageId(stageId));
    }

    /**
     * 新增生长阶段
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthstage:add')")
    @Log(title = "生长阶段", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureGrowthStage agricultureGrowthStage)
    {
        return toAjax(agricultureGrowthStageService.insertAgricultureGrowthStage(agricultureGrowthStage));
    }

    /**
     * 修改生长阶段
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthstage:edit')")
    @Log(title = "生长阶段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureGrowthStage agricultureGrowthStage)
    {
        return toAjax(agricultureGrowthStageService.updateAgricultureGrowthStage(agricultureGrowthStage));
    }

    /**
     * 删除生长阶段
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthstage:remove')")
    @Log(title = "生长阶段", businessType = BusinessType.DELETE)
	@DeleteMapping("/{stageIds}")
    public AjaxResult remove(@PathVariable Long[] stageIds)
    {
        return toAjax(agricultureGrowthStageService.deleteAgricultureGrowthStageByStageIds(stageIds));
    }
}

