package cn.mingzhu.iot.app.common.iot.deviceQuartz;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.quartz.SchedulerException;
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
import cn.mingzhu.iot.app.bas.entity.DeviceQuartz;
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
public class DeviceQuartzApiController {

	@Autowired
	private DeviceQuartzService service;

	@PostMapping("/deviceQuartz")
	@ApiOperation(value = "新增计划", notes = "新增计划", produces = "application/json")
	@ApiImplicitParam(name = "record", value = "DeviceQuartz", dataType = "DeviceQuartz")
	public ResponseData<DeviceQuartz> add(HttpServletRequest request, @RequestBody DeviceQuartz record)
			throws SchedulerException {

		log.info("/deviceQuartz");
		log.info("record={}", record);
		int rslt = 0;

		rslt = service.insert(record);

		if (rslt != 1) {
			throw new AppEntityNotFoundException("API参数错误");
		}

		return new ResponseData<DeviceQuartz>(DmCode.CREATED, record);
	}

	@PutMapping("/deviceQuartz/addlist/{id}")
	@ApiOperation(value = "新增计划", notes = "新增计划记录", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "计划ID", dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "ids", value = "ArrayList<Integer>", dataType = "ArrayList<Integer>") })
	public ResponseData<Integer> addlist(HttpServletRequest request, @PathVariable("id") Integer id,
			@RequestBody ArrayList<Integer> ids) {

		log.info("/deviceQuartz/addlist/{id}");
		log.info("id={}, ids={}", id, ids);
		int rslt = 0;

		rslt = service.insertList(ids, id);

		return new ResponseData<Integer>(DmCode.OK, rslt);
	}
	
	@GetMapping("/deviceQuartz")
	@ApiOperation(value = "获取分页", notes = "获取分页详细信息", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pn", value = "页数", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "ps", value = "每页记录数", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "sf", value = "排序字段: id, name(设备名称)", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "st", value = "排序方式：asc, desc", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "search", value = "模糊查询字段，只针对name(设备名称), code(编号),memo(备注)字段", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "filter", value = "过滤条件", dataType = "HashMap<String, Object>", paramType = "query") })
	public ResponseData<PageInfo<DeviceQuartzRow>> pageInfo(HttpServletRequest request,
			@RequestParam(value = "pn", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "ps", required = false, defaultValue = Pager.DEFAULT_PAER_SIZE) Integer pageSize,
			@RequestParam(value = "sf", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "st", required = false, defaultValue = "desc") String sortType,
			@RequestParam(value = "search", required = false) String search) {

		log.info("/deviceQuartz");
		log.info("pn={}, ps={}, sf={}, st={}, search={}", pageNumber, pageSize, sortField, sortType, search);

		Example example = new Example(DeviceQuartz.class);
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

		PageInfo<DeviceQuartzRow> pageInfo = service.page(example, pageNumber, pageSize);

		return new ResponseData<PageInfo<DeviceQuartzRow>>(DmCode.OK, pageInfo);
	}

	@GetMapping("/deviceQuartz/{id}")
	@ApiOperation(value = "获取计划信息", notes = "获取计划详细信息，含设备明细详细信息", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "ID", dataType = "Integer", paramType = "path")
	public ResponseData<DeviceQuartzEx> get(HttpServletRequest request, @PathVariable("id") Integer id) {

		log.info("/deviceQuartz/{id}");
		log.info("id={}", id);

		DeviceQuartzEx record = service.findExById(id);

		return new ResponseData<DeviceQuartzEx>(DmCode.OK, record);
	}

	@GetMapping("/deviceQuartz/byDeviceId/{deviceId}")
	@ApiOperation(value = "根据设备Id获取计划信息", notes = "获取计划详细信息，含设备明细详细信息", produces = "application/json")
	@ApiImplicitParam(name = "deviceId", value = "deviceId", dataType = "Integer", paramType = "path")
	public ResponseData<List<DeviceQuartzRow>> getByDeviceId(HttpServletRequest request,
			@PathVariable("deviceId") Integer deviceId) {

		log.info("/deviceQuartz/byDeviceId/{deviceId}");
		log.info("deviceId={}", deviceId);

		List<DeviceQuartzRow> record = service.listRowsByDeviceId(deviceId);

		return new ResponseData<List<DeviceQuartzRow>>(DmCode.OK, record);
	}

	@PutMapping("/deviceQuartz/{id}")
	@ApiOperation(value = "修改计划", notes = "修改单条计划记录", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "计划ID", dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "record", value = "Integer", dataType = "DeviceQuartz") })
	public ResponseData<DeviceQuartz> update(HttpServletRequest request, @PathVariable("id") Integer id,
			@RequestBody DeviceQuartz record) {

		log.info("/deviceQuartz/{id}");
		log.info("id={}, record={}", id, record);

		DeviceQuartz rslt = service.update(record);

		return new ResponseData<DeviceQuartz>(DmCode.OK, rslt);
	}

	@DeleteMapping("/deviceQuartz")
	@ApiOperation(value = "批量删除设备清单", notes = "批量删除设备清单", produces = "application/json")
	@ApiImplicitParam(name = "ids", value = "设备ID列表", allowMultiple = true, dataType = "Integer", paramType = "body")
	public ResponseData<List<Integer>> deleteByIds(@RequestBody List<Integer> ids) throws SchedulerException {

		log.info("ids={}", ids);

		boolean rslt = service.deleteByIds(ids);

		if (rslt != true) {
			throw new AppEntityNotFoundException("删除失败！");
		}
		return new ResponseData<List<Integer>>(DmCode.NO_CONTENT, ids);
	}

	@ApiOperation(value = "开", notes = "开启任务", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "设备ID", dataType = "Integer", paramType = "path")
	@PutMapping("/deviceQuartz/{id}/state/on")
	public ResponseData<DeviceQuartz> updateStateToConfirmed(@PathVariable("id") Integer id) throws SchedulerException {

		log.info("id={}", id);

		DeviceQuartz rslt = service.updateStateToOn(id);

		return new ResponseData<DeviceQuartz>(DmCode.OK, rslt);
	}

	@ApiOperation(value = "关", notes = "关闭任务", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "设备ID", dataType = "Integer", paramType = "path")
	@PutMapping("/deviceQuartz/{id}/state/off")
	public ResponseData<DeviceQuartz> updateStateToOff(@PathVariable("id") Integer id) throws SchedulerException {

		log.info("id={}", id);

		DeviceQuartz rslt = service.updateStateToOff(id);

		return new ResponseData<DeviceQuartz>(DmCode.OK, rslt);
	}
}
