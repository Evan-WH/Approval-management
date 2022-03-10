package com.examine.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.examine.common.annotation.Excel;
import com.examine.common.core.domain.BaseEntity;

/**
 * 访客申请对象 examine_process
 *
 * @author Evan-WH
 * @date 2022-03-06
 */
public class ExamineProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 访客申请信息ID
     */
    private Long id;

    /** 访客姓名 */
    @Excel(name = "访客姓名")
    private String name;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private Long idcard;

    /** 车牌号码 */
    @Excel(name = "车牌号码")
    private String carnumber;

    /** 被访部门ID */
    @Excel(name = "被访部门ID")
    private String deptid;

    /** 被访问人姓名 */
    @Excel(name = "被访问人姓名")
    private String bvname;

    /** 访问开始时间 */
    @Excel(name = "访问开始时间")
    private Long begtime;

    /** 备注 */
    @Excel(name = "备注")
    private String descr;

    /** 访问结束时间 */
    @Excel(name = "访问结束时间")
    private Long endtime;

    /** 审批状态，默认待审 */
    @Excel(name = "访问结束时间")
    private Long status;


    /** 用户id */
    @Excel(name = "用户id")
    private Long userid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setIdcard(Long idcard)
    {
        this.idcard = idcard;
    }
    public Long getIdcard()
    {
        return idcard;
    }

    public void setCarnumber(String carnumber)
    {
        this.carnumber = carnumber;
    }
    public String getCarnumber()
    {
        return carnumber;
    }

    public void setDeptid(String deptid)
    {
        this.deptid = deptid;
    }
    public String getDeptid()
    {
        return deptid;
    }

    public void setBvname(String bvname)
    {
        this.bvname = bvname;
    }
    public String getBvname()
    {
        return bvname;
    }

    public void setBegtime(Long begtime)
    {
        this.begtime = begtime;
    }
    public Long getBegtime()
    {
        return begtime;
    }

    public void setDescr(String descr)
    {
        this.descr = descr;
    }
    public String getDescr()
    {
        return descr;
    }

    public void setEndtime(Long endtime)
    {
        this.endtime = endtime;
    }
    public Long getEndtime()
    {
        return endtime;
    }

    public void setStatus(Long status) {this.status = status;}
    public Long getStatus()
    {
        return status;
    }
    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("idcard", getIdcard())
                .append("carnumber", getCarnumber())
                .append("deptid", getDeptid())
                .append("bvname", getBvname())
                .append("begtime", getBegtime())
                .append("descr", getDescr())
                .append("endtime", getEndtime())
                .append("status", getStatus())
                .append("userid", getUserid())
                .toString();
    }
}
