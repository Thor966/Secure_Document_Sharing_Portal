package com.doc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.AdminDocumentManageDTO;
import com.doc.service.IAdminDocumentService;

@RestController
public class AdminDocumentController
{
	
	@Autowired
	private IAdminDocumentService docService;
	
	
	// get the all documents
	@GetMapping("/fetchAdminManageDocData")
	public ResponseEntity<?> fetchAllManageDocumentData(@RequestParam(required = false) String keyword,
														@RequestParam(required = false) String status,
													@PageableDefault(page=0, size=10, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
	{
		// get the logged In User
		
		
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
	
	

}
