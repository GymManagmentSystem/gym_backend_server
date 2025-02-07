package com.example.member.repo;

import com.example.member.dto.BasicMemberDto;
import com.example.member.dto.MemberDto;
import com.example.member.model.MemberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberRepo extends JpaRepository<MemberModel, Integer>  {
@Query(value = "SELECT * FROM member_model",nativeQuery = true)
List<MemberModel> getAllMembersDetails();

@Query(value = "SELECT * FROM member_model ORDER BY member_id DESC LIMIT 1",nativeQuery = true)
MemberModel getLatestMember();

@Query(value = "SELECT COUNT(*)>0 FROM member_model WHERE first_name=?1",nativeQuery = true)
Integer memberExistsByName(String firstName);

@Query(value = "SELECT COUNT(*)>0 FROM member_model WHERE email=?1",nativeQuery = true)
Integer memberExistsByEmail(String  email);

@Query(value = "SELECT COUNT(*)>0 FROM member_model WHERE contact_number=?1",nativeQuery = true)
Integer memberExistsByContactNumber(String contactNumber);

@Modifying
@Query(value = "UPDATE member_model SET address=:#{#member.address},age=:#{#member.age},contact_number=:#{#member.contactNumber},date_registered=:#{#member.dateRegistered},email=:#{#member.email},first_name=:#{#member.firstName},last_name=:#{#member.lastName},gender=:#{#member.gender},height=:#{#member.height},weight=:#{#member.weight} WHERE member_id=:#{#member.memberId}",nativeQuery = true)
Integer updateMemberDetails(@Param("member") MemberModel member);

@Query(value = "SELECT email FROM member_model WHERE first_name=?1",nativeQuery = true)
String getMemberEmail(String firstName);
}




