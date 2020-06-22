package cn.mingzhu.iot.app.common.iot.deviceQuartz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.BaseService;
import cn.mingzhu.iot.app.bas.entity.DeviceJob;
import cn.mingzhu.iot.app.bas.entity.DeviceQuartz;
import cn.mingzhu.iot.app.bas.entity.IotDevice;
import cn.mingzhu.iot.app.bas.exception.ApiDBUpdateException;
import cn.mingzhu.iot.app.bas.exception.ApiEntityNotFoundException;
import cn.mingzhu.iot.app.bas.exception.ApiIllegalArgumentException;
import cn.mingzhu.iot.app.common.iot.device.IotDeviceService;
import cn.mingzhu.iot.app.common.iot.deviceJob.DeviceJobService;
import cn.mingzhu.iot.app.util.ScheduledJob.QuartzSchedulerNew;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class DeviceQuartzService extends BaseService<DeviceQuartz> {

	@Autowired
	private DeviceJobService deviceJobService;

	@Autowired
	private IotDeviceService iotDeviceService;
	
	@Autowired
	private QuartzSchedulerNew quartzSchedulerNew;

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
	private DeviceQuartzRow toRow(DeviceQuartz source) {
		log.info("source={}", source);
		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		DeviceQuartzRow rslt = new DeviceQuartzRow();
		BeanUtils.copyProperties(source, rslt);

		if (source.getJobId() != null && source.getJobId() > 0) {

			DeviceJob deviceJob = deviceJobService.findById(source.getJobId());
			if (deviceJob != null) {
				rslt.setDeviceJob(deviceJob);
			}
		}

		log.info("rslt={}", rslt);
		return rslt;
	}

	/**
	 * 子表持久类对象转换为视图扩展类对象
	 * 
	 * @param source
	 * @return
	 */
	private DeviceQuartzEx toEx(DeviceQuartz source) {
		log.info("source={}", source);

		DeviceQuartzRow row = toRow(source);

		if (source == null) {
			throw new ApiIllegalArgumentException("API转换参数错误");
		}

		DeviceQuartzEx rslt = new DeviceQuartzEx();
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
	public DeviceQuartz findById(Integer id) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		DeviceQuartz srch = new DeviceQuartz();
		srch.setId(id);
		DeviceQuartz found = super.selectByPrimaryKey(srch);
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
	public DeviceQuartzRow findRowById(Integer id) {
		DeviceQuartz found = findById(id);
		return toRow(found);
	}

	/**
	 * 根据ID获取详细信息
	 * 
	 * @param id
	 * @return
	 */
	public DeviceQuartzEx findExById(Integer id) {
		DeviceQuartz found = findById(id);
		return toEx(found);
	}

	/**
	 * 根据ID获取行信息
	 * 
	 * @param id
	 * @return
	 */
	public List<DeviceQuartzRow> listRowsAll() {
		List<DeviceQuartz> source = super.selectAll();

		List<DeviceQuartzRow> items = new ArrayList<DeviceQuartzRow>();
		for (int i = 0; i < source.size(); i++) {
			items.add(toRow(source.get(i)));
		}

		return items;
	}

	/**
	 * 查询状态为ON的设备
	 * 
	 * @param id
	 * @return
	 */
	public List<DeviceQuartzRow> listRowsOnAll() {
		Example example = new Example(DeviceQuartz.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("state", DeviceQuartzState.ON.toString());

		List<DeviceQuartz> source = super.selectByExample(example);

		List<DeviceQuartzRow> items = new ArrayList<DeviceQuartzRow>();
		for (int i = 0; i < source.size(); i++) {
			items.add(toRow(source.get(i)));
		}

		return items;
	}

	/**
	 * 根据ID获取行信息
	 * 
	 * @param id
	 * @return
	 */
	public List<DeviceQuartzRow> listRowsByDeviceId(Integer deviceId) {
		Example example = new Example(DeviceQuartz.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("deviceId", deviceId);

		List<DeviceQuartz> source = super.selectByExample(example);

		List<DeviceQuartzRow> items = new ArrayList<DeviceQuartzRow>();
		for (int i = 0; i < source.size(); i++) {
			items.add(toRow(source.get(i)));
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
	 * @throws SchedulerException
	 */
	public int insert(DeviceQuartz record) {
		if (record == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}

		record.setId(0).setState(DeviceQuartzState.OFF.getName());

		int rslt = 0;
		rslt = super.insert(record);
		if (rslt != 1) {
			throw new ApiEntityNotFoundException("API参数错误");
		}

		quartzSchedulerNew.addJob(toRow(record));

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
	public int insertList(List<Integer> ids,Integer deviceId) {
		if (ids == null || ids.size() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误: ids");
		}

		if (deviceId == null || deviceId <= 0) {
			throw new ApiIllegalArgumentException("API参数错误: roomId");
		}
		
		Set<Integer> addids = preAddIds(ids, deviceId);
		
		int rslt = 0;

		if (addids.size() <= 0) {
			rslt = 1;
			return rslt;
		}
		for (Integer id : addids) {
			if (id != null) {
				DeviceJob job = new DeviceJob();
				job = deviceJobService.findById(id);
				
				DeviceQuartz deviceQuartz = new DeviceQuartz();
				deviceQuartz.setId(0).setDeviceId(deviceId).setState(DeviceQuartzState.OFF.getName()).setJobId(id).setGroup(deviceId + "#" + id).setRemark(job.getRemark());
				rslt = super.insert(deviceQuartz);
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
	 * @throws SchedulerException
	 */
	public DeviceQuartz update(DeviceQuartz record) {
		if (record.getId() == null || record.getId() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		DeviceQuartz srch = new DeviceQuartz();
		srch.setId(record.getId());
		DeviceQuartz found = super.selectByPrimaryKey(srch);

		if (found == null) {
			throw new ApiEntityNotFoundException("计划不存在");
		}

		int rslt = super.updateByPrimaryKeySelective(record);

		if (rslt != 1) {
			throw new ApiEntityNotFoundException("API参数错误");
		}
		DeviceQuartzRow oldQuartz = toRow(found);

		DeviceQuartzRow newQuartz = toRow(record);
		try {
			quartzSchedulerNew.deleteJob(oldQuartz.getDeviceJob().getName(), oldQuartz.getGroup());
		} catch (SchedulerException e) {
			throw new ApiEntityNotFoundException("API参数错误");
		}

		quartzSchedulerNew.addJob(newQuartz);
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
	public PageInfo<DeviceQuartzRow> page(Example example, Integer pageNumber, Integer pageSize) {

		PageInfo<DeviceQuartz> source = super.selectByExampleList(example, pageNumber, pageSize);

		List<DeviceQuartzRow> items = new ArrayList<DeviceQuartzRow>();
		for (int i = 0; i < source.getList().size(); i++) {
			items.add(toRow(source.getList().get(i)));
		}

		PageInfo<DeviceQuartzRow> rslt = new PageInfo<DeviceQuartzRow>();
		BeanUtils.copyProperties(source, rslt);
		rslt.setList(items);

		return rslt;
	}

	/**
	 * 改变任务的状态 0-开 1-关 注意：私有方法无需加回滚（@Transactional），在公有方法中加
	 * 
	 * @param siteId
	 * @param id
	 * @param state
	 * @return
	 * @throws SchedulerException
	 */
	private int updateState(Integer id, DeviceQuartzState state) throws SchedulerException {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		DeviceQuartz srch = new DeviceQuartz();
		srch.setId(id);
		DeviceQuartz found = super.selectByPrimaryKey(srch);
		if (found == null) {
			throw new ApiIllegalArgumentException("计划不存在");
		}
		int rslt = 0;

		DeviceQuartz update = new DeviceQuartz();
		update.setId(id).setState(state.getName());
		try {
			rslt = super.updateByPrimaryKeySelective(update);
		} catch (Exception ex) {
			throw new ApiDBUpdateException("设备状态修改错误", ex);
		}
		if (rslt != 1) {
			throw new ApiDBUpdateException("修改状态失败");
		}

		DeviceQuartzRow oldQuartz = toRow(found);

		DeviceQuartzRow newQuartz = findRowById(update.getId());

		if (state == DeviceQuartzState.ON) {
			quartzSchedulerNew.addJob(newQuartz);
		} else if (state == DeviceQuartzState.OFF) {
			quartzSchedulerNew.deleteJob(oldQuartz.getDeviceJob().getName(), oldQuartz.getGroup());
		}

		return rslt;
	}

	/*
	 * 开启任务
	 */
	@Transactional(rollbackFor = Exception.class)
	public DeviceQuartz updateStateToOn(Integer id) throws SchedulerException {
		updateState(id, DeviceQuartzState.ON);
		return findExById(id);
	}

	/*
	 * 关闭任务
	 */
	@Transactional(rollbackFor = Exception.class)
	public DeviceQuartz updateStateToOff(Integer id) throws SchedulerException {
		updateState(id, DeviceQuartzState.OFF);
		return findExById(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 * @throws SchedulerException
	 */
	public boolean deleteByIds(List<Integer> ids) throws SchedulerException {

		log.debug("delete ids={}", ids);

		if (ids == null || ids.size() == 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		preProcessIds(ids);

		List<DeviceQuartz> records = new ArrayList<DeviceQuartz>();

		boolean rslt = false;
		for (int i = 0; i < ids.size(); i++) {
			DeviceQuartz record = new DeviceQuartz();
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
	 * 根据jobId删除
	 * 
	 * @param ids
	 * @return
	 * @throws SchedulerException
	 */
	public boolean deleteByJobIdId(Integer JobId) throws SchedulerException {

		log.debug("delete ids={}", JobId);

		if (JobId == null) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		
		Example example = new Example(DeviceQuartz.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("jobId", JobId);

		List<DeviceQuartz> source = super.selectByExample(example);

		if (source == null) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		
		List<Integer> ids = source.stream().map(x -> x.getId()).collect(Collectors.toList());
		preProcessIds(ids);

		List<DeviceQuartz> records = new ArrayList<DeviceQuartz>();

		boolean rslt = false;
		for (int i = 0; i < ids.size(); i++) {
			DeviceQuartz record = new DeviceQuartz();
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
	private Set<Integer> preAddIds(List<Integer> ids,Integer deviceId) {
		IotDevice iotDevice = new IotDevice();
		iotDevice = iotDeviceService.findById(deviceId);
		
		if (iotDevice == null) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		
		Set<Integer> addSets = new HashSet<Integer>();

		Example srchExample = new Example(DeviceQuartz.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andEqualTo("deviceId", iotDevice.getId());

		List<DeviceQuartz> founds = super.selectByExample(srchExample);
		
		List<DeviceJob> addJobs = new ArrayList<DeviceJob>();

		for (int i = 0; i < ids.size(); i++) {
			DeviceJob record = new DeviceJob();
			record = deviceJobService.findById(ids.get(i));
			addJobs.add(record);
		}
		
		if (founds != null) {
			List<DeviceQuartzRow> items = new ArrayList<DeviceQuartzRow>();

			for (int i = 0; i < founds.size(); i++) {
				items.add(toRow(founds.get(i)));
			}
			
			List<DeviceJob> oldDevices = items.stream().map(x -> x.getDeviceJob()).collect(Collectors.toList());
			
			if (oldDevices != null && addJobs != null) {
				
				if (addJobs != null && addJobs.size() > 0) {
					for (DeviceJob item : addJobs) {
						if (item.getId() != null) {
							addSets.add(item.getId());
						}
					}
					for (DeviceJob item : oldDevices) {
						if (item.getId() != null) {
							addSets.remove(item.getId());
						}
					}
				}
			}
			return addSets;
		}
		
		for (DeviceJob item : addJobs) {
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
		Example srchExample = new Example(DeviceQuartz.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andIn("id", ids);

		List<DeviceQuartz> founds = super.selectByExample(srchExample);
		List<DeviceQuartzRow> items = new ArrayList<DeviceQuartzRow>();

		for (int i = 0; i < founds.size(); i++) {
			items.add(toRow(founds.get(i)));
		}

		for (int i = 0; i < items.size(); i++) {
			try {
				quartzSchedulerNew.deleteJob(items.get(i).getDeviceJob().getName(), items.get(i).getGroup());
			} catch (SchedulerException e) {
				throw new ApiEntityNotFoundException("API参数错误");
			}
		}
	}
}
