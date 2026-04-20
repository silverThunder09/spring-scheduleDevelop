package com.scheduledevelop.user.entity;

import com.scheduledevelop.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    private String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void updateUser(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
