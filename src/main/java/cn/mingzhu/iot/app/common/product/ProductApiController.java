package cn.mingzhu.iot.app.common.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.ResponseData;
import cn.mingzhu.iot.app.bas.constant.DmCode;
import cn.mingzhu.iot.app.bas.constant.Pager;
import cn.mingzhu.iot.app.bas.constant.TrcConstants;
import cn.mingzhu.iot.app.bas.entity.Product;
import cn.mingzhu.iot.app.bas.exception.AppEntityNotFoundException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductApiController {

	@Autowired
	private ProductService service;

	@PostMapping("/product")
	@ApiOperation(value = "新增工序", notes = "新增工序", produces = "application/json")
	@ApiImplicitParam(name = "record", value = "ProductEx", dataType = "ProductEx")
	public ResponseData<ProductEx> add(HttpServletRequest request, @RequestBody ProductEx record) {

		log.info("/processname");
		log.info("record={}", record);
		int rslt = 0;

		rslt = service.insert(record);

		if (rslt != 1) {
			throw new AppEntityNotFoundException("API参数错误");
		}

		return new ResponseData<ProductEx>(DmCode.CREATED, record);
	}

	@GetMapping("/product")
	@ApiOperation(value = "获取分页", notes = "获取分页详细信息", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pn", value = "页数", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "ps", value = "每页记录数", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "sf", value = "排序字段: id, name(工序名称)", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "st", value = "排序方式：asc, desc", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "search", value = "模糊查询字段，只针对name(工序名称), code(编号),memo(备注)字段", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "filter", value = "过滤条件", dataType = "HashMap<String, Object>", paramType = "query") })
	public ResponseData<PageInfo<ProductRow>> pageInfo(HttpServletRequest request,
			@RequestParam(value = "pn", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "ps", required = false, defaultValue = Pager.DEFAULT_PAER_SIZE) Integer pageSize,
			@RequestParam(value = "sf", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "st", required = false, defaultValue = "desc") String sortType,
			@RequestParam(value = "search", required = false) String search) {

		log.info("/product");
		log.info("pn={}, ps={}, sf={}, st={}, search={}", pageNumber, pageSize, sortField, sortType, search);

		Example example = new Example(Product.class);
		Criteria criteria = example.createCriteria();
		if (search != null && !"".equals(search)) {
			criteria.orLike("code", "%" + search + "%");
			criteria.orLike("name", "%" + search + "%");
			criteria.orLike("memo", "%" + search + "%");
		}

		// Criteria criteria4 = example.createCriteria();
		// criteria4.andEqualTo("removed", RemovedConstants.REMOVED_NO);
		// example.and(criteria4);

		if (TrcConstants.ASC.equalsIgnoreCase(sortType)) {
			example.orderBy(sortField).asc();
		} else {
			example.orderBy(sortField).desc();
		}

		PageInfo<ProductRow> pageInfo = service.page(1, example, pageNumber, pageSize);

		return new ResponseData<PageInfo<ProductRow>>(DmCode.OK, pageInfo);
	}

	@GetMapping("/product/{id}")
	@ApiOperation(value = "获取工序信息", notes = "获取工序详细信息，含工序明细详细信息", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "ID", dataType = "Integer", paramType = "path")
	public ResponseData<ProductEx> get(HttpServletRequest request, @PathVariable("id") Integer id) {

		log.info("/product/{id}");
		log.info("id={}", id);

		ProductEx record = service.findExById(id);

		return new ResponseData<ProductEx>(DmCode.OK, record);
	}

	@PutMapping("/product/{id}")
	@ApiOperation(value = "修改工序", notes = "修改单条工序记录", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "工序ID", dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "record", value = "Integer", dataType = "ProductEx") })
	public ResponseData<ProductEx> update(HttpServletRequest request, @PathVariable("id") Integer id,
			@RequestBody ProductEx record) {

		log.info("/product/{id}");
		log.info("id={}, record={}", id, record);

		ProductEx rslt = service.update(record);

		return new ResponseData<ProductEx>(DmCode.OK, rslt);
	}

	@DeleteMapping("/product")
	@ApiOperation(value = "批量删除工序清单", notes = "批量删除工序清单", produces = "application/json")
	@ApiImplicitParam(name = "ids", value = "工序ID列表", allowMultiple = true, dataType = "Integer", paramType = "body")
	public ResponseData<List<Integer>> deleteByIds(@RequestBody List<Integer> ids) {

		log.info("ids={}", ids);

		boolean rslt = service.deleteByIds(1, ids);

		if (rslt != true) {
			throw new AppEntityNotFoundException("删除失败！");
		}
		return new ResponseData<List<Integer>>(DmCode.NO_CONTENT, ids);
	}
}
