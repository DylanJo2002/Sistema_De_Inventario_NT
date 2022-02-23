package com.nt.Backend_NT.model;

import java.util.List;

import com.nt.Backend_NT.entities.LabelEntity;

import lombok.Data;

@Data
public class LabelsResponse {
	private List<LabelEntity> labels;

	public LabelsResponse(List<LabelEntity> labels) {
		this.labels = labels;
	}
	
	public LabelsResponse() {};
}
