package com.study.deviceManager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.study.deviceManager.consts.DeviceConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Device {

    @TableId(value = "device_id", type = IdType.AUTO)
    private int deviceId;
    private String deviceName;
    private String deviceOwner;
    private int deviceStatus = DeviceConst.DEVICE_STATUS_UNLAND;
    private String lendUser;
    private String lendTime;
}
