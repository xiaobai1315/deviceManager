<template>
  <div>
    <!-- 下拉选项 -->
    <div>
      <el-select v-model="deviceNameValue" placeholder="设备名称" size="small" clearable @clear="clearDeviceName">
        <el-option v-for="item in deviceNameOptions" :key="item" :label="item" :value="item">
        </el-option>
      </el-select>
      <el-select v-model="deviceStatusValue" placeholder="设备状态" size="small" clearable @clear="clearDeviceStatus">
        <el-option v-for="item in deviceStatusOptions" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <el-select v-model="deviceOwnerValue" placeholder="所属人" size="small" clearable @clear="clearDeviceOwner">
        <el-option v-for="item in deviceOwnerOptions" :key="item" :label="item" :value="item">
        </el-option>
      </el-select>
    </div>
    <!-- 重置、查询按钮 -->
    <div class="buttons">
      <el-button @click="reset" size="small">重置</el-button>
      <el-button @click="getDeviceList" size="small" type="primary">查询</el-button>
    </div>
    <!-- 分割线 -->
    <el-divider></el-divider>
    <!-- 借用、归还按钮 -->
    <div style="margin-bottom: 20px">
      <el-button @click="lend" size="small" type="primary">借用</el-button>
      <el-button @click="giveBack" size="small" type="primary">归还</el-button>
      <el-button @click="dialogFormVisible = true" size="small" type="primary">添加设备</el-button>
      <!-- 添加设备提示框 -->
      <el-dialog title="收货地址" :visible.sync="dialogFormVisible">
        <el-form :model="device">
          <el-form-item label="设备名称" :label-width="formLabelWidth">
            <el-input v-model="device.deviceName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="所属人" :label-width="formLabelWidth">
            <el-input v-model="device.deviceOwner" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addDevice">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 数据列表 -->
    <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="deviceId" label="设备编码" width="180">
      </el-table-column>
      <el-table-column prop="deviceName" label="设备名称" width="180">
      </el-table-column>
      <el-table-column prop="deviceOwner" label="所属人">
      </el-table-column>
      <el-table-column label="是否借出">
        <template slot-scope="scope">
          <div v-if="scope.row.deviceStatus === 1">借出</div>
          <div v-else>未借出</div>
        </template>
      </el-table-column>
      <el-table-column prop="lendUser" label="使用人">
      </el-table-column>
      <el-table-column prop="lendTime" label="借出时间">
        <template slot-scope="scope">
          <div v-if="scope.row.deviceStatus === 1">{{scope.row.lendTime}}</div>
          <div v-else></div>
        </template>
      </el-table-column>
       <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.deviceStatus === 1" size="mini" type="success" @click="returnDevice(scope.$index, scope.row)">归还</el-button>
            <el-button v-else size="mini" type="primary" @click="lendDevice(scope.$index, scope.row)">借用</el-button>
            <el-button size="mini" type="danger" @click="deleteDeviceMessage(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination background layout="prev, pager, next" :total="total" @prev-click="prevClick" @next-click="nextClick" @current-change="currentChange">
    </el-pagination>
  </div>
</template>

