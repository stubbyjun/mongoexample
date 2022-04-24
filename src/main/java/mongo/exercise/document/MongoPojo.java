package mongo.exercise.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MongoPojo {

	@Id   //요것이 고유 키 값
	private String id;
	
	private String fisrtName;
	private String lastName;
	private String job;
	private String location;
	private String date;
	private String salary;
	private Object all;  //모든 객체를 매핑
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFisrtName() {
		return fisrtName;
	}
	public void setFisrtName(String fisrtName) {
		this.fisrtName = fisrtName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public Object getAll() {
		return all;
	}
	public void setAll(Object all) {
		this.all = all;
	}
	@Override
	public String toString() {
		return "MongoPojo [id=" + id + ", fisrtName=" + fisrtName + ", lastName=" + lastName + ", job=" + job
				+ ", location=" + location + ", date=" + date + ", salary=" + salary + ", all=" + all + "]";
	}

}
