<template>
 <div>
     <el-table
    ref="multipleTable"
    :data="tableData"
    tooltip-effect="dark"
    style="width: 100%">
    <el-table-column
      type="selection"
      width="55">
    </el-table-column>
    <el-table-column
      label="日期"
      width="120">
      <template slot-scope="scope">{{ scope.row.date }}</template>
    </el-table-column>
    <el-table-column
      prop="name"
      label="姓名"
      width="120">
    </el-table-column>
    <el-table-column
      prop="address"
      label="地址"
      show-overflow-tooltip>
    </el-table-column>
  </el-table>
  <div style="margin-top: 20px">
    <el-button @click="toggleSelection([tableData[1], tableData[2]])">切换第二、第三行的选中状态</el-button>
    <el-button @click="toggleSelection()">取消选择</el-button>
  </div>
 </div>
</template>

<script>
export default {
 data () {
    return {
        tableData: []
    }
 },
 methods:{
     async getTestCaseList() {
         await this.$http.post('/autotest/list')
         .then(response => {
             console.log(response)
         })
         .catch(function(error){
             console.log(error)
         })
     }
 },
 components:{
 }
}

</script>

<style scoped>

</style>