<script>
  export default {
    created() {
      this.getDeviceList();
      this.getDeviceNameList()
      this.getDeviceOwnerList()
    },
    data() {
      return {
        tableData: [],
        multipleSelection: [],
        page: 1,
        pageSize: 10,
        total: 0,
        searchForm: {},
        deviceNameOptions: [],
        deviceStatusOptions: [{
            value: 0,
            label: '未借出'
          },
          {
            value: 1,
            label: '借出'
          }
        ],
        deviceOwnerOptions: [],
        deviceNameValue: null,
        deviceStatusValue: null,
        deviceOwnerValue: null,
        dialogFormVisible: false,
        device: {
          deviceName: '',
          deviceOwner: ''
        },
        formLabelWidth: '120px'
      };
    },
    methods: {

      // 选择设备列表
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      // 获取设备列表
      async getDeviceList() {
        this.searchForm.page = this.page
        this.searchForm.pageSize = this.pageSize
        this.searchForm.deviceName = this.deviceNameValue
        this.searchForm.deviceStatus = this.deviceStatusValue
        this.searchForm.deviceOwner = this.deviceOwnerValue

        await this.$http
          .post("/device/list", this.searchForm)
          .then(response => {
            if (response.data.code != 200) {
              this.$message.error(response.data.message);
            } else {
              this.tableData = response.data.data.records
              this.total = response.data.data.total
            }
          })
          .catch(function (error) {
            this.$message.error("获取设备列表失败");
          });
      },

      // 获取设备名称下拉列表
      async getDeviceNameList() {
        await this.$http
          .post('/device/nameList')
          .then(response => {
            console.log(response.data.data)
            if (response.data.code != 200) {
              this.$message.error(response.data.message)
            } else {
              this.deviceNameOptions = response.data.data
            }
          })
      },

      // 获取设备使用人列表
      async getDeviceOwnerList() {
        await this.$http
          .post('/device/ownerList')
          .then(response => {
            if (response.data.code != 200) {
              this.$message.error(response.data.message)
            } else {
              this.deviceOwnerOptions = response.data.data
            }
          })
      },

      // 页码改变
      currentChange(val) {
        this.page = val;
        this.getDeviceList()
      },

      // 上一页
      prevClick(val) {
        this.page -= 1;
        this.getDeviceList()
      },

      // 下一页
      nextClick(val) {
        this.page += 1;
        this.getDeviceList()
      },

      // 借用设备
      lend() {

        this.$prompt('请输入用户名', '借用设备', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (val) => {
            if (val == null) {
              return '请输入用户名'
            }
          },
        }).then(({
          value
        }) => {
          this.searchForm.deviceList = this.multipleSelection
          this.searchForm.lendUserName = value

          this.$http.post('/device/lend', this.searchForm)
            .then(response => {
              console.log(response.data)
              if (response.data.code != 200) {
                this.$message.error(response.data.message)
                this.$refs.multipleTable.clearSelection()
              } else {
                this.$message({
                  type: 'success',
                  message: '操作成功 '
                });
                this.getDeviceList()
              }
            })
        }).catch(() => {

        });
      },

      // 归还设备
      giveBack() {
        this.searchForm.deviceList = this.multipleSelection
        this.$http.post('/device/return', this.searchForm)
          .then(response => {
            if (response.data.code != 200) {
              this.$message.error(response.data.message)
              this.$res.multipleTable.clearSelection()
            } else {
              this.$message({
                type: 'success',
                message: '操作成功'
              })
              this.getDeviceList()
            }
          }).catch(() => {

          })
      },

      // 添加设备
      addDevice() {
        this.dialogFormVisible = false
        this.$http.post('/device/addDevice', this.device)
        .then(response => {
          if (response.data.code != 200) {
            this.$message.error(response.data.message)
            this.device = {}
          }else {
            this.$message({
                type: 'success',
                message: '操作成功'
              })
              this.getDeviceList()
          }
        }).catch(() => {})
      },

      // 点击操作中的借用按钮
      lendDevice(index, row) {
        this.multipleSelection = row
        this.lend()
      },

      // 点击操作中的归还按钮
      returnDevice(index, row) {
        this.multipleSelection = row
        this.giveBack()
      },

      // 删除设备提示框
      deleteDeviceMessage(index, row) {
        console.log(row.deviceName);
        this.$confirm('此操作将永久删除该设备, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteDevice(row)
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
        this.multipleSelection = []
        this.searchForm.deviceList = []
        this.searchForm = {}
      },

      // 删除设备
      deleteDevice(row) {
        this.multipleSelection.push(row)
        this.searchForm.deviceList = this.multipleSelection
        this.$http.post('/device/deleteDevice', this.searchForm)
        .then(response => {
          if (response.data.code == 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getDeviceList()
          }else {
            this.$message({
              type: 'info',
              message: '设备删除失败'
            });  
          }
        })
        .catch(() => {
          this.$message({
              type: 'info',
              message: '设备删除失败'
            }); 
        })
      },

      // 重置查询选项
      reset() {
        this.deviceNameValue = null
        this.deviceStatusValue = null
        this.deviceOwnerValue = null
        this.getDeviceList()
      },
      // 清空设备名称下拉列表
      clearDeviceName() {
        this.deviceNameValue = null
        this.getDeviceList()
      },
      // 清空设备状态下拉列表
      clearDeviceStatus() {
        this.deviceStatusValue = null
        this.getDeviceList()
      },
      // 清空设备所属人列表
      clearDeviceOwner() {
        this.deviceOwnerValue = null
        this.getDeviceList()
      }
    }
  };

</script>

<style scoped>
  .el-pagination {
    text-align: right;
    padding: 20px 20px 20px 0;
  }

  .el-select {
    margin-right: 40px;

  }

  .buttons {
    text-align: right;
    margin-top: 20px;
    margin-right: 20px;
  }

</style>

