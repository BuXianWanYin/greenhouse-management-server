package com.server.controller.iot;

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
import com.server.domain.AgricultureSoilData;
import com.server.service.AgricultureSoilDataService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 土壤8参数传感器数据Controller
 * 
 * @author bxwy
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/device/soildata")
public class AgricultureSoilDataController extends BaseController
{
    @Autowired
    private AgricultureSoilDataService agricultureSoilDataService;

    /**
     * 查询土壤8参数传感器数据列表
     */
    @PreAuthorize("@ss.hasPermi('device:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureSoilData agricultureSoilData)
    {
        startPage();
        List<AgricultureSoilData> list = agricultureSoilDataService.selectAgricultureSoilDataList(agricultureSoilData);
        return getDataTable(list);
    }

    /**
     * 导出土壤8参数传感器数据列表
     */
    @PreAuthorize("@ss.hasPermi('device:data:export')")
    @Log(title = "土壤8参数传感器数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureSoilData agricultureSoilData)
    {
        List<AgricultureSoilData> list = agricultureSoilDataService.selectAgricultureSoilDataList(agricultureSoilData);
        ExcelUtil<AgricultureSoilData> util = new ExcelUtil<AgricultureSoilData>(AgricultureSoilData.class);
        util.exportExcel(response, list, "土壤8参数传感器数据数据");
    }

    /**
     * 获取土壤8参数传感器数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureSoilDataService.selectAgricultureSoilDataById(id));
    }

    /**
     * 新增土壤8参数传感器数据
     */
    @PreAuthorize("@ss.hasPermi('device:data:add')")
    @Log(title = "土壤8参数传感器数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureSoilData agricultureSoilData)
    {
        return toAjax(agricultureSoilDataService.insertAgricultureSoilData(agricultureSoilData));
    }

    /**
     * 修改土壤8参数传感器数据
     */
    @PreAuthorize("@ss.hasPermi('device:data:edit')")
    @Log(title = "土壤8参数传感器数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureSoilData agricultureSoilData)
    {
        return toAjax(agricultureSoilDataService.updateAgricultureSoilData(agricultureSoilData));
    }

    /**
     * 删除土壤8参数传感器数据
     */
    @PreAuthorize("@ss.hasPermi('device:data:remove')")
    @Log(title = "土壤8参数传感器数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureSoilDataService.deleteAgricultureSoilDataByIds(ids));
    }

    /**
     * 查询土壤趋势数据
     * 
     * @param pastureId 温室ID
     * @param range 时间范围：'day'(24小时), 'week'(7天), 'month'(30天)
     * @return 趋势数据
     */
    @GetMapping("/trend")
    public AjaxResult getTrendData(Long pastureId, String range)
    {
        return success(agricultureSoilDataService.getTrendData(pastureId, range));
    }
}

