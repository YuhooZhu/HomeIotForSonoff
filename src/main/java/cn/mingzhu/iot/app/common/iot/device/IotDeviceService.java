package cn.mingzhu.iot.app.common.iot.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.BaseService;
import cn.mingzhu.iot.app.bas.Response;
import cn.mingzhu.iot.app.bas.entity.DeviceQuartz;
import cn.mingzhu.iot.app.bas.entity.IotDevice;
import cn.mingzhu.iot.app.bas.exception.ApiDBUpdateException;
import cn.mingzhu.iot.app.bas.exception.ApiEntityNotFoundException;
import cn.mingzhu.iot.app.bas.exception.ApiIllegalArgumentException;
import cn.mingzhu.iot.app.common.iot.deviceQuartz.DeviceQuartzRow;
import cn.mingzhu.iot.app.config.HttpClient;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class IotDeviceService extends BaseService<IotDevice> {

	// @Autowired
	// private UserService userSvc;

	@Autowired
	private HttpClient httpClient;

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
	private IotDeviceRow toRow(IotDevice source) {
		log.info("source={}", source);
		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		IotDeviceRow rslt = new IotDeviceRow();
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
	private IotDeviceEx toEx(IotDevice source) {
		log.info("source={}", source);

		IotDeviceRow row = toRow(source);

		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		IotDeviceEx rslt = new IotDeviceEx();
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
	public IotDevice findById(Integer id) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		IotDevice srch = new IotDevice();
		srch.setId(id);
		IotDevice found = super.selectByPrimaryKey(srch);
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
	public IotDeviceRow findRowById(Integer id) {
		IotDevice found = findById(id);
		return toRow(found);
	}

	/**
	 * 根据ID获取详细信息
	 * 
	 * @param id
	 * @return
	 */
	public IotDeviceEx findExById(Integer id) {
		IotDevice found = findById(id);
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
	public int insert(IotDeviceEx record) {
		if (record == null || record.getName() == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}

		record.setId(0).setState(IotDeviceState.OFF.getName());

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
	public IotDeviceEx update(IotDeviceEx record) {
		if (record.getId() == null || record.getId() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		if (record == null || record.getName() == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}
		IotDevice srch = new IotDevice();
		srch.setId(record.getId());
		IotDevice found = super.selectByPrimaryKey(srch);

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
	 * 根据ID获取行信息
	 * 
	 * @param id
	 * @return
	 */
	public List<IotDeviceRow> listRowsAll() {
		List<IotDevice> source = super.selectAll();

		List<IotDeviceRow> items = new ArrayList<IotDeviceRow>();
		for (int i = 0; i < source.size(); i++) {
			items.add(toRow(source.get(i)));
		}

		return items;
	}

	/**
	 * 获取分页列表
	 * 
	 * @param example
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageInfo<IotDeviceRow> page(Integer siteId, Example example, Integer pageNumber, Integer pageSize) {
		if (siteId == null || siteId <= 0) {
			throw new ApiIllegalArgumentException("API参数错误: siteId");
		}

		// Criteria criteria = example.createCriteria();
		// criteria.andEqualTo("sid", siteId);
		// example.and(criteria);

		PageInfo<IotDevice> source = super.selectByExampleList(example, pageNumber, pageSize);

		List<IotDeviceRow> items = new ArrayList<IotDeviceRow>();
		for (int i = 0; i < source.getList().size(); i++) {
			items.add(toRow(source.getList().get(i)));
		}

		PageInfo<IotDeviceRow> rslt = new PageInfo<IotDeviceRow>();
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

		List<IotDevice> records = new ArrayList<IotDevice>();

		boolean rslt = false;
		for (int i = 0; i < ids.size(); i++) {
			IotDevice record = new IotDevice();
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
		Example srchExample = new Example(IotDevice.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andIn("id", ids);

		List<IotDevice> founds = super.selectByExample(srchExample);

	}

	/**
	 * 改变设备的状态 0-开 1-关 注意：私有方法无需加回滚（@Transactional），在公有方法中加
	 * 
	 * @param siteId
	 * @param id
	 * @param state
	 * @return
	 */
	private int updateState(Integer id, IotDeviceState state) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		IotDevice srch = new IotDevice();
		srch.setId(id);
		IotDevice found = super.selectByPrimaryKey(srch);
		if (found == null) {
			throw new ApiIllegalArgumentException("设备不存在");
		}
		int rslt = 0;

		Response response = new Response();

		if (state == IotDeviceState.ON) {
			response = switchOn(found);
		} else if (state == IotDeviceState.OFF) {
			response = switchOff(found);
		}

		log.debug("rsp={}", response);

		if (response == null || response.getError() != 0) {
			throw new ApiIllegalArgumentException("HTTP返回值错误,不为0或不存在");
		}

		IotDevice update = new IotDevice();
		update.setId(id).setState(state.getName());
		try {
			rslt = super.updateByPrimaryKeySelective(update);
		} catch (Exception ex) {
			throw new ApiDBUpdateException("设备状态修改错误", ex);
		}
		if (rslt != 1) {
			throw new ApiDBUpdateException("修改状态失败");
		}

		return rslt;
	}

	/*
	 * 延时开灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public IotDevice delayO(Integer id) {
		updateState(id, IotDeviceState.ON);
		return findExById(id);
	}

	/*
	 * 延时关灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public IotDevice delayOff(Integer id) {
		updateState(id, IotDeviceState.ON);
		return findExById(id);
	}

	/*
	 * 单个开灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public IotDevice updateStateToOn(Integer id) {
		updateState(id, IotDeviceState.ON);
		return findExById(id);
	}

	/*
	 * 单个关灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public IotDevice updateStateToOff(Integer id) {
		updateState(id, IotDeviceState.OFF);
		return findExById(id);
	}

	/*
	 * 多个开灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<IotDevice> updateStatesToOn(List<Integer> ids) {
		for (int i = 0; i < ids.size(); i++) {
			updateState(ids.get(i), IotDeviceState.ON);
		}

		List<IotDevice> rslt = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			rslt.add(findExById(ids.get(i)));
		}
		return rslt;
	}

	/*
	 * 多个关灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<IotDevice> updateStatesToOff(List<Integer> ids) {
		for (int i = 0; i < ids.size(); i++) {
			updateState(ids.get(i), IotDeviceState.OFF);
		}

		List<IotDevice> rslt = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			rslt.add(findExById(ids.get(i)));
		}
		return rslt;
	}

	public Response switchOn(IotDevice iotDevice) {
		HashMap<String, String> map = new HashMap<>();
		map.put("switch", "on");

		Response response = new Response();
		try {
			response = httpClient.post("switch", map, iotDevice);
		} catch (Exception e) {
			throw new ApiIllegalArgumentException("http请求错误", e);
		}

		return response;
	}

	public Response switchOff(IotDevice iotDevice) {
		HashMap<String, String> map = new HashMap<>();
		map.put("switch", "off");

		Response response = new Response();
		try {
			response = httpClient.post("switch", map, iotDevice);
		} catch (Exception e) {
			throw new ApiIllegalArgumentException("http请求错误", e);
		}

		return response;
	}

}
