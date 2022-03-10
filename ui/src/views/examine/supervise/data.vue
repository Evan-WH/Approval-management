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

    <el-table v-loading="loading" :data="processList" @selection-change="handleSelectionChange">
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
          <el-button size="mini" type="text" icon="el-icon-thumb" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:process:list']">通过审批</el-button>
          <!-- <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:process:edit']">修改</el-button> -->
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:process:remove']">删除</el-button>

        </template>

      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

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
        <!-- <el-form-item label="被访部门" prop="deptid">
          <el-input v-model="form.deptid" placeholder="请输入被访部门" :disabled="true" />
        </el-form-item> -->
        <el-form-item label="被访姓名" prop="bvname">
          <el-input v-model="form.bvname" placeholder="请输入被访问人姓名" />
        </el-form-item>
        <!-- <el-form-item label="来访时间" prop="begtime">
          <el-input v-model="form.begtime" placeholder="请输入来访时间" />
        </el-form-item>
        <el-form-item label="离开时间" prop="endtime">
          <el-input v-model="form.endtime" placeholder="请输入离开时间" />
        </el-form-item> -->
        <el-form-item label="备注" prop="descr">
          <el-input v-model="form.descr" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="success" @click="push" style="float:left">审批通过</el-button>
        <el-button type="primary" @click="cancel">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProcess, getProcess, delProcess, addProcess, updateProcess } from "@/api/system/process";
import { listDept } from "@/api/system/dept"
import { getUserProfile } from "@/api/system/user";
import { addMessage } from "@/api/system/message";
import moment from 'moment'

export default {
  name: "Process",
  data () {
    return {
      deptList: [],
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
      message: {},
      userList: [],
      // 表单校验
      rules: {
      }
    };
  },
  created () {
    this.getList();
  },

  methods: {

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
      this.form = []
      this.reset();
      const id = row.id || this.ids
      listDept(this.queryParams).then(response => {
        this.deptList = response.data
        this.loading = false;
        getProcess(id).then(response => {
          this.form = response.data;

          this.$confirm('此操作将对访客通过审批, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.form.status == 1) {
              this.$message({
                type: 'error',
                message: '审批已通过，请勿重新提交信息'
              });
            } else {
              this.form.status = 1
              this.$message({
                type: 'success',
                message: '审批通过，已向门岗发起详细信息核实!'
              });
              updateProcess(this.form).then(response => {
                this.getList();
              });
              var date = new Date();

              var year = date.getFullYear();        //年 ,从 Date 对象以四位数字返回年份
              var month = date.getMonth() + 1;      //月 ,从 Date 对象返回月份 (0 ~ 11) ,date.getMonth()比实际月份少 1 个月
              var day = date.getDate();             //日 ,从 Date 对象返回一个月中的某一天 (1 ~ 31)

              var hours = date.getHours();          //小时 ,返回 Date 对象的小时 (0 ~ 23)
              var minutes = date.getMinutes();      //分钟 ,返回 Date 对象的分钟 (0 ~ 59)
              var seconds = date.getSeconds();      //秒 ,返回 Date 对象的秒数 (0 ~ 59)   

              //修改月份格式
              if (month >= 1 && month <= 9) {
                month = "0" + month;
              }

              //修改日期格式
              if (day >= 0 && day <= 9) {
                day = "0" + day;
              }

              //修改小时格式
              if (hours >= 0 && hours <= 9) {
                hours = "0" + hours;
              }

              //修改分钟格式
              if (minutes >= 0 && minutes <= 9) {
                minutes = "0" + minutes;
              }

              //修改秒格式
              if (seconds >= 0 && seconds <= 9) {
                seconds = "0" + seconds;
              }
              getUserProfile().then(res => {
                this.userList = res.data
                console.log(this.userList.userName)
                //获取当前系统时间  格式(yyyy-mm-dd hh:mm:ss)
                var currentFormatDate = year + "-" + month + "-" + day
                this.message.messageTitle = '审批通过'
                this.message.messageCreateby = this.userList.userName
                this.message.messageTime = currentFormatDate
                this.message.messageContent = this.userList.userName + "已有审批通过请查收！"
                this.message.roleId = 3
                addMessage(this.message).then(response => {
                });
                //   this.$modal.msgSuccess("已成功发起申请，请注意查看审批结果");
              })


            }
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            });
          });

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