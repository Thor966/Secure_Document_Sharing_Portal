package com.doc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doc.dto.AdminDTO;
import com.doc.dto.GlobalAuditLogsDTO;
import com.doc.service.IAdminDashboardService;
import com.doc.service.IAuditLogsService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AdminGlobalSecurityLogs
{
	
	@Autowired
	private IAuditLogsService auditService;
	
	@Autowired
	private IAdminDashboardService dashboardService;
	
	
	
	// get the logged in Admin
		@PreAuthorize("hasAuthority('ADMIN')")
		public AdminDTO getLoggedInAdmin()
		{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
			
			String username = authentication.getName();
			
			AdminDTO admin = dashboardService.getAdminByUsername(username);
			
			return admin;
		}
	
	
	// get the global audit logs for admin
		@GetMapping("/globalAuditLogs")
		@PreAuthorize("hasAuthority('ADMIN')")
		public ResponseEntity<?> getAllGlobalAuditLogs(@RequestParam(required = false) String keyword,
														@RequestParam(required = false) String status,
														@PageableDefault(page=0, size=20, sort="insertedOn", direction=Direction.DESC) Pageable pageable)
		{
			
			// get the logged in Admin
			AdminDTO admin = getLoggedInAdmin();
			
			
			
			Page<GlobalAuditLogsDTO> auditPage;
			
			
			if(keyword != null && !keyword.trim().isEmpty() &&
					status != null && !status.trim().isEmpty())
			{
				auditPage = auditService.filterByKeywordAndStatus(keyword, status, pageable);
			}
			else if(keyword != null && !keyword.trim().isEmpty())
			{
				auditPage = auditService.filterByKeyword(keyword, pageable);
			}
			else if(status != null && !status.trim().isEmpty())
			{
				auditPage = auditService.filterByStatus(status, pageable);
			}
			else
			{
				auditPage = auditService.getGlobalAuditLogs(pageable);
			}
			
			
			// get the service class method
			
			Map<String, Object> response = new HashMap<>();
			
			response.put("content", auditPage.getContent());
			response.put("currentPage", auditPage.getNumber());
		    response.put("pageSize", auditPage.getSize());
		    response.put("totalElements", auditPage.getTotalElements());
		    response.put("totalPages", auditPage.getTotalPages());
		    response.put("first", auditPage.isFirst());
		    response.put("last", auditPage.isLast());
			
			
		    return ResponseEntity.ok(response);
		    
		}
		
		
		// export CSV files
		@GetMapping("/export-csv")
		@PreAuthorize("hasAuthority('ADMIN')")
		public void exportToCSVFile(HttpServletResponse res) throws IOException
		{
			
			// get logged in Admin
			AdminDTO admin = getLoggedInAdmin();
			
			res.setContentType("text/csv");
	        res.setHeader("Content-Disposition", "attachment; filename=GlobalSecurityLogs.csv");
	        
	        List<GlobalAuditLogsDTO> logList = auditService.getAllGlobalAuditLogs();
	        
	        PrintWriter writer = res.getWriter();
	        
	        writer.println("Timestamp, User Name, Action, Doc Name, Status, Source");
	        
	        for(GlobalAuditLogsDTO log : logList)
	        {
	        	writer.println(
	        				
	        			log.getTimestamp() + "," +
	        			log.getUsername() + "," +
	        			log.getAction() + "," +
	        			log.getDocumentName() + "," +
	        			log.getStatus() + "," +
	        			log.getSource() 
	        			
	        			
	        			);
	        	
	        	 
	        }
	        
	        writer.flush();
            writer.close();

		}
		
		
		

}
