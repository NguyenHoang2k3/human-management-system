package com.lab.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import com.lab.lib.enumerated.SystemRole;

@Table(name = "roles", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", unique = true, nullable = false)
	private int roleId;
	
	@Column(name = "role_name", nullable = false, length = 255)
	@Enumerated(EnumType.STRING)
	private SystemRole roleName;
	
	@Column(name = "description")
	private String description;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "role_permission",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id")
		)
	private List<Permission> permissions;
	
}
