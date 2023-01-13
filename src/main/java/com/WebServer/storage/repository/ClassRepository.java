package com.WebServer.storage.repository;

import com.WebServer.storage.entity.CustomClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<CustomClass, Integer> {

}
