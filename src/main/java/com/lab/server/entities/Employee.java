package com.lab.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.lab.lib.enumerated.EmployeeStatus;

@Table(name = "employees", schema = "public")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id", unique = true, nullable = false)
	private int employeeId;
	
	@Column(name = "first_name", nullable = false, length = 255)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 255)
	private String lastName;
	
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;
	
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "hire_date")
	private LocalDate hireDate;
	
	@Column(name = "salary")
	private Double salary;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private EmployeeStatus status;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;
	
	@ManyToOne
	@JoinColumn(name = "position_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Position positionId;
	
	@ManyToOne
	@JoinColumn(name = "department_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Department departmentId;
	
}
