package com.lab.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Table(name = "permissions", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Permission implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "permission_id", unique = true, nullable = false)
	private int permissionId;
	
	@Column(name = "permission_name", nullable = false, length = 255)
	private String permissionName;
	
	@Column(name = "description")
	private String description;
	
	@ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
}
