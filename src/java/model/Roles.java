package model;

import java.util.List;
import util.di.annotation.Column;
import util.di.annotation.Nested;

public class Roles {

    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "roleName")
    private String roleName;

    // QUAN HỆ 1-N: 1 role có thể có nhiều user
    @Nested
    private List<User> users;

    // Các hằng số tên role định nghĩa sẵn
    public static final String ADMIN = "ADMIN";
    public static final String MANAGER = "MANAGER";
    public static final String STAFF = "STAFF";

    // Constructors
    public Roles() {
    }

    public Roles(String roleName) {
        this.roleName = roleName;
    }

    // Getters & Setters
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Roles{"
                + "roleId=" + roleId
                + ", roleName='" + roleName + '\''
                + '}';
    }
}