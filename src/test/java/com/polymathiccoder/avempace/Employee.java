package com.polymathiccoder.avempace;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.polymathiccoder.avempace.config.Region;
import com.polymathiccoder.avempace.meta.annotation.Attribute;
import com.polymathiccoder.avempace.meta.annotation.Entity;
import com.polymathiccoder.avempace.meta.annotation.PersistAsType;
import com.polymathiccoder.avempace.meta.annotation.Table;
import com.polymathiccoder.avempace.meta.annotation.constraint.LSI;
import com.polymathiccoder.avempace.meta.annotation.constraint.PrimaryHashKey;
import com.polymathiccoder.avempace.meta.annotation.constraint.PrimaryRangeKey;

@Entity (primaryRegion = Region.AP_NORTHEAST_1)
@Table (name = "tbl_employee")
@AutoProperty
public class Employee {
	@PrimaryRangeKey(persistAsType = PersistAsType.NUMBER)
	private Long id;

	@PrimaryHashKey
	private String company;

	@Attribute(name = "first_name")
	private String firstName;

	@Attribute(name = "age", persistAsType = PersistAsType.NUMBER)
	private Integer age;

	@LSI(indexName = "idx_location")
	private String location;

	public Employee() {
	}

	public Employee(long id, String firstName, int age, String company, String location) {
		this.id = id;
		this.firstName = firstName;
		this.age = age;
		this.company = company;
		this.location = location;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getCompany() { return company; }
	public void setCompany(String company) { this.company = company; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public Integer getAge() { return age; }
	public void setAge(Integer age) { this.age = age; }

	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }

	// Common methods
	@Override
	public boolean equals(final Object other) { return Pojomatic.equals(this, other); }
	@Override
	public int hashCode() { return Pojomatic.hashCode(this); }
	@Override
	public String toString() { return Pojomatic.toString(this); }
}
