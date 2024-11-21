package com.example.demo.repo;

import com.example.demo.model.PackageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PackageRepo extends JpaRepository<PackageModel,String> {
    @Query(value = "SELECT * FROM package_model ORDER BY package_id DESC LIMIT 1",nativeQuery = true)
    PackageModel getLastExercise();

    @Query(value = "SELECT COUNT(*)>0 FROM package_model WHERE package_name=?1",nativeQuery = true)
    Integer existByName(String name);
}
