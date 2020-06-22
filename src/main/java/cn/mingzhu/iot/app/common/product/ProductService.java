package cn.mingzhu.iot.app.common.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.BaseService;
import cn.mingzhu.iot.app.bas.entity.Product;
import cn.mingzhu.iot.app.bas.exception.ApiEntityNotFoundException;
import cn.mingzhu.iot.app.bas.exception.ApiIllegalArgumentException;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class ProductService extends BaseService<Product> {

	// @Autowired
	// private UserService userSvc;

	/**
	 * 子表持久类对象转换为视图扩展类对象
	 * 
	 * *** 注意 ***
	 * 
	 * 只能对下级子表的持久类查询（使用service的selectXXX方法），基本数据类型不能使用service的扩展类查询（使用service的findXXX方法）
	 * 
	 * @param source
	 * @return
	 */
	private ProductRow toRow(Product source) {
		log.info("source={}", source);
		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		ProductRow rslt = new ProductRow();
		BeanUtils.copyProperties(source, rslt);

		log.info("rslt={}", rslt);
		return rslt;
	}

	/**
	 * 子表持久类对象转换为视图扩展类对象
	 * 
	 * @param source
	 * @return
	 */
	private ProductEx toEx(Product source) {
		log.info("source={}", source);

		ProductRow row = toRow(source);

		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		ProductEx rslt = new ProductEx();
		BeanUtils.copyProperties(row, rslt);

		log.info("rslt={}", rslt);
		return rslt;
	}

	/**
	 * 根据ID获取持久类实例
	 * 
	 * @param id
	 * @return
	 */
	@Cacheable(value = "product", key = "#id", unless = "#result == null")
	public Product findById(Integer id) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		Product srch = new Product();
		srch.setId(id);
		Product found = super.selectByPrimaryKey(srch);
		if (found == null) {
			return null;
		}
		return found;
	}

	/**
	 * 根据ID获取行信息
	 * 
	 * @param id
	 * @return
	 */
	@Cacheable(value = "product_row", key = "#id", unless = "#result == null")
	public ProductRow findRowById(Integer id) {
		Product found = findById(id);
		return toRow(found);
	}

	/**
	 * 根据ID获取详细信息
	 * 
	 * @param id
	 * @return
	 */
	@Cacheable(value = "product_ex", key = "#id", unless = "#result == null")
	public ProductEx findExById(Integer id) {
		Product found = findById(id);
		return toEx(found);
	}

	/**
	 * 新建
	 * 
	 * 插入数据，最后更新编号
	 * 
	 * @param record
	 * @return
	 */
	public int insert(ProductEx record) {
		if (record == null || record.getName() == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}

		record.setId(0);

		int rslt = 0;
		rslt = super.insert(record);
		if (rslt != 1) {
			throw new ApiEntityNotFoundException("API参数错误");
		}

		return rslt;
	}

	/**
	 * 更新
	 * 
	 * 
	 * 
	 * @param record
	 * @return
	 */
	@Caching(put = { @CachePut(value = "product_ex", key = "#record.id", unless = "#result == null") }, evict = {
			@CacheEvict(value = "product_row", key = "#record.id"),
			@CacheEvict(value = "product", key = "#record.id") })
	public ProductEx update(ProductEx record) {
		if (record.getId() == null || record.getId() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		if (record == null || record.getName() == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}
		Product srch = new Product();
		srch.setId(record.getId());
		Product found = super.selectByPrimaryKey(srch);

		if (found == null) {
			throw new ApiEntityNotFoundException("工序不存在");
		}

		int rslt = super.updateByPrimaryKeySelective(record);

		if (rslt != 1) {
			throw new ApiEntityNotFoundException("API参数错误");
		}

		return findExById(record.getId());
	}

	/**
	 * 获取分页列表
	 * 
	 * @param example
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageInfo<ProductRow> page(Integer siteId, Example example, Integer pageNumber, Integer pageSize) {
		if (siteId == null || siteId <= 0) {
			throw new ApiIllegalArgumentException("API参数错误: siteId");
		}

//		Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("sid", siteId);
//		example.and(criteria);

		PageInfo<Product> source = super.selectByExampleList(example, pageNumber, pageSize);

		List<ProductRow> items = new ArrayList<ProductRow>();
		for (int i = 0; i < source.getList().size(); i++) {
			items.add(toRow(source.getList().get(i)));
		}

		PageInfo<ProductRow> rslt = new PageInfo<ProductRow>();
		BeanUtils.copyProperties(source, rslt);
		rslt.setList(items);

		return rslt;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(Integer siteId, List<Integer> ids) {

		log.debug("delete ids={}", ids);
		if (siteId == null || siteId <= 0) {
			throw new ApiIllegalArgumentException("API参数错误: siteId");
		}

		if (ids == null || ids.size() == 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		preProcessIds(ids);

		List<Product> records = new ArrayList<Product>();

		boolean rslt = false;
		for (int i = 0; i < ids.size(); i++) {
			Product record = new Product();
			record.setId(ids.get(i));
			records.add(record);
		}

		if (!rslt && records.size() > 0) {

			log.info("records={}", records);
			super.deleteBatchByPrimaryKey(records);

			rslt = true;

		}
		return rslt;
	}

	/**
	 * 删除前预处理
	 * 
	 * @param siteId
	 * @param ids
	 */
	private void preProcessIds(List<Integer> ids) {
		Example srchExample = new Example(Product.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andIn("id", ids);

		List<Product> founds = super.selectByExample(srchExample);

	}

}

//
// // -------------------- Dashboard数据统计接口实现 --------------------
// @Override
// public int count(Integer siteId, Integer day, Example example) {
//
// log.info("siteId={}, day={}", siteId, day);
//
// if (siteId == null || siteId <= 0) {
// throw new ApiIllegalArgumentException("API参数错误: siteId");
// }
// if (example == null) {
// example = new Example(TbProcess.class);
// }
//
// Criteria criteria = example.createCriteria();
// criteria.andEqualTo("sid", siteId);
//
// if (day != null) {
// criteria.andGreaterThan("createtime", DateTimeUtil.getTime(day.intValue()));
// }
//
// int rslt = super.countByExample(example);
// log.info("rslt={}", rslt);
//
// return rslt;
// }
//
// @Override
// public List<ProcessRow> latest(Integer siteId, Integer day, Example example)
// {
// log.info("siteId={}, day={}", siteId, day);
//
// if (siteId == null || siteId <= 0) {
// throw new ApiIllegalArgumentException("API参数错误: siteId");
// }
//
// Criteria criteria = example.createCriteria();
// criteria.andEqualTo("sid", siteId);
//
// if (day != null) {
// criteria.andGreaterThan("time", DateTimeUtil.getTime(day.intValue()));
// }
//
// example.setOrderByClause("`id` DESC");
//
// PageInfo<TbProcess> source = super.selectByExampleList(example, 1,
// DmConstants.DASHBOARD_LATEST_SIZE);
//
// List<ProcessRow> rslts = new ArrayList<ProcessRow>();
// for (TbProcess item : source.getList()) {
// rslts.add(toTbProcessRow(item));
// }
//
// return rslts;
// }
//
// /**
// * 按时间段、查询条件聚合统计记录数
// *
// * @param siteId
// * @param day
// * @param example
// * @return
// */
// @Override
// public List<ChartSeries> aggregateByCount(Integer siteId, Integer day,
// Example example, String groupBy) {
// log.info("siteId={}, day={}, example={}", siteId, day, example);
//
// if (siteId == null || siteId <= 0) {
// throw new ApiIllegalArgumentException("API参数错误: siteId");
// }
//
// if (example == null) {
// example = new Example(ReportingFault.class);
// }
//
// if (groupBy == null || "".equals(groupBy)) {
// throw new ApiIllegalArgumentException("API参数错误");
// }
//
// Criteria criteria = example.createCriteria();
// criteria.andEqualTo("sid", siteId);
//
// if (day != null) {
// criteria.andGreaterThan("time", DateTimeUtil.getTime(day.intValue()));
// }
//
// // 聚合查询条件
// AggregateCondition aggCndTbProcessApprovalstate =
// AggregateCondition.builder().aggregateBy("id")
// .aliasName("aggregation").aggregateType(AggregateType.COUNT);
// aggCndTbProcessApprovalstate.groupBy("approvalstate");
//
// example.setOrderByClause("aggregation DESC");
//
// List<TbProcess> aggregateList = super.selectAggregationByExample(example,
// aggCndTbProcessApprovalstate);
//
// Double total = 0D;
// for (TbProcess item : aggregateList) {
// total += item.getAggregation(); // 聚合查询结果值
// }
// log.info("aggregateList={}, total={}", aggregateList, total);
// List<ChartSeries> rslt = new ArrayList<ChartSeries>();
//
// ChartSeries series = new ChartSeries();
// series.setName("出卖申请状态占比");
//
// if (total.equals(0D)) { //
// return rslt;
// }
//
// List<ChartSeriesData> data = new ArrayList<ChartSeriesData>();
// for (TbProcess item : aggregateList) {
// String pointName = null; // 单个点（数据值）的名称
// pointName = TbProcessApprovalstate.getName(item.getApprovalstate());
//
// if (pointName == null) {
// continue;
// }
//
// ChartSeriesData set = new ChartSeriesData(pointName, item.getAggregation());
// data.add(set);
// log.debug("data");
// }
// series.setData(data); // 图表数据集合（一个系列的数据值）
//
// rslt.add(series); // 增加一个系列
// log.info("rslt={}", rslt);
// return rslt;
// }
// // -------------------- Dashboard数据统计接口实现 --------------------
//
// }
