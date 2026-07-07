package com.project.campus.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Faculty count of a department
    @Query("""
            SELECT COUNT(u)
            FROM User u
            WHERE u.department.id = :departmentId
              AND u.role = com.project.campus.user.model.Role.FACULTY
            """)
    Long countFaculty(@Param("departmentId") Long departmentId);

    // HOD name of a department
    @Query("""
            SELECT u.name
            FROM User u
            WHERE u.department.id = :departmentId
              AND u.role = com.project.campus.user.model.Role.HOD
            """)
    String getHodName(@Param("departmentId") Long departmentId);

}