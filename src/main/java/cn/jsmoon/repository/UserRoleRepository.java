package cn.jsmoon.repository;

import cn.jsmoon.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户角色关联Repository类
 * @author: LTQ
 * @create: 2018-09-17 13:54
 **/
public interface UserRoleRepository extends JpaRepository<UserRole,Integer>,JpaSpecificationExecutor<UserRole> {

    /**
     * 根据用户ID删除用户角色关联表信息
     * @param userId
     */
    @Query(value = "delete from t_user_role where user_id=?1",nativeQuery = true)
    @Modifying
    void deleteByUserId(Integer userId);

    /**
     * 根据角色ID删除用户角色关联表信息
     * @param roleId
     */
    @Query(value = "delete from t_user_role where role_id=?1",nativeQuery = true)
    @Modifying
    void deleteByRoleId(Integer roleId);
}
