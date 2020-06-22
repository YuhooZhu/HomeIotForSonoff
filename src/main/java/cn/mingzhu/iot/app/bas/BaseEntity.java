package cn.mingzhu.iot.app.bas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -3016921355520527945L;

	@Column(name = "sid")
	private Integer sid;
}
