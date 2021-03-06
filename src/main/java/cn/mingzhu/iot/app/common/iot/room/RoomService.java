package cn.mingzhu.iot.app.common.iot.room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.BaseService;
import cn.mingzhu.iot.app.bas.entity.IotDevice;
import cn.mingzhu.iot.app.bas.entity.Room;
import cn.mingzhu.iot.app.bas.exception.ApiDBUpdateException;
import cn.mingzhu.iot.app.bas.exception.ApiEntityNotFoundException;
import cn.mingzhu.iot.app.bas.exception.ApiIllegalArgumentException;
import cn.mingzhu.iot.app.common.iot.device.IotDeviceService;
import cn.mingzhu.iot.app.common.iot.room.detail.RoomDetailRow;
import cn.mingzhu.iot.app.common.iot.room.detail.RoomDetailService;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class RoomService extends BaseService<Room> {

	@Autowired
	private RoomDetailService roomDetailService;

	@Autowired
	private IotDeviceService iotDeviceService;
	
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
	private RoomRow toRow(Room source) {
		log.info("source={}", source);
		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		RoomRow rslt = new RoomRow();
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
	private RoomEx toEx(Room source) {
		log.info("source={}", source);

		RoomRow row = toRow(source);

		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		RoomEx rslt = new RoomEx();
		BeanUtils.copyProperties(row, rslt);

		List<RoomDetailRow> roomDetails = roomDetailService.ListRowByPid(source.getId());
		
		if (roomDetails != null) {
			rslt.setItems(roomDetails);
		}

		log.info("rslt={}", rslt);
		return rslt;
	}

	/**
	 * 根据ID获取持久类实例
	 * 
	 * @param id
	 * @return
	 */
	public Room findById(Integer id) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		Room srch = new Room();
		srch.setId(id);
		Room found = super.selectByPrimaryKey(srch);
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
	public RoomRow findRowById(Integer id) {
		Room found = findById(id);
		return toRow(found);
	}

	/**
	 * 根据ID获取详细信息
	 * 
	 * @param id
	 * @return
	 */
	public RoomEx findExById(Integer id) {
		Room found = findById(id);
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
	public int insert(RoomEx record) {
		if (record == null || record.getName() == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}

		record.setId(0).setState(RoomState.OFF.getName());

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
	public Room update(Room record) {
		if (record.getId() == null || record.getId() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		if (record == null || record.getName() == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}
		Room srch = new Room();
		srch.setId(record.getId());
		Room found = super.selectByPrimaryKey(srch);

		if (found == null) {
			throw new ApiEntityNotFoundException("工序不存在");
		}

		int rslt = super.updateByPrimaryKeySelective(record);

		if (rslt != 1) {
			throw new ApiEntityNotFoundException("API参数错误");
		}

		return findById(record.getId());
	}

	/**
	 * 获取分页列表
	 * 
	 * @param example
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageInfo<RoomRow> page(Example example, Integer pageNumber, Integer pageSize) {

		// Criteria criteria = example.createCriteria();
		// criteria.andEqualTo("sid", siteId);
		// example.and(criteria);

		PageInfo<Room> source = super.selectByExampleList(example, pageNumber, pageSize);

		List<RoomRow> items = new ArrayList<RoomRow>();
		for (int i = 0; i < source.getList().size(); i++) {
			items.add(toRow(source.getList().get(i)));
		}

		PageInfo<RoomRow> rslt = new PageInfo<RoomRow>();
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

		List<Room> records = new ArrayList<Room>();

		boolean rslt = false;
		for (int i = 0; i < ids.size(); i++) {
			Room record = new Room();
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
		Example srchExample = new Example(Room.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andIn("id", ids);

		List<Room> founds = super.selectByExample(srchExample);

	}

	/**
	 * 改变设备的状态 0-开 1-关 注意：私有方法无需加回滚（@Transactional），在公有方法中加
	 * 
	 * @param siteId
	 * @param id
	 * @param state
	 * @return
	 */
	private int updateState(Integer id, RoomState state) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		RoomEx found = findExById(id);
		if (found == null) {
			throw new ApiIllegalArgumentException("设备不存在");
		}
		int rslt = 0;

		if (found.getItems() == null || found.getItems().size() <= 0) {
			return 1;
		}
		
		List<Integer> deviceIds = found.getItems().stream().map(x -> x.getDevice().getId()).collect(Collectors.toList());
		
		List<IotDevice> devices = new ArrayList<IotDevice>();
		if (state == RoomState.ON) {
			devices  = iotDeviceService.updateStatesToOn(deviceIds);
		} else if (state == RoomState.OFF) {
			devices  = iotDeviceService.updateStatesToOff(deviceIds);
		}

		log.debug("rsp={}", devices);

		if (devices == null) {
			throw new ApiIllegalArgumentException("房间操作失败");
		}

		Room update = new Room();
		update.setId(id).setState(state.getName());
		try {
			rslt = super.updateByPrimaryKeySelective(update);
		} catch (Exception ex) {
			throw new ApiDBUpdateException("房间状态修改错误", ex);
		}
		if (rslt != 1) {
			throw new ApiDBUpdateException("修改状态失败");
		}

		return rslt;
	}

	/*
	 * 单个开灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public Room updateStateToOn(Integer id) {
		updateState(id, RoomState.ON);
		return findExById(id);
	}

	/*
	 * 单个关灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public Room updateStateToOff(Integer id) {
		updateState(id, RoomState.OFF);
		return findExById(id);
	}

	/*
	 * 多个开灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<Room> updateStatesToOn(List<Integer> ids) {
		for (int i = 0; i < ids.size(); i++) {
			updateState(ids.get(i), RoomState.ON);
		}

		List<Room> rslt = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			rslt.add(findExById(ids.get(i)));
		}
		return rslt;
	}

	/*
	 * 多个关灯
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<Room> updateStatesToOff(List<Integer> ids) {
		for (int i = 0; i < ids.size(); i++) {
			updateState(ids.get(i), RoomState.OFF);
		}

		List<Room> rslt = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			rslt.add(findExById(ids.get(i)));
		}
		return rslt;
	}
}
