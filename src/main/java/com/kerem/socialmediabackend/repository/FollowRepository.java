package com.kerem.socialmediabackend.repository;

import com.kerem.socialmediabackend.entity.Follow;
import com.kerem.socialmediabackend.view.VwSearchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f.followId FROM Follow f WHERE f.userId = :userId")
    List<Long> findFollowedUserIdsByUserId(@Param("userId") Long userId);

    @Query("select new com.kerem.socialmediabackend.view.VwSearchUser(u.id,u.userName,u.name,u.avatar) from User u where u.userName ilike ?1")
    List<VwSearchUser> getAllByUserName(String userName);

    List<Follow> findAllByUserId(Long userId);
}
