package com.doc.dto;

import lombok.Data;

@Data
public class UserDTO 
{
	private Long uid;
	private String firstName;
    private String lastName;
    private String email;
    private String password;

}
