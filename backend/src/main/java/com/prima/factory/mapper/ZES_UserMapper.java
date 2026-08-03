package com.prima.factory.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ZES_UserMapper
{
    @Select("""
        SELECT id, username, full_name AS fullName, password_hash AS passwordHash,
               approval_status AS approvalStatus, is_locked AS isLocked, is_active AS isActive
          FROM system_user
         WHERE username = #{ZES_username}
        """)
    Map<String, Object> ZES_findByUsername(@Param("ZES_username") String ZES_username);

    @Select("""
        SELECT id FROM factory
         WHERE is_active = 1
           AND (factory_code = #{ZES_factory} OR factory_name = #{ZES_factory})
         LIMIT 1
        """)
    Long ZES_findFactoryId(@Param("ZES_factory") String ZES_factory);

    @Select("SELECT COUNT(*) FROM system_user WHERE username = #{ZES_username}")
    int ZES_countUsers(@Param("ZES_username") String ZES_username);

    @Select("SELECT COUNT(*) FROM user_registration_request WHERE username = #{ZES_username} AND status = 'PENDING'")
    int ZES_countPendingRequests(@Param("ZES_username") String ZES_username);

    @Insert("""
        INSERT INTO user_registration_request(
            username, full_name, password_hash, factory_id, department,
            position, phone, email, requested_role, status)
        VALUES(
            #{ZES_username}, #{ZES_name}, #{ZES_passwordHash}, #{ZES_factoryId},
            #{ZES_department}, #{ZES_position}, #{ZES_phone}, #{ZES_email},
            #{ZES_requestedRole}, 'PENDING')
        """)
    int ZES_insertRegistration(Map<String, Object> ZES_registration);
}
