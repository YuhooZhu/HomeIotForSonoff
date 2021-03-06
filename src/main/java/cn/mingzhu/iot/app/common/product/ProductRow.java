package cn.mingzhu.iot.app.common.product;

import cn.mingzhu.iot.app.bas.entity.Product;
import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRow extends Product {

	private static final long serialVersionUID = 1L;

	// /**
	// * 是否有已审核的明细记录(版本)
	// */
	// @Transient
	// private Boolean isStateActived;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

}