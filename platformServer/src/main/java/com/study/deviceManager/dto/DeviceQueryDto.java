package com.study.deviceManager.dto;

import com.study.deviceManager.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceQueryDto {

    // 查询第几页数据
    private int page;

    // 每页显示数据数量
    private int pageSize;

    // 设备列表
    private List<Device> deviceList;

    // 借用人
    private String lendUserName;

    // 设备名称
    private String deviceName;

    // 设备状态
    private Integer deviceStatus;

    // 设备所有人
    private String deviceOwner;
}
