package com.example.staffMember.repo;

import com.example.staffMember.model.StaffMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffMemberRepo extends JpaRepository<StaffMemberModel,Integer> {
}
