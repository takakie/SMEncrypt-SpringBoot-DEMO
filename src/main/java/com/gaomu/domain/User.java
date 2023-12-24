package com.gaomu.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    @TableId
    private Long id;
    private String userName;
    private String nickName;
    private String password;
    /**
     * 账号状态(0正常 1停用）
     */
    private String status;
    private String email;
    /**
     * 用户性别（0男，1女，2未知）
     */
    private String phoneNumber;
    private String sex;
    private String avatar;
    private String userType;
    private Long createBy;
    private Date createTime;
    private Date updateTime;

    private Integer delFlag;
    private String secretKey;
    private String iv;



}
