<template>
  <div class="app-container" :v-loading="loading">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="访客姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入访客姓名" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="证件号码" prop="idcard">
        <el-input v-model="queryParams.idcard" placeholder="请输入证件号码" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="车牌号码" prop="carnumber">
        <el-input v-model="queryParams.carnumber" placeholder="请输入车牌号码" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:process:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:process:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:process:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="newList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="访客姓名" align="center" prop="name" />
      <!-- <el-table-column label="证件号码" align="center" prop="idcard" /> -->
      <!-- <el-table-column label="车牌号码" align="center" prop="carnumber" /> -->
      <el-table-column label="被访部门" align="center" prop="deptid" width="200" />
      <el-table-column label="被访问人姓名" align="center" prop="bvname" />
      <el-table-column label="来访时间" :formatter="dateFormat" align="center" prop="begtime" width="180" />
      <el-table-column label="离开时间" :formatter="dateFormat" align="center" prop="endtime" width="180" />
      <el-table-column label="备注" align="center" prop="descr" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status==0">待审核</el-tag>
          <el-tag type="success" v-else-if="scope.row.status==1">已通过</el-tag>
          <el-tag type="danger" v-else="scope.row.status==2">被驳回</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:process:edit']">修改</el-button>
        </template>

      </el-table-column>
    </el-table>

    <el-pagination v-show="total>0" :total="newList.length" @current-change="currentChange"
      @size-change="handleSizeChange" :current-page="pageInfo.pageSize" />
    </el-pagination>
    <!--审批访客申请对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="访客姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入访客姓名" />
        </el-form-item>
        <el-form-item label="证件号码" prop="idcard">
          <el-input v-model="form.idcard" placeholder="请输入证件号码" :disabled="true" />
        </el-form-item>
        <el-form-item label="车牌号码" prop="carnumber">
          <el-input v-model="form.carnumber" placeholder="请输入车牌号码" :disabled="true" />
        </el-form-item>
        <el-form-item label="被访姓名" prop="bvname">
          <el-input v-model="form.bvname" placeholder="请输入被访问人姓名" />
        </el-form-item>
        <el-form-item label="备注" prop="descr">
          <el-input v-model="form.descr" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确认修改</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProcess, getProcess, delProcess, addProcess, updateProcess } from "@/api/system/process";
import { listDept } from "@/api/system/dept"
import { getUserProfile } from "@/api/system/user";
import moment from 'moment'

export default {
  name: "Process",
  data () {
    return {
      deptList: [],
      pageInfo: {
        currentPage: 1,
        pageSize: 10,
        pageTotal: 20
      },
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
      newList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
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
      time: '',
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created () {
    this.getList();
  },

  methods: {
    handleSizeChange (val) {
      this.pageInfo.pageSize = val
    },
    currentChange (val) {
      this.pageInfo.currentPage = val
    },
    //时间格式化
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY:MM:DD HH:mm");
    },
    /** 查询访客申请列表 */
    getList () {
      this.newList = []
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = response.data
        this.loading = false;
        listProcess(this.queryParams).then(response => {
          this.processList = response.rows;
          this.total = response.total;
          this.loading = false;
          for (var i = 0; i < this.deptList.length; i++) {
            for (var j = 0; j < this.processList.length; j++) {
              if (this.deptList[i].deptId == parseInt(this.processList[j].deptid)) {
                this.processList[j].deptid = this.deptList[i].deptName
              }
            }
          }
          for (var k = 0; k < this.processList.length; k++) {
            if (this.processList[k].status == 1) {
              this.newList.push(this.processList[k])
            }
          }
          console.log(this.newList)
        });
      });

    },
    // 取消按钮
    cancel () {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        id: null,
        name: null,
        idcard: null,
        carnumber: null,
        deptid: null,
        bvname: null,
        begtime: null,
        descr: null,
        endtime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      const id = row.id || this.ids
      listDept(this.queryParams).then(response => {
        this.deptList = response.data
        this.loading = false;
        getProcess(id).then(response => {
          this.form = response.data;
          this.open = true;
        });

      });
    },
    push () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.status == 1) {
            alert("审核已通过，请勿重复提交")
            return 0;
          } else if (this.form.id != null) {
            this.form.status = 1

            updateProcess(this.form).then(response => {
              this.$modal.msgSuccess("已通过审核，并向用户发送提示信息");
              this.open = false;
              this.getList();
            });
          } else {
            alert("信息格式有误")
          }
        }
      })
    },
    /** 提交按钮 */
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProcess(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProcess(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除访客申请编号为"' + ids + '"的数据项？').then(function () {
        return delProcess(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport () {
      this.download('system/process/export', {
        ...this.queryParams
      }, `process_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>