package test.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.eweb4j.mvc.validator.annotation.Length;
import org.eweb4j.mvc.validator.annotation.Required;
import org.eweb4j.orm.Model;

import test.Users;

@Entity
@Table(name = "t_pet")
public class Pet extends Model<Pet> implements Serializable{
	
	private static final long serialVersionUID = 3841852708264752696L;

	public final static Pet inst = new Pet();
	
	@Id
	@Column(name = "id")
	private long petId;

	@Column(name = "num", unique=true)
	@PrimaryKeyJoinColumn
	@Required
	private String number;

	@Column(unique = true)
	@Length(min = 5, max = 8)
	private String name;

	private int age;
	
	
	@Column(name = "cate")
	private String type;// 只能添加猫和狗两种类型

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="master_id", referencedColumnName="id")
	private Master master;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private Users user;
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Master getMaster() {
		return master;
	}

	public void setMaster(Master master) {
		this.master = master;
	}

	public long getPetId() {
		return petId;
	}

	public void setPetId(long petId) {
		this.petId = petId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Pet [petId=" + petId + ", number=" + number + ", name=" + name
				+ ", age=" + age + ", type=" + type + ", master=" + master
				+ ", user=" + user + "]";
	}

}
