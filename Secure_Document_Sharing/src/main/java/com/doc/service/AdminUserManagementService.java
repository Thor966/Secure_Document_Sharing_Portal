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

			
			return dto;
			
			
			
		});
	}
	
	
	
	// filter by both keyword and status
	@Override
	public Page<AdminUserManageDTO> filterByKeywordAndStatus(String keyword, String status, Pageable pageable) {
		
		// get the filter data
		Page<User> bothFilterData = userRepo.searchByKeywordAndStatus(keyword, status, pageable);
		
		 // Get storage usage in ONE query
	    List<UserStorageProjection> storageList =
	            docRepo.getStorageUsageForAllUsers();

	    // Convert list to Map for fast lookup
	    Map<Long, Long> storageMap = storageList.stream()
	            .collect(Collectors.toMap(
	                    UserStorageProjection::getUserId,
	                    UserStorageProjection::getTotalStorage
	            ));
		
		return bothFilterData.map(filter->{
			
			// create the dot object
			
			AdminUserManageDTO dto = new AdminUserManageDTO();
			
			dto.setUid(filter.getUid());
			dto.setFirstName(filter.getFirstName());
			dto.setLastName(filter.getLastName());
			dto.setEmail(filter.getEmail());
			dto.setStatus(filter.getStatus());
			dto.setLastLogin(filter.getLastLogin());
			dto.setJoinOn(filter.getInsertedOn());
			// Fetch storage from map
	        Long bytes = storageMap.getOrDefault(filter.getUid(), 0L);

	        // Convert to MB
	        double mb = bytes / (1024.0 * 1024.0);

	        dto.setStorageUsed(mb);
	        
	        return dto;
			
		});
	}
	
	
	
	
	// filter by keyword
	@Override
	public Page<AdminUserManageDTO> filterByKeyword(String keyword, Pageable pageable) {
		
		
		// get the filter data
				Page<User> filterDataByKeyword = userRepo.searchByKeyword(keyword,pageable);
				
				 // Get storage usage in ONE query
			    List<UserStorageProjection> storageList =
			            docRepo.getStorageUsageForAllUsers();

			    // Convert list to Map for fast lookup
			    Map<Long, Long> storageMap = storageList.stream()
			            .collect(Collectors.toMap(
			                    UserStorageProjection::getUserId,
			                    UserStorageProjection::getTotalStorage
			            ));
				
				return filterDataByKeyword.map(filter->{
					
					// create the dot object
					
					AdminUserManageDTO dto = new AdminUserManageDTO();
					
					dto.setUid(filter.getUid());
					dto.setFirstName(filter.getFirstName());
					dto.setLastName(filter.getLastName());
					dto.setEmail(filter.getEmail());
					dto.setStatus(filter.getStatus());
					dto.setLastLogin(filter.getLastLogin());
					dto.setJoinOn(filter.getInsertedOn());
					// Fetch storage from map
			        Long bytes = storageMap.getOrDefault(filter.getUid(), 0L);

			        // Convert to MB
			        double mb = bytes / (1024.0 * 1024.0);

			        dto.setStorageUsed(mb);
			        
			        return dto;
					
				});
	}
	
	
	
	
	
	// filter by status
	@Override
	public Page<AdminUserManageDTO> filterByStatus(String status, Pageable pageable) {
		
		// get the filter data
		Page<User> filterDataByStatus = userRepo.searchByStatus(status,pageable);
		
		 // Get storage usage in ONE query
	    List<UserStorageProjection> storageList =
	            docRepo.getStorageUsageForAllUsers();

	    // Convert list to Map for fast lookup
	    Map<Long, Long> storageMap = storageList.stream()
	            .collect(Collectors.toMap(
	                    UserStorageProjection::getUserId,
	                    UserStorageProjection::getTotalStorage
	            ));
		
		return filterDataByStatus.map(filter->{
			
			// create the dot object
			
			AdminUserManageDTO dto = new AdminUserManageDTO();
			
			dto.setUid(filter.getUid());
			dto.setFirstName(filter.getFirstName());
			dto.setLastName(filter.getLastName());
			dto.setEmail(filter.getEmail());
			dto.setStatus(filter.getStatus());
			dto.setLastLogin(filter.getLastLogin());
			dto.setJoinOn(filter.getInsertedOn());
			// Fetch storage from map
	        Long bytes = storageMap.getOrDefault(filter.getUid(), 0L);

	        // Convert to MB
	        double mb = bytes / (1024.0 * 1024.0);

	        dto.setStorageUsed(mb);
	        
	        return dto;
			
		});
		
		
		
	}
	
	
	
	
	//update the user status
	@Override
	public void updateUserStatus(Long uid, String status) {
		// get the user data first
		User user = userRepo.findById(uid).orElseThrow(()-> new IllegalAccessError("User Not Found"));
		
		//update the user status
		user.setStatus(status);
		
		// save the user status
		userRepo.save(user);
		
	}
	

}
