package com.geemeta.core.entity;

import com.geemeta.core.gql.meta.Id;
import com.geemeta.core.gql.meta.Title;

/**
 * @author hongxq
 */
//@MappedSuperclass
public abstract class IdEntity{
//implements Serializable
	protected Long id;

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Title(title = "序号")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
