package com.example.staffMember.repo;

import com.example.staffMember.model.StaffMemberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffMemberRepo extends JpaRepository<StaffMemberModel,Integer> {

    @Query( value ="SELECT * FROM staff_member_model", nativeQuery = true)
    List<StaffMemberModel> findTableDetailsStaffMember();

    @Query(value = "SELECT * FROM staff_member_model WHERE staff_member_id=?1",nativeQuery = true)
    StaffMemberModel findStaffMemberModelById(int staffMemberId);

    @Query(value = "SELECT COUNT(*)>0 FROM staff_member_model WHERE first_name=?1",nativeQuery = true)
    Integer memberExistsByName(String firstName);

    @Query(value = "SELECT COUNT(*)>0 FROM staff_member_model WHERE email=?1",nativeQuery = true)
    Integer memberExistsByEmail(String  email);

    @Query(value = "SELECT COUNT(*)>0 FROM staff_member_model WHERE contact_number=?1",nativeQuery = true)
    Integer memberExistsByContactNumber(String contactNumber);
}
