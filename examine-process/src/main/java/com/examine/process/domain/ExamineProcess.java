package com.examine.process.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long carnumber;

    /** 被访部门ID */
    @Excel(name = "被访部门ID")
    private String deptid;

    /** 被访问人姓名 */
    @Excel(name = "被访问人姓名")
    private String bvname;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

    /** 备注 */
    @Excel(name = "备注")
    private String desc;

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
    public void setCarnumber(Long carnumber) 
    {
        this.carnumber = carnumber;
    }

    public Long getCarnumber() 
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
    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }
    public void setDesc(String desc) 
    {
        this.desc = desc;
    }

    public String getDesc() 
    {
        return desc;
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
            .append("time", getTime())
            .append("desc", getDesc())
            .toString();
    }
}
