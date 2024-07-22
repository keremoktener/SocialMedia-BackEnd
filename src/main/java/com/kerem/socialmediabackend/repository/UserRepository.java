package com.kerem.socialmediabackend.repository;


import com.kerem.socialmediabackend.entity.User;
import com.kerem.socialmediabackend.view.VwUserAvatar;
import com.kerem.socialmediabackend.view.VwUserForFollow;
import com.kerem.socialmediabackend.view.VwUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findOptionalByUserNameAndPassword(String userName, String password);

    List<User> findAllByUserNameContaining(String userName);

    //Query'de değerler view'dekilerle aynı sırada olmalı!
    @Query("select new com.kerem.socialmediabackend.view.VwUserProfile(u.userName,u.avatar,u.followerCount,u.followingCount,u.name,u.about,u.bornDate,u.phone,u.address) from User u where u.id=?1")
    VwUserProfile getByAuthId(Long id);

    //@Query("select new com.muhammet.view.VwUserAvatar(u.userName,u.avatar) from  User u where  u.id=?1")
    //VwUserAvatar getUserNameAndAvatar(Long id);

    @Query("select new com.kerem.socialmediabackend.view.VwUserAvatar(u.id,u.userName,u.avatar) from  User u ")
    List<VwUserAvatar> getAllUserAvatar();

    @Query("select new com.kerem.socialmediabackend.view.VwUserAvatar(u.id, u.userName, u.avatar) from User u where u.id=?1")
    VwUserAvatar getUserAvatarAndUserName(Long id);

    @Query("SELECT new com.kerem.socialmediabackend.view.VwUserForFollow(u.id,u.name,u.userName,u.avatar) FROM User u WHERE u.id NOT IN :followedIds")
    List<VwUserForFollow> findUsersNotFollowedBy(@Param("followedIds") List<Long> followedIds);


}
