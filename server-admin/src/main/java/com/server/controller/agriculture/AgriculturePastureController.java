package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.annotation.SeeRefreshData;
import com.server.domain.AgriculturePasture;
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
import com.server.service.AgriculturePastureService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 温室Controller
 */
@RestController
@RequestMapping("/agriculture/pasture")
public class AgriculturePastureController extends BaseController
{
    @Autowired
    private AgriculturePastureService agriculturePastureService;

    /**
     * 查询温室列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:pasture:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriculturePasture agriculturePasture)
    {
        startPage();
        List<AgriculturePasture> list = agriculturePastureService.selectAgriculturePastureList(agriculturePasture);
        return getDataTable(list);
    }

    /**
     * 导出温室列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:pasture:export')")
    @Log(title = "温室", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgriculturePasture agriculturePasture)
    {
        List<AgriculturePasture> list = agriculturePastureService.selectAgriculturePastureList(agriculturePasture);
        ExcelUtil<AgriculturePasture> util = new ExcelUtil<AgriculturePasture>(AgriculturePasture.class);
        util.exportExcel(response, list, "温室数据");
    }

    /**
     * 获取温室详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:pasture:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agriculturePastureService.selectAgriculturePastureById(id));
    }

    /**
     * 新增温室
     */
    @PreAuthorize("@ss.hasPermi('agriculture:pasture:add')")
    @Log(title = "温室", businessType = BusinessType.INSERT)
    @PostMapping
    @SeeRefreshData
    public AjaxResult add(@RequestBody AgriculturePasture agriculturePasture)
    {
        return toAjax(agriculturePastureService.insertAgriculturePasture(agriculturePasture));
    }

    /**
     * 修改温室
     */
    @PreAuthorize("@ss.hasPermi('agriculture:pasture:edit')")
    @Log(title = "温室", businessType = BusinessType.UPDATE)
    @PutMapping
    @SeeRefreshData
    public AjaxResult edit(@RequestBody AgriculturePasture agriculturePasture)
    {
        return toAjax(agriculturePastureService.updateAgriculturePasture(agriculturePasture));
    }

    /**
     * 删除温室
     */
    @PreAuthorize("@ss.hasPermi('agriculture:pasture:remove')")
    @Log(title = "温室", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @SeeRefreshData
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agriculturePastureService.deleteAgriculturePastureByIds(ids));
    }


}
