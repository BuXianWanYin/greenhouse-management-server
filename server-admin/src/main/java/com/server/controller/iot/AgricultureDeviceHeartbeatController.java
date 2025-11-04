package com.server.controller.iot;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.domain.AgricultureDeviceHeartbeat;
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
import com.server.service.AgricultureDeviceHeartbeatService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 设备心跳状态（
 * 
 * @author server
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/device/heartbeat")
public class AgricultureDeviceHeartbeatController extends BaseController
{
    @Autowired
    private AgricultureDeviceHeartbeatService agricultureDeviceHeartbeatService;

    /**
     * 查询设备心跳状态 
     */
    @PreAuthorize("@ss.hasPermi('device:heartbeat:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        startPage();
        List<AgricultureDeviceHeartbeat> list = agricultureDeviceHeartbeatService.selectAgricultureDeviceHeartbeatList(agricultureDeviceHeartbeat);
        return getDataTable(list);
    }

    /**
     * 导出设备心跳状态 
     */
    @PreAuthorize("@ss.hasPermi('device:heartbeat:export')")
    @Log(title = "设备心跳状态（关联设备，设备删除时心跳记录自动删除）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        List<AgricultureDeviceHeartbeat> list = agricultureDeviceHeartbeatService.selectAgricultureDeviceHeartbeatList(agricultureDeviceHeartbeat);
        ExcelUtil<AgricultureDeviceHeartbeat> util = new ExcelUtil<AgricultureDeviceHeartbeat>(AgricultureDeviceHeartbeat.class);
        util.exportExcel(response, list, "设备心跳状态（关联设备，设备删除时心跳记录自动删除）数据");
    }

    /**
     * 获取设备心跳状态（关联设备，设备删除时心跳记录自动删除）详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:heartbeat:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureDeviceHeartbeatService.selectAgricultureDeviceHeartbeatById(id));
    }

    /**
     * 新增设备心跳状态
     */
    @PreAuthorize("@ss.hasPermi('device:heartbeat:add')")
    @Log(title = "设备心跳状态（关联设备，设备删除时心跳记录自动删除）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        return toAjax(agricultureDeviceHeartbeatService.insertAgricultureDeviceHeartbeat(agricultureDeviceHeartbeat));
    }

    /**
     * 修改设备心跳状态
     */
    @PreAuthorize("@ss.hasPermi('device:heartbeat:edit')")
    @Log(title = "设备心跳状态（关联设备，设备删除时心跳记录自动删除）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        return toAjax(agricultureDeviceHeartbeatService.updateAgricultureDeviceHeartbeat(agricultureDeviceHeartbeat));
    }

    /**
     * 删除设备心跳状态
     */
    @PreAuthorize("@ss.hasPermi('device:heartbeat:remove')")
    @Log(title = "设备心跳状态（关联设备，设备删除时心跳记录自动删除）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureDeviceHeartbeatService.deleteAgricultureDeviceHeartbeatByIds(ids));
    }
}

