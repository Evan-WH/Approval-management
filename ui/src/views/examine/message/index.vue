<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="通知标题" prop="messageTitle">
        <el-input v-model="queryParams.messageTitle" placeholder="请输入通知标题" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <!-- <el-form-item label="创建者" prop="messageCreateby">
        <el-input v-model="queryParams.messageCreateby" placeholder="请输入创建者" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item> -->
      <el-form-item label="接收人" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入接收人" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="接收状态" prop="messageStatus">
        <el-select v-model="queryParams.messageStatus" placeholder="请选择接收状态" clearable size="small">
          <el-option v-for="dict in dict.type.sys_notice_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <!-- <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:message:add']">新增</el-button> -->
      </el-col>
      <el-col :span="1.5">
        <!-- <!-- <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:message:edit']">修改</el-button> -->
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:message:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:message:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="myMessageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="通知编号" align="center" prop="id" />
      <el-table-column label="通知标题" align="center" prop="messageTitle" />
      <el-table-column label="通知内容" align="center" prop="messageContent" width="255" />
      <el-table-column label="创建者" align="center" prop="messageCreateby" />
      <el-table-column label="接收角色ID" align="center" prop="roleId" />
      <el-table-column label="接收状态" align="center" prop="messageStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_notice_status" :value="scope.row.messageStatus" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="messageTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.messageTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-success" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:message:edit']">确认已读</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:message:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改审批通过通知对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="通知标题" prop="messageTitle">
          <el-input v-model="form.messageTitle" placeholder="请输入通知标题" />
        </el-form-item>
        <el-form-item label="通知内容">
          <editor v-model="form.messageContent" :min-height="192" />
        </el-form-item>
        <el-form-item label="创建者" prop="messageCreateby">
          <el-input v-model="form.messageCreateby" placeholder="请输入创建者" />
        </el-form-item>
        <el-form-item label="接收角色" prop="roleId">
          <el-input v-model="form.roleId" placeholder="请输入接收人" />
        </el-form-item>
        <el-form-item label="接收状态">
          <el-radio-group v-model="form.messageStatus">
            <el-radio v-for="dict in dict.type.sys_notice_status" :key="dict.value" :label="parseInt(dict.value)">
              {{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="创建时间" prop="messageTime">
          <el-date-picker clearable size="small" v-model="form.messageTime" type="date" value-format="yyyy-MM-dd"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMessage, getMessage, delMessage, addMessage, updateMessage } from "@/api/system/message";
import { getUserProfile } from "@/api/system/user";
export default {
  name: "Message",
  dicts: ['sys_notice_status'],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      user: '',
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 审批通过通知表格数据
      messageList: [],
      myMessageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        messageTitle: null,
        messageContent: null,
        messageCreateby: null,
        roleid: null,
        messageStatus: null,
        messageTime: null
      },
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
    /** 查询审批通过通知列表 */
    getList () {
      this.loading = true;
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows;
        this.total = response.total;
        this.loading = false;
        getUserProfile().then(response => {
          this.myMessageList = []
          this.user = response.data;
          this.roleGroup = response.roleGroup;
          this.postGroup = response.postGroup;
          for (var i = 0; i < this.messageList.length; i++) {
            if (this.messageList[i].roleId == parseInt(this.user.roles[0].roleId)) {
              this.myMessageList.push(this.messageList[i])
            }
          }
          console.log(this.myMessageList)
        });
      })
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
        messageTitle: null,
        messageContent: null,
        messageCreateby: null,
        userId: null,
        messageStatus: 0,
        messageTime: null
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
    /** 新增按钮操作 */
    handleAdd () {
      this.reset();
      this.open = true;
      this.title = "添加审批通过通知";
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      const id = row.id || this.ids
      getMessage(id).then(response => {
        this.form = response.data;
        this.form.messageStatus = 1
        updateMessage(this.form).then(response => {
          this.$modal.msgSuccess("信息已读~");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 提交按钮 */
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMessage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMessage(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除审批通过通知编号为"' + ids + '"的数据项？').then(function () {
        return delMessage(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport () {
      this.download('system/message/export', {
        ...this.queryParams
      }, `message_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
