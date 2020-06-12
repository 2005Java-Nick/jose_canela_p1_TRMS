package com.revature.Objects;

import java.io.Serializable;

public class GradeFormat implements Serializable {

	private int gradeFormatId;
	private String format;
	private String defaultPassingGrade;

	public GradeFormat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getGradeFormatId() {
		return gradeFormatId;
	}

	public void setGradeFormatId(int gradeFormatId) {
		this.gradeFormatId = gradeFormatId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDefaultPassingGrade() {
		return defaultPassingGrade;
	}

	public void setDefaultPassingGrade(String defaultPassingGrade) {
		this.defaultPassingGrade = defaultPassingGrade;
	}

}
