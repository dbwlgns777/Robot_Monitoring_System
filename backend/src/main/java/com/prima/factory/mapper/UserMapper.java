package com.prima.factory.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("""
        SELECT id,
               username,
               full_name AS fullName,
               password_hash AS passwordHash,
               approval_status AS approvalStatus,
               is_locked AS isLocked,
               is_active AS isActive,
               failed_login_count AS failedLoginCount,
               last_login_at AS lastLoginAt
          FROM system_user
         WHERE username = #{username}
        """)
    Map<String, Object> find(String username);

    @Insert("INSERT INTO user_registration_request(username,full_name,password_hash,factory_id,department,position,phone,email,requested_role,status) VALUES(#{username},#{name},#{passwordHash},#{factoryId},#{department},#{position},#{phone},#{email},#{requestedRole},'PENDING')")
    int signup(Map<String, Object> body);

    @Update("UPDATE system_user SET failed_login_count=#{count},is_locked=#{locked},last_login_at=#{lastLogin} WHERE id=#{id}")
    int loginResult(Map<String, Object> body);
}
