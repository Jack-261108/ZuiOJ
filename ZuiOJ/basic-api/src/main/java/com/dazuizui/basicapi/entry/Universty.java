package com.dazuizui.basicapi.entry;

import java.io.Serializable;
import java.util.Date;

/**
 * 大学实体类
 */
public class Universty implements Serializable {
    private Long id;
    private String chineseName;  //中文名字
    private String englishName;  //英文名字
    private String createBy;     //创建人
    private Long   createById;   //创建人id
    private Date createTime;     //创建时间
    private String updateBy;     //修改人
    private Long updateById;     //修改人id
    private Date updateTime;     //修改时间
    private int delFalg;         //逻辑删除
    private int status;          //状态

    @Override
    public String toString() {
        return "Universty{" +
                "id=" + id +
                ", chineseName='" + chineseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createById=" + createById +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateById=" + updateById +
                ", updateTime=" + updateTime +
                ", delFalg=" + delFalg +
                ", status=" + status +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getUpdateById() {
        return updateById;
    }

    public void setUpdateById(Long updateById) {
        this.updateById = updateById;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getDelFalg() {
        return delFalg;
    }

    public void setDelFalg(int delFalg) {
        this.delFalg = delFalg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Universty() {
    }

    public Universty(Long id, String chineseName, String englishName, String createBy, Long createById, Date createTime, String updateBy, Long updateById, Date updateTime, int delFalg, int status) {
        this.id = id;
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.createBy = createBy;
        this.createById = createById;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateById = updateById;
        this.updateTime = updateTime;
        this.delFalg = delFalg;
        this.status = status;
    }
}
