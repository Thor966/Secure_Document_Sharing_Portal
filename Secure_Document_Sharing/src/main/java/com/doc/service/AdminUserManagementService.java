package com.doc.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doc.dto.AdminUserManageDTO;
import com.doc.dto.UserStorageProjection;
import com.doc.entity.User;
import com.doc.repository.DocumentsRepository;
import com.doc.repository.UserRepository;

@Service
public class AdminUserManagementService implements IAdminUserManagementService 
{
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private DocumentsRepository docRepo;
	
	
	
	// get the disabled user count
		@Override
		public Long getDisabledUserCount() {
			
			return userRepo.countByStatus("DISABLED");
		}
	
	
	// get all users data 
	@Override
	public Page<AdminUserManageDTO> getAllUsersData(Pageable pageable) {
		
		Page<User> userData = userRepo.findAll(pageable);
		
		
		 // Get storage usage in ONE query
	    List<UserStorageProjection> storageList =
	            docRepo.getStorageUsageForAllUsers();

	    // Convert list to Map for fast lookup
	    Map<Long, Long> storageMap = storageList.stream()
	            .collect(Collectors.toMap(
	                    UserStorageProjection::getUserId,
	                    UserStorageProjection::getTotalStorage
	            ));
	    
	    
		
		return userData.map(user->{
			
			// create dto object
			AdminUserManageDTO dto = new AdminUserManageDTO();
			
			dto.setUid(user.getUid());
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
			dto.setEmail(user.getEmail());
			dto.setStatus(user.getStatus());
			dto.setLastLogin(user.getLastLogin());
			dto.setJoinOn(user.getInsertedOn());
			// Fetch storage from map
	        Long bytes = storageMap.getOrDefault(user.getUid(), 0L);

	        // Convert to MB
	        double mb = bytes / (1024.0 * 1024.0);

	        dto.setStorageUsed(mb);
	        
	        System.out.println("Service Debug data: " + dto);
			
			return dto;
			
			
			
		});
	}
	

}
