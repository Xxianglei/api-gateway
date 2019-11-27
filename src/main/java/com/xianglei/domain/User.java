package com.xianglei.domain;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 描述：用户/管理实体
 * 时间：[2019/11/27:10:45]
 * 作者：xianglei
 * params: FLOW_IDvarchar(64) NOT NULL流水号
 * NAMEvarchar(64) NULL姓名
 * PASSWORDvarchar(64) NOT NULL密码
 * ACCOUNTvarbinary(64) NOT NULL账号
 * CREATE_TIMEdatetime NULL创建时间
 * PHONEvarchar(64) NULL手机号
 * STATUSint(1) NULL0/1下线/上线
 * VIPint(1) NULL0/1 会员/非会员
 */
@Component
public class User implements Serializable {


    private static final long serialVersionUID = -4240023762185445691L;
    private String flowId;
    private String name;
    private String password;
    private String account;
    private String create_date;
    private String phone;
    private int status;
    private int vip;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }
}
