package cn.mingzhu.iot.app.common.iot.deviceJob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.BaseService;
import cn.mingzhu.iot.app.bas.entity.DeviceJob;
import cn.mingzhu.iot.app.bas.exception.ApiEntityNotFoundException;
import cn.mingzhu.iot.app.bas.exception.ApiIllegalArgumentException;
import cn.mingzhu.iot.app.common.iot.deviceQuartz.DeviceQuartzService;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class DeviceJobService extends BaseService<DeviceJob> {

	/**
	 * 根据ID获取持久类实例
	 * 
	 * @param id
	 * @return
	 */
	public DeviceJob findById(Integer id) {
		if (id == null || id <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		DeviceJob srch = new DeviceJob();
		srch.setId(id);
		DeviceJob found = super.selectByPrimaryKey(srch);
		if (found == null) {
			return null;
		}
		return found;
	}

	/**
	 * 新建
	 * 
	 * 插入数据，最后更新编号
	 * 
	 * @param record
	 * @return
	 */
	public int insert(DeviceJob record) {
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
	public int insertWork(DeviceWork record) {
		if (record == null) {
			throw new ApiIllegalArgumentException("API参数错误: name");
		}

		record.setId(0).setCron(getCron(record.getSeq(), record.getTime()))
				.setRemark(record.getSeq() + record.getTime());

		int rslt = 0;
		rslt = super.insert(record);
		if (rslt != 1) {
			throw new ApiEntityNotFoundException("API参数错误");
		}

		return rslt;
	}

	/***
	 * 功能描述：日期转换cron表达式
	 * 
	 * @param date
	 * @return
	 */
	private String formatDateByPattern(String time) {
		String[] times = time.split(":");
		if (times[1].length() == 1) {
			times[1] = "0" + times[1];
		}
		
		String formatTimeStr = "00 " + times[1] + " " + times[0] + " * * ? *";
		return formatTimeStr;
	}

	private String getCron(String seq, String time) {
		if (seq != null || time != null) {
			if ("[每天]".equals(seq)) {
				String cron = formatDateByPattern(time);
				return cron;
			}
			return "";
		} else {
			return "";
		}
	}

	/**
	 * 更新
	 * 
	 * 
	 * 
	 * @param record
	 * @return
	 */
	public DeviceJob update(DeviceJob record) {
		if (record.getId() == null || record.getId() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}
		DeviceJob srch = new DeviceJob();
		srch.setId(record.getId());
		DeviceJob found = super.selectByPrimaryKey(srch);

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
	 * 根据ID获取行信息
	 * 
	 * @param id
	 * @return
	 */
	public List<DeviceJob> listRowsAll() {
		List<DeviceJob> source = super.selectAll();

		return source;
	}

	/**
	 * 获取分页列表
	 * 
	 * @param example
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageInfo<DeviceJob> page(Example example, Integer pageNumber, Integer pageSize) {

		// Criteria criteria = example.createCriteria();
		// criteria.andEqualTo("sid", siteId);
		// example.and(criteria);

		PageInfo<DeviceJob> source = super.selectByExampleList(example, pageNumber, pageSize);

		List<DeviceJob> items = new ArrayList<DeviceJob>();
		for (int i = 0; i < source.getList().size(); i++) {
			items.add(source.getList().get(i));
		}

		PageInfo<DeviceJob> rslt = new PageInfo<DeviceJob>();
		BeanUtils.copyProperties(source, rslt);
		rslt.setList(items);

		return rslt;
	}

	@Autowired
	DeviceQuartzService deviceQuartzService;

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

		List<DeviceJob> records = new ArrayList<DeviceJob>();

		boolean rslt = false;
		for (int i = 0; i < ids.size(); i++) {
			DeviceJob record = new DeviceJob();
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
	 * @throws SchedulerException
	 */
	private void preProcessIds(List<Integer> ids) throws SchedulerException {
		Example srchExample = new Example(DeviceJob.class);
		Criteria srchCriteria = srchExample.createCriteria();
		srchCriteria.andIn("id", ids);

		List<DeviceJob> founds = super.selectByExample(srchExample);

		if (founds == null || founds.size() <= 0) {
			throw new ApiIllegalArgumentException("API参数错误");
		}

		for (int i = 0; i < founds.size(); i++) {
			deviceQuartzService.deleteByJobIdId(founds.get(i).getId());
		}

	}
}
