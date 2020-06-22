package cn.mingzhu.iot.app.bas;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class Response implements Comparable<Response> {
	private int seq;

	private int error;

	public int getSeq() {
		return seq;
	}

	@Override
	public int compareTo(Response o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
