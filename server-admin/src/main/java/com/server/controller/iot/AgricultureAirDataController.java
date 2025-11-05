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
import com.server.domain.AgricultureAirData;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgriculturePasture;
import com.server.service.AgricultureAirDataService;
import com.server.service.AgricultureDeviceService;
import com.server.service.AgriculturePastureService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 温度湿度光照传感器数据Controller
 * 
 * @author server
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/device/airdata")
public class AgricultureAirDataController extends BaseController
{
    @Autowired
    private AgricultureAirDataService agricultureAirDataService;

    @Autowired
    private AgricultureDeviceService agricultureDeviceService;

    @Autowired
    private AgriculturePastureService agriculturePastureService;

    /**
     * 查询温度湿度光照传感器数据列表
     */
    @PreAuthorize("@ss.hasPermi('device:airdata:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureAirData agricultureAirData)
    {
        startPage();
        List<AgricultureAirData> list = agricultureAirDataService.selectAgricultureAirDataList(agricultureAirData);
        return getDataTable(list);
    }

    /**
     * 导出温度湿度光照传感器数据列表
     */
    @PreAuthorize("@ss.hasPermi('device:airdata:export')")
    @Log(title = "温度湿度光照传感器数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureAirData agricultureAirData)
    {
        List<AgricultureAirData> list = agricultureAirDataService.selectAgricultureAirDataList(agricultureAirData);
        
        // 获取所有设备ID和温室ID
        Set<Long> deviceIds = list.stream()
            .map(AgricultureAirData::getDeviceId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        
        Set<Long> pastureIds = list.stream()
            .map(AgricultureAirData::getPastureId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        
        // 批量查询设备名称
        Map<Long, String> deviceNameMap = new HashMap<>();
        if (!deviceIds.isEmpty()) {
            List<AgricultureDevice> devices = agricultureDeviceService.listByIds(deviceIds);
            for (AgricultureDevice device : devices) {
                if (device != null && device.getDeviceName() != null) {
                    deviceNameMap.put(device.getId(), device.getDeviceName());
                }
            }
        }
        
        // 批量查询温室名称
        Map<Long, String> pastureNameMap = new HashMap<>();
        if (!pastureIds.isEmpty()) {
            List<AgriculturePasture> pastures = agriculturePastureService.listByIds(pastureIds);
            for (AgriculturePasture pasture : pastures) {
                if (pasture != null && pasture.getName() != null) {
                    pastureNameMap.put(pasture.getId(), pasture.getName());
                }
            }
        }
        
        // 填充设备名称和温室名称
        for (AgricultureAirData data : list) {
            if (data.getDeviceId() != null) {
                data.setDeviceName(deviceNameMap.getOrDefault(data.getDeviceId(), String.valueOf(data.getDeviceId())));
            }
            if (data.getPastureId() != null) {
                data.setPastureName(pastureNameMap.getOrDefault(data.getPastureId(), String.valueOf(data.getPastureId())));
            }
        }
        
        ExcelUtil<AgricultureAirData> util = new ExcelUtil<AgricultureAirData>(AgricultureAirData.class);
        util.exportExcel(response, list, "温度湿度光照传感器数据数据");
    }

    /**
     * 获取温度湿度光照传感器数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:airdata:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agricultureAirDataService.selectAgricultureAirDataById(id));
    }

    /**
     * 新增温度湿度光照传感器数据
     */
    @PreAuthorize("@ss.hasPermi('device:airdata:add')")
    @Log(title = "温度湿度光照传感器数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureAirData agricultureAirData)
    {
        return toAjax(agricultureAirDataService.insertAgricultureAirData(agricultureAirData));
    }

    /**
     * 修改温度湿度光照传感器数据
     */
    @PreAuthorize("@ss.hasPermi('device:airdata:edit')")
    @Log(title = "温度湿度光照传感器数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureAirData agricultureAirData)
    {
        return toAjax(agricultureAirDataService.updateAgricultureAirData(agricultureAirData));
    }

    /**
     * 删除温度湿度光照传感器数据
     */
    @PreAuthorize("@ss.hasPermi('device:airdata:remove')")
    @Log(title = "温度湿度光照传感器数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agricultureAirDataService.deleteAgricultureAirDataByIds(ids));
    }

    /**
     * 查询气象趋势数据
     * 
     * @param pastureId 温室ID
     * @param range 时间范围：'day'(24小时), 'week'(7天), 'month'(30天)
     * @return 趋势数据
     */
    @GetMapping("/trend")
    public AjaxResult getTrendData(Long pastureId, String range)
    {
        return success(agricultureAirDataService.getTrendData(pastureId, range));
    }
}

