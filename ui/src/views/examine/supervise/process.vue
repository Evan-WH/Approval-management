<template>
  <div class="app-container">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <!-- 访客姓名 -->
      <el-form-item label="访客姓名" prop="name">
        <el-input v-model="ruleForm.name" placeholder="请输入您的姓名"></el-input>
      </el-form-item>
      <!-- 访客身份证号 -->
      <el-form-item label="证件号码" prop="idcard">
        <el-input v-model="ruleForm.idcard" placeholder="请输入您的证件号码"></el-input>
      </el-form-item>
      <!-- 访客车牌号码 -->
      <el-form-item label="车牌号码" prop="carnumber">
        <el-input v-model="ruleForm.carnumber" placeholder="请输入您的车牌号码"></el-input>
      </el-form-item>
      <!-- 被访问部门 -->
      <el-form-item label="被访部门" prop="deptid">
        <treeselect v-model="ruleForm.deptid" :options="deptList" :normalizer="normalizer" placeholder="请输入或选择您要访问的部门"
          @input="parentIdChange" />
      </el-form-item>
      <!-- 访客车牌号码 -->
      <el-form-item label="被访姓名" prop="bvname">
        <el-input v-model="ruleForm.bvname" placeholder="请输入您需要访人姓名"></el-input>
        </el-form-ite>
        <!-- 访问预约起始时间 -->
      </el-form-item>
      <el-form-item label="预约时间" prop="time">
        <el-date-picker v-model="ruleForm.time" type="datetimerange" range-separator="至" start-placeholder="开始日期"
          end-placeholder="结束日期" format="yyyy-MM-dd HH:mm" align="right">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="备注" prop="desc">
        <el-input type="textarea" v-model="ruleForm.desc"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">提交申请</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addProcess, listDept } from "@/api/system/process";
import { getUserProfile } from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { addMessage } from "@/api/system/message";
export default {
  name: "Process",
  components: { Treeselect },
  data () {
    return {
      deptList: [],
      userList: [],
      ruleForm: {
        name: '',
        idcard: '',
        carnumber: '',
        bvname: '',
        time: '',
        desc: '',
        deptid: undefined,
        // 开始时间
        begtime: '',
        // 结束时间
        endtime: '',
        status: 0,
        userid: ''
      },
      rules: {
        name: [{ required: true, message: '请输入访客姓名', trigger: 'blur' }],
        idcard: [{ required: true, message: '请输入您的证件号码', trigger: 'blur' }
        ],
        carnumber: [{ required: true, message: '请输入车牌号码', trigger: 'blur' }],
        bvname: [{ required: true, message: '请输入被访人员姓名', trigger: 'blur' }],
        deptid: [{ required: true, message: '请选择被访部门', trigger: 'input' }],
        time: [{
          type: 'array',
          required: true,
          message: '请选择日期区间',
        }]

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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      userId: '',
      form: {}
    };
  },
  created () {
    this.getDeptList()
  },
  methods: {
    //   提交申请
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.ruleForm.begtime = Date.parse(this.ruleForm.time[0])
          this.ruleForm.endtime = Date.parse(this.ruleForm.time[1])
          this.ruleForm.userid = this.userList.userId
          addProcess(this.ruleForm).then(response => {
            if (response.code == 200) {
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

              //获取当前系统时间  格式(yyyy-mm-dd hh:mm:ss)
              var currentFormatDate = year + "-" + month + "-" + day
              this.form.messageTitle = '访问申请待审批'
              this.form.messageCreateby = this.userList.userName
              this.form.messageTime = currentFormatDate
              this.form.messageContent = this.userList.userName + "已发起申请，请您审批"
              this.form.roleId = "1"
              addMessage(this.form).then(response => {
                this.open = false;
              });
              this.$modal.msgSuccess("已成功发起申请，请注意查看审批结果");
              console.log(response.code)
              this.$router.push('task')
            } else {
              this.$message({
                showClose: true,
                message: '申请失败，请联系管理员',
                type: 'error'
              });
            }
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    parentIdChange (val) {
      this.$refs['ruleForm'].validateField('deptid')
    },
    // 表单验证
    resetForm (formName) {
      this.$refs[formName].resetFields();
    },
    /** 查询部门列表 */
    getDeptList () {
      this.loading = true;
      getUserProfile().then(res => {
        this.userList = res.data
      })
      listDept(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data, "deptId");
        this.loading = false;
        console.log(this.deptList)
      });
    },
    /** 转换部门数据结构 */
    normalizer (node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    /** 转存数据 */
  }
};
</script>
