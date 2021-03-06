package cn.jsmoon.repository;

import cn.jsmoon.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色repository类
 * @author: LTQ
 * @create: 2018-08-22 14:49
 **/
public interface RoleRepository extends JpaRepository<Role,Integer>,JpaSpecificationExecutor<Role> {

    /**
     * 通过用户ID查找角色集合
     * @param id
     * @return
     */
    @Query(value = "SELECT r.* FROM t_user u,t_role r,t_user_role ur WHERE ur.user_id=u.id AND ur.role_id=r.id AND u.id=?1",nativeQuery = true)
    List<Role> findByUserId(Integer id);

    /**
     * 通过角色名查找角色实体
     * @param roleName
     * @return
     */
    @Query(value = "select * from t_role where name=?1",nativeQuery = true)
    Role findByRoleName(String roleName);

}
