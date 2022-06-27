package com.tusofia.app.homeworkVerification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tusofia.app.homeworkVerification.domain.entities.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
	Optional<File> findByUrl(String url);
}
