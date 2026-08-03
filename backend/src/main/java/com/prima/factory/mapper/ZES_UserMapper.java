package com.prima.factory.mapper;

import java.util.Map;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;

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
        SELECT r.role_code
          FROM user_role ur
          JOIN role r ON r.id = ur.role_id
         WHERE ur.user_id = #{ZES_userId}
         ORDER BY r.role_code
        """)
    List<String> ZES_findRoleCodes(@Param("ZES_userId") long ZES_userId);

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

    @Select("""
        SELECT rr.id,
               rr.username,
               rr.full_name AS fullName,
               rr.factory_id AS factoryId,
               f.factory_name AS factoryName,
               rr.department,
               rr.position,
               rr.phone,
               rr.email,
               rr.requested_role AS requestedRole,
               rr.status,
               rr.created_at AS createdAt
          FROM user_registration_request rr
          LEFT JOIN factory f ON f.id = rr.factory_id
         WHERE rr.status = 'PENDING'
         ORDER BY rr.created_at, rr.id
        """)
    List<Map<String, Object>> ZES_findPendingRegistrations();

    @Select("""
        SELECT id,
               username,
               full_name AS fullName,
               password_hash AS passwordHash,
               requested_role AS requestedRole,
               status
          FROM user_registration_request
         WHERE id = #{ZES_registrationId}
         FOR UPDATE
        """)
    Map<String, Object> ZES_findRegistrationForUpdate(
        @Param("ZES_registrationId") long ZES_registrationId);

    @Select("SELECT id FROM role WHERE role_code = #{ZES_roleCode} LIMIT 1")
    Long ZES_findRoleIdByCode(@Param("ZES_roleCode") String ZES_roleCode);

    @Select("SELECT role_code AS roleCode, role_name AS roleName FROM role ORDER BY id")
    List<Map<String, Object>> ZES_findAssignableRoles();

    @Insert("""
        INSERT INTO system_user(
            username, full_name, password_hash, approval_status, is_locked, is_active)
        VALUES(
            #{ZES_username}, #{ZES_fullName}, #{ZES_passwordHash}, 'APPROVED', 0, 1)
        """)
    @Options(useGeneratedKeys = true, keyProperty = "ZES_userId")
    int ZES_insertApprovedUser(Map<String, Object> ZES_user);

    @Insert("INSERT INTO user_role(user_id, role_id) VALUES(#{ZES_userId}, #{ZES_roleId})")
    int ZES_insertUserRole(
        @Param("ZES_userId") long ZES_userId, @Param("ZES_roleId") long ZES_roleId);

    @Update("""
        UPDATE user_registration_request
           SET status = #{ZES_status},
               reviewed_by = #{ZES_reviewerId},
               reviewed_at = CURRENT_TIMESTAMP(3)
         WHERE id = #{ZES_registrationId}
           AND status = 'PENDING'
        """)
    int ZES_updateRegistrationStatus(
        @Param("ZES_registrationId") long ZES_registrationId,
        @Param("ZES_status") String ZES_status,
        @Param("ZES_reviewerId") long ZES_reviewerId);
}
