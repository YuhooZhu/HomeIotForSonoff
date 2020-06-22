package cn.mingzhu.iot.app.common.iot.room.detail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.BaseService;
import cn.mingzhu.iot.app.bas.entity.IotDevice;
import cn.mingzhu.iot.app.bas.entity.Room;
import cn.mingzhu.iot.app.bas.entity.RoomDetail;
import cn.mingzhu.iot.app.bas.exception.ApiEntityNotFoundException;
import cn.mingzhu.iot.app.bas.exception.ApiIllegalArgumentException;
import cn.mingzhu.iot.app.common.iot.device.IotDeviceService;
import cn.mingzhu.iot.app.common.iot.room.RoomService;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class RoomDetailService extends BaseService<RoomDetail> {

	@Autowired
	private IotDeviceService iotDeviceService;

	@Autowired
	private RoomService roomService;
	
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
	private RoomDetailRow toRow(RoomDetail source) {
		log.info("source={}", source);
		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		RoomDetailRow rslt = new RoomDetailRow();
		BeanUtils.copyProperties(source, rslt);

		if (source.getDeviceId() != null) {

			IotDevice iotDevice = iotDeviceService.findById(source.getDeviceId());
			if (iotDevice != null) {
				rslt.setDevice(iotDevice);
			}
		}

		log.info("rslt={}", rslt);
		return rslt;
	}

	/**
	 * 
	 * /** 根据ID获取持久类实例
	 * 
	 * @param id
	 * @return
	 */
	public RoomDetail findById(Integer id) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		RoomDetail srch = new RoomDetail();
		srch.setId(id);
		RoomDetail found = super.selectByPrimaryKey(srch);
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
	public RoomDetailRow findRowById(Integer id) {
		RoomDetail found = findById(id);
		return toRow(found);
	}

	/**
	 * 
	 * /** 根据ID获取持久类实例
	 * 
	 * @param id
	 * @return
	 */
	public List<RoomDetail> ListByPid(Integer pid) {
		if (pid == null || pid <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		Example srchExample = new Example(RoomDetail.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andEqualTo("pid", pid);

		List<RoomDetail> founds = super.selectByExample(srchExample);

		if (founds == null) {
			return null;
		}
		return founds;
	}

	/**
	 * 
	 * /** 根据ID获取持久类实例
	 * 
	 * @param id
	 * @return
	 */
	public List<RoomDetailRow> ListRowByPid(Integer pid) {
		if (pid == null || pid <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		Example srchExample = new Example(RoomDetail.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andEqualTo("pid", pid);

		List<RoomDetail> founds = super.selectByExample(srchExample);

		if (founds == null) {
			return null;
		}

		List<RoomDetailRow> items = new ArrayList<RoomDetailRow>();
		for (int i = 0; i < founds.size(); i++) {
			items.add(toRow(founds.get(i)));
		}

		return items;
	}

	/**
	 * 新建
	 * 
	 * 插入数据，最后更新编号
	 * 
	 * @param record
	 * @return
	 */
	public int insert(RoomDetail record) {
		if (record == null) {
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
	 * 新建
	 * 
	 * 插入数据，最后更新编号
	 * 
	 * @param record
	 * @return
	 */
	public int insertList(List<Integer> ids,Integer roomId) {
		if (ids == null || ids.size() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误: ids");
		}

		if (roomId == null || roomId <= 0) {
			throw new ApiIllegalArgumentException("API参数错误: roomId");
		}
		
		Set<Integer> addids = preAddIds(ids, roomId);
		
		int rslt = 0;

		if (addids.size() <= 0) {
			rslt = 1;
			return rslt;
		}
		for (Integer id : addids) {
			if (id != null) {
				RoomDetail roomDetail = new RoomDetail();
				roomDetail.setId(0).setPid(roomId).setDeviceId(id);
				rslt = super.insert(roomDetail);
			}
		}
		
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
	public RoomDetail update(RoomDetail record) {
		if (record == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}
		if (record.getId() == null || record.getId() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		RoomDetail srch = new RoomDetail();
		srch.setId(record.getId());
		RoomDetail found = super.selectByPrimaryKey(srch);

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
	public PageInfo<RoomDetailRow> page(Example example, Integer pageNumber, Integer pageSize) {

		// Criteria criteria = example.createCriteria();
		// criteria.andEqualTo("sid", siteId);
		// example.and(criteria);

		PageInfo<RoomDetail> source = super.selectByExampleList(example, pageNumber, pageSize);

		List<RoomDetailRow> items = new ArrayList<RoomDetailRow>();
		for (int i = 0; i < source.getList().size(); i++) {
			items.add(toRow(source.getList().get(i)));
		}

		PageInfo<RoomDetailRow> rslt = new PageInfo<RoomDetailRow>();
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
	public boolean deleteByIds(List<Integer> ids) {
		boolean rslt = false;

		log.debug("delete ids={}", ids);

		if (ids == null || ids.size() == 0) {
			rslt = true;
			return rslt;
		}
		preProcessIds(ids);

		List<RoomDetail> records = new ArrayList<RoomDetail>();

		for (int i = 0; i < ids.size(); i++) {
			RoomDetail record = new RoomDetail();
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
	 * 增加前预处理
	 * 
	 * @param siteId
	 * @param ids
	 * @return 
	 */
	private Set<Integer> preAddIds(List<Integer> ids,Integer roomId) {
		Room room = new Room();
		room = roomService.findById(roomId);
		
		if (room == null) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		
		Set<Integer> addSets = new HashSet<Integer>();

		Example srchExample = new Example(RoomDetail.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andEqualTo("pid", room.getId());

		List<RoomDetail> founds = super.selectByExample(srchExample);
		
		List<IotDevice> addDevices = new ArrayList<IotDevice>();

		for (int i = 0; i < ids.size(); i++) {
			IotDevice record = new IotDevice();
			record = iotDeviceService.findById(ids.get(i));
			addDevices.add(record);
		}
		
		if (founds != null) {
			List<RoomDetailRow> items = new ArrayList<RoomDetailRow>();

			for (int i = 0; i < founds.size(); i++) {
				items.add(toRow(founds.get(i)));
			}
			
			List<IotDevice> oldDevices = items.stream().map(x -> x.getDevice()).collect(Collectors.toList());
			
			if (oldDevices != null && addDevices != null) {
				
				if (addDevices != null && addDevices.size() > 0) {
					for (IotDevice item : addDevices) {
						if (item.getId() != null) {
							addSets.add(item.getId());
						}
					}
					for (IotDevice item : oldDevices) {
						if (item.getId() != null) {
							addSets.remove(item.getId());
						}
					}
				}
			}
			return addSets;
		}
		
		for (IotDevice item : addDevices) {
			if (item.getId() != null) {
				addSets.add(item.getId());
			}
		}
		return addSets;
	}

	/**
	 * 删除前预处理
	 * 
	 * @param siteId
	 * @param ids
	 */
	private void preProcessIds(List<Integer> ids) {
		Example srchExample = new Example(RoomDetail.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andIn("id", ids);

		List<RoomDetail> founds = super.selectByExample(srchExample);

	}
}
