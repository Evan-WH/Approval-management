<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick" v-loading="loading">
      <el-tab-pane label="我的申请" name="first">
        <el-table
          :data="myList.slice((pageInfo.currentPage-1)*pageInfo.pageSize,pageInfo.currentPage*pageInfo.pageSize)"
          style="width: 100%" :row-class-name="tableRowClassName" @selection-change="handleSelectionChange">
          <el-table-column prop="id" label="编号" align="center">
          </el-table-column>
          <el-table-column prop="name" label="申请昵称" align="center">
          </el-table-column>
          <el-table-column prop="begtime" label="预约时间" :formatter="dateFormat" align="center">
          </el-table-column>
          <el-table-column prop="endtime" label="离开时间" :formatter="dateFormat" align="center">
          </el-table-column>
          <el-table-column prop="status" label="审批状态" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status==0">待审核</el-tag>
              <el-tag type="success" v-else-if="scope.row.status==1">已通过</el-tag>
              <el-tag type="success" v-else="scope.row.status==2">被驳回</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="show(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination v-show="total>0" :total="myList.length" @current-change="currentChange"
          @size-change="handleSizeChange" :current-page="pageInfo.pageSize" />
        </el-pagination>
      </el-tab-pane>
      <!-- 待处理面板 -->
      <el-tab-pane label="待处理" name="second">
        <el-table
          :data="processList0.slice((pageInfo.currentPage-1)*pageInfo.pageSize,pageInfo.currentPage*pageInfo.pageSize)"
          style="width: 100%" :row-class-name="tableRowClassName" @selection-change="handleSelectionChange">
          <el-table-column prop="id" label="编号" align="center">
          </el-table-column>
          <el-table-column prop="name" label="申请昵称" align="center">
          </el-table-column>
          <el-table-column prop="begtime" label="预约时间" :formatter="dateFormat" align="center">
          </el-table-column>
          <el-table-column prop="endtime" label="离开时间" :formatter="dateFormat" align="center">
          </el-table-column>
          <el-table-column prop="status" label="审批状态" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status==0">待审核</el-tag>
              <el-tag type="success" v-else-if="scope.row.status==1">已通过</el-tag>
              <el-tag type="success" v-else="scope.row.status==2">被驳回</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="show(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination v-show="total>0" :total="processList0.length" @current-change="currentChange"
          @size-change="handleSizeChange" :current-page="pageInfo.pageSize" />
        </el-pagination>
      </el-tab-pane>
      <!-- 完成审批面包 -->
      <el-tab-pane label="已完成审批" name="third">
        <el-table
          :data="processList1.slice((pageInfo.currentPage-1)*pageInfo.pageSize,pageInfo.currentPage*pageInfo.pageSize)"
          style="width: 100%" :row-class-name="tableRowClassName" @selection-change="handleSelectionChange">
          <el-table-column prop="id" label="编号" align="center">
          </el-table-column>
          <el-table-column prop="name" label="申请昵称" align="center">
          </el-table-column>
          <el-table-column prop="begtime" label="预约时间" :formatter="dateFormat" align="center">
          </el-table-column>
          <el-table-column prop="endtime" label="离开时间" :formatter="dateFormat" align="center">
          </el-table-column>
          <el-table-column prop="status" label="审批状态" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status==0">待审核</el-tag>
              <el-tag type="success" v-else-if="scope.row.status==1">已通过</el-tag>
              <el-tag type="success" v-else="scope.row.status==2">被驳回</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="show(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination v-show="total>0" :total="processList1.length" @current-change="currentChange"
          @size-change="handleSizeChange" :current-page="pageInfo.pageSize" />
        </el-pagination>
      </el-tab-pane>
    </el-tabs>
    <el-dialog title="审批详情" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <div style="margin-top:-20px">
          <el-alert title="审批状态：待审核" type="info" :closable="false" v-if="form.status==0" center></el-alert>
          <el-alert title="审批状态：已通过" type="success" :closable="false" v-else="form.status==1" center></el-alert>
        </div>
        <el-divider></el-divider>
        <div style="height:300px">
          <el-steps direction="vertical" :active="form.status+2" finish-status="success">
            <el-step title="已提交" :description="'发起人：'+form.name"></el-step>
            <el-step title="待审批"></el-step>
            <el-step title="已通过" :description="'被访问人：'+form.bvname+'————若已通过审批请注意访问时间'"></el-step>
          </el-steps>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { listProcess, getProcess, delProcess, addProcess, updateProcess } from "@/api/system/process";
import moment from 'moment'
import { getUserProfile } from "@/api/system/user";
export default {
  data () {
    return {
      processList: [],
      myList: [],
      userList: [],
      processList0: [],
      processList1: [],
      pageInfo: {
        currentPage: 1,
        pageSize: 10,
        pageTotal: 20
      },
      activeName: 'first',
      open: false,
      status: 1,
      form: {},
      begtime: '',
      endtime: '',
      loading: true,
      // 条数
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 访客申请表格数据
      processList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      queryParams: {
        userid: '',
        pageNum: 1,
        pageSize: 10,
        name: null,
        idcard: null,
        carnumber: null,
        deptid: null,
        bvname: null,
        begtime: null,
        descr: null,
        endtime: null,
        status: null
      },
    }
  },
  created () {
    this.getList()
  },
  methods: {
    handleSizeChange (val) {
      this.pageInfo.pageSize = val
    },
    currentChange (val) {
      this.pageInfo.currentPage = val
    },
    //   时间格式化
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY年MM月DD日 HH:mm");
    },
    getList () {
      this.myList = []
      this.processList = []
      getUserProfile().then(res => {
        this.queryParams.userid = res.data.userId
        listProcess().then(response => {
          this.processList = response.rows;
          this.total = response.total
          for (let i = 0; i < this.processList.length; i++) {
            if (this.processList[i].userid == this.queryParams.userid) {
              this.myList.push(this.processList[i])
            }
          }
          this.loading = false
          for (let j = 0; j < this.myList.length; j++) {
            if (this.myList[j].status == 0) {
              this.processList0.push(this.myList[j])
            }
          }
          for (let k = 0; k < this.myList.length; k++) {
            if (this.myList[k].status == 1) {
              this.processList1.push(this.myList[k])
            }
          }
        });
      })
      //   00000000000
    },
    handleClick (tab, event) {
      console.log(tab, event);
    },
    tableRowClassName ({ row, rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row';
      } else if (rowIndex === 3) {
        return 'success-row';
      }
      return '';
    },
    handleSelectionChange (val) {
      this.multipleSelection = val;
    },
    // 详情
    show (row) {
      const id = row.id || this.ids
      this.open = true;
      getProcess(id).then(response => {
        this.form = response.data
      },
      )
    },
    handleClose (done) {
      done();
    },
  }
}
</script>
<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>