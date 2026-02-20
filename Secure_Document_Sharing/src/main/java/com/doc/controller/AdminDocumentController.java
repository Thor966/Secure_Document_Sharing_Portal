package com.doc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.AdminDTO;
import com.doc.dto.AdminDocumentManageDTO;
import com.doc.service.IAdminDashboardService;
import com.doc.service.IAdminDocumentService;

@RestController
public class AdminDocumentController
{
	
	@Autowired
	private IAdminDocumentService docService;
	
	@Autowired
	private IAdminDashboardService dashboardService;
	
	
	
	// get the logged in Admin
	public AdminDTO getLoggedInAdmin()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
		
		String username = authentication.getName();
		
		AdminDTO admin = dashboardService.getAdminByUsername(username);
		
		return admin;
	}
	
	
	
	// get the doc, active doc, expire doc and revoke doc count
	@GetMapping("/documentStats")
	public ResponseEntity<?> getDocumentStatsCount()
	{
		// get the logged in user
		AdminDTO admin = getLoggedInAdmin();

			// for total doc count
			Long docCount = docService.getTotalSharedDocCount();
			Long activeDocCount = docService.getTotalActiveDocCount();
			Long expiredDocCount = docService.getTotalExpiredDocumentCount();
			Long revokedDocCount = docService.getTotalRevokeDocumentCount();
			
			// set all these in response
			
			Map<String, Object> response = new HashMap<>();
			
			response.put("docCount", docCount);
			response.put("activeDocCount", activeDocCount);
			response.put("expiredDocCount", expiredDocCount);
			response.put("revokedDocCount", revokedDocCount);
			
			return ResponseEntity.ok(response);
	}
	
	
	
	// get the all documents
	@GetMapping("/fetchAdminManageDocData")
	public ResponseEntity<?> fetchAllManageDocumentData(@RequestParam(required = false) String keyword,
														@RequestParam(required = false) String status,
													@PageableDefault(page=0, size=10, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		// get the logged In User
		AdminDTO admin = getLoggedInAdmin();
		
		Page<AdminDocumentManageDTO> docPage;
		
		if(keyword != null && !keyword.trim().isEmpty() &&
			status != null && !status.trim().isEmpty())
		{
			docPage = docService.filterByKeywordAndStatus(keyword, status, pageable);
			
			System.out.println("Both filter Data "+ docPage);
		}
		else if(keyword != null && !keyword.trim().isEmpty())
		{
			docPage = docService.filterByKeyword(keyword, pageable);
			
			System.out.println("Filter by keyword "+docPage);
		}
		else if(status != null && !status.trim().isEmpty())
		{
			docPage = docService.filterByStatus(status, pageable);
			System.out.println("filter by status "+ docPage);
		}
		else
		{
			docPage = docService.getAllDocuments(pageable);
		}
		
	
		
		Map<String, Object> response = new HashMap<>();
		
		
		response.put("content", docPage.getContent());
	    response.put("currentPage", docPage.getNumber());
	    response.put("pageSize", docPage.getSize());
	    response.put("totalElements", docPage.getTotalElements());
	    response.put("totalPages", docPage.getTotalPages());
	    response.put("first", docPage.isFirst());
	    response.put("last", docPage.isLast());
		
		return ResponseEntity.ok(response);
	}
	
	
	
	// user force revoked
	@PostMapping("/forceRevoke/{dpid}")
	public ResponseEntity<?> userForceRevokeByAdmin(@PathVariable("dpid") Long dpid, @RequestParam("action") String action)
	{
		
		// get the logged in Admin
		AdminDTO admin = getLoggedInAdmin();
		
		// get the service class method
		docService.forceRevokeDoc(dpid, action);
		
		return ResponseEntity.ok("Document are Revoked Successfully");
	}
	

}
