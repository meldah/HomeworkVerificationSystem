package com.tusofia.app.homeworkVerification.repository;

import javax.transaction.Transactional;

import com.tusofia.app.homeworkVerification.domain.entities.User;

@Transactional
public interface UserRepository extends UserBaseRepository<User> {
}
