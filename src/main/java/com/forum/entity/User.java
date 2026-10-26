package com.forum.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data                        // Lombok：自动生成 getter/setter/toString
@TableName("user")           // 告诉 MyBatis-Plus：我对应数据库的 user 表
public class User {

    @TableId(type = IdType.AUTO)   // 主键，自增（对应建表时的 AUTO_INCREMENT）
    private Long id;

    private String username;       // 字段名和表的列名一一对应
    private String password;
    private String email;
    private String avatar;

    private LocalDateTime createdAt;    // 表的 created_at 列
    private LocalDateTime updatedAt;    // 表的 updated_at 列
}