package com.study.deviceManager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.Framework.utils.DateUtil;
import com.study.Framework.utils.LogUtil;
import com.study.Framework.webResult.WebResult;
import com.study.Framework.webResult.WebResultUtil;
import com.study.deviceManager.consts.DeviceConst;
import com.study.deviceManager.dao.DeviceDao;
import com.study.deviceManager.dto.DeviceQueryDto;
import com.study.deviceManager.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceDao deviceDao;

    /**
     *  获取设备列表
     * @param deviceQueryDto 设备信息
     * @return WebResult
     */
    @RequestMapping(path = "/list")
    @ResponseBody
    public WebResult deviceList(@RequestBody @Validated DeviceQueryDto deviceQueryDto) {
        try {
            Page<Device> page = new Page<>(deviceQueryDto.getPage(), deviceQueryDto.getPageSize());
            QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
            if (!(deviceQueryDto.getDeviceName() == null)){
                queryWrapper.eq("device_name", deviceQueryDto.getDeviceName());
            }
            if (!(deviceQueryDto.getDeviceStatus() == null)){
                queryWrapper.eq("device_status", deviceQueryDto.getDeviceStatus());
            }
            if (!(deviceQueryDto.getDeviceOwner() == null)){
                queryWrapper.eq("device_owner", deviceQueryDto.getDeviceOwner());
            }
            Page<Device> devicePage = deviceDao.selectPage(page, queryWrapper);
            LogUtil.info(devicePage.getRecords().toString());

            
            return WebResultUtil.success(devicePage);
        }catch (Exception e) {
            LogUtil.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return WebResultUtil.fail("获取设备列表失败");
        }
    }

    /**
     * 借用设备
     * @return WebResult
     */
    @RequestMapping(path = "/lend")
    @ResponseBody
    public WebResult lendDevice(@RequestBody DeviceQueryDto deviceQueryDto) {

        if (deviceQueryDto.getDeviceList().isEmpty()) {
            LogUtil.error("请选择设备");
            return WebResultUtil.fail("请选择设备");
        }

        for (Device device : deviceQueryDto.getDeviceList()) {
            if (device.getDeviceStatus() == DeviceConst.DEVICE_STATUS_LANDED) {
                LogUtil.error("设备已借出，请确认");
                return WebResultUtil.fail("设备已借出，请确认");
            }
        }

        try {
            List<Integer> collect = deviceQueryDto.getDeviceList().stream().map(Device::getDeviceId).collect(Collectors.toList());

            collect.forEach(id -> {
                Device device = deviceDao.selectById(id);
                device.setDeviceStatus(DeviceConst.DEVICE_STATUS_LANDED);
                device.setLendUser(deviceQueryDto.getLendUserName());
                device.setLendTime(DateUtil.date2String());
                deviceDao.updateById(device);
            });

            return WebResultUtil.success();
        }catch (Exception e) {
            LogUtil.error(Arrays.toString(e.getStackTrace()));
            return WebResultUtil.fail("借用设备失败");
        }
    }

    /**
     * 归还设备
     * @param deviceQueryDto 归还的设备信息
     * @return WebResult
     */
    @RequestMapping(path = "/return")
    @ResponseBody
    public WebResult returnDevice(@RequestBody DeviceQueryDto deviceQueryDto) {
        if (deviceQueryDto.getDeviceList().isEmpty()) {
            return WebResultUtil.fail("请选择设备");
        }

        for (Device device : deviceQueryDto.getDeviceList()) {
            if (device.getDeviceStatus() == DeviceConst.DEVICE_STATUS_UNLAND) {
                return WebResultUtil.fail("设备未借出，请确认");
            }
        }

        try {
            List<Integer> collect = deviceQueryDto.getDeviceList().stream().map(Device::getDeviceId).collect(Collectors.toList());

            collect.forEach(id -> {
                Device device = deviceDao.selectById(id);
                device.setDeviceStatus(DeviceConst.DEVICE_STATUS_UNLAND);
                device.setLendUser("");
                device.setLendTime(null);
                deviceDao.updateById(device);
            });

            return WebResultUtil.success();
        }catch (Exception e) {
            LogUtil.error(Arrays.toString(e.getStackTrace()));
            return WebResultUtil.fail("归还设备失败");
        }
    }

    /**
     * 获取设备名称列表
     * @return 设备名称列表
     */
    @RequestMapping(path = "/nameList")
    @ResponseBody
    public WebResult deviceNameList() {
        try {
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
            deviceQueryWrapper.select("distinct device_name");
            List<Device> devices = deviceDao.selectList(deviceQueryWrapper);
            List<String> deviceNameList = devices.stream().map(Device::getDeviceName).collect(Collectors.toList());
            Assert.notNull(devices, "设备列表为空");
            return WebResultUtil.success(deviceNameList);
        }catch (Exception e) {
            LogUtil.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return WebResultUtil.fail("获取设备列表失败");
        }
    }

    /**
     * 获取设备使用人列表
     * @return 设备使用人列表
     */
    @RequestMapping(path = "/ownerList")
    @ResponseBody
    public WebResult deviceOwnerList() {
        try {
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
            deviceQueryWrapper.select("distinct device_owner");
            List<Device> devices = deviceDao.selectList(deviceQueryWrapper);
            List<String> collect = devices.stream().map(Device::getDeviceOwner).collect(Collectors.toList());
            return WebResultUtil.success(collect);
        }catch (Exception e) {
            LogUtil.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return WebResultUtil.fail("获取设备使用人列表失败");
        }
    }

    /**
     * 添加设备
     * @param device 设备
     * @return 结果
     */
    @RequestMapping(path = "/addDevice")
    @ResponseBody
    public WebResult addDevice(@RequestBody Device device) {
        try {
            System.out.println(device.toString());

            if (device.getDeviceName().isEmpty() || device.getDeviceOwner().isEmpty()) {
                return WebResultUtil.fail("设备名称或所有人不能为空");
            }

            int res = deviceDao.insert(device);
            if (res == 1) {
                return WebResultUtil.success();
            }else {
                return WebResultUtil.fail("添加设备失败");
            }
        }catch (Exception e) {
            LogUtil.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return WebResultUtil.fail("添加设备失败");
        }
    }

    /**
     * 删除设备
     * @param deviceQueryDto 设备信息
     * @return 结果
     */
    @RequestMapping(path = "/deleteDevice")
    @ResponseBody
    public WebResult deleteDevice(@RequestBody DeviceQueryDto deviceQueryDto) {
        try {
            if (deviceQueryDto.getDeviceList().isEmpty()) {
                return WebResultUtil.fail("请选择要删除的设备");
            }

            for (Device device : deviceQueryDto.getDeviceList()) {
                int i = deviceDao.deleteById(device.getDeviceId());
                if (i != 1) {
                    return WebResultUtil.fail("删除设备失败");
                }
            }

            return WebResultUtil.success("删除设备成功");

        }catch (Exception e) {
            LogUtil.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return WebResultUtil.fail("删除设备失败");
        }
    }

    public static void main(String[] args) {
        
    }
}
