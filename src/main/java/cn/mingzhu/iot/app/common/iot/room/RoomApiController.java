package cn.mingzhu.iot.app.common.iot.room;

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
import cn.mingzhu.iot.app.bas.entity.Room;
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
public class RoomApiController {

	@Autowired
	private RoomService service;

	@PostMapping("/room")
	@ApiOperation(value = "新增房间", notes = "新增房间", produces = "application/json")
	@ApiImplicitParam(name = "record", value = "Room", dataType = "Room")
	public ResponseData<Room> add(HttpServletRequest request, @RequestBody Room record) {

		log.info("/room");
		log.info("record={}", record);
		int rslt = 0;

		rslt = service.insert(record);

		if (rslt != 1) {
			throw new AppEntityNotFoundException("API参数错误");
		}

		return new ResponseData<Room>(DmCode.CREATED, record);
	}

	@GetMapping("/room")
	@ApiOperation(value = "获取分页", notes = "获取分页详细信息", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pn", value = "页数", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "ps", value = "每页记录数", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "sf", value = "排序字段: id, name(房间名称)", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "st", value = "排序方式：asc, desc", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "search", value = "模糊查询字段，只针对name(房间名称), code(编号),memo(备注)字段", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "filter", value = "过滤条件", dataType = "HashMap<String, Object>", paramType = "query") })
	public ResponseData<PageInfo<RoomRow>> pageInfo(HttpServletRequest request,
			@RequestParam(value = "pn", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "ps", required = false, defaultValue = Pager.DEFAULT_PAER_SIZE) Integer pageSize,
			@RequestParam(value = "sf", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "st", required = false, defaultValue = "desc") String sortType,
			@RequestParam(value = "search", required = false) String search) {

		log.info("/room");
		log.info("pn={}, ps={}, sf={}, st={}, search={}", pageNumber, pageSize, sortField, sortType, search);

		Example example = new Example(Room.class);
		Criteria criteria = example.createCriteria();
		if (search != null && !"".equals(search)) {
			criteria.orLike("code", "%" + search + "%");
			criteria.orLike("name", "%" + search + "%");
		}

		// Criteria criteria4 = example.createCriteria();
		// criteria4.andEqualTo("removed", RemovedConstants.REMOVED_NO);
		// example.and(criteria4);

		if (TrcConstants.ASC.equalsIgnoreCase(sortType)) {
			example.orderBy(sortField).asc();
		} else {
			example.orderBy(sortField).desc();
		}

		PageInfo<RoomRow> pageInfo = service.page(example, pageNumber, pageSize);

		return new ResponseData<PageInfo<RoomRow>>(DmCode.OK, pageInfo);
	}

	@GetMapping("/room/{id}")
	@ApiOperation(value = "获取房间信息", notes = "获取房间详细信息，含房间明细详细信息", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "ID", dataType = "Integer", paramType = "path")
	public ResponseData<RoomEx> get(HttpServletRequest request, @PathVariable("id") Integer id) {

		log.info("/room/{id}");
		log.info("id={}", id);

		RoomEx record = service.findExById(id);

		return new ResponseData<RoomEx>(DmCode.OK, record);
	}

	@PutMapping("/room/{id}")
	@ApiOperation(value = "修改房间", notes = "修改单条房间记录", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "房间ID", dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "record", value = "Integer", dataType = "Room") })
	public ResponseData<Room> update(HttpServletRequest request, @PathVariable("id") Integer id,
			@RequestBody Room record) {

		log.info("/room/{id}");
		log.info("id={}, record={}", id, record);

		Room rslt = service.update(record);

		return new ResponseData<Room>(DmCode.OK, rslt);
	}

	@DeleteMapping("/room")
	@ApiOperation(value = "批量删除房间清单", notes = "批量删除房间清单", produces = "application/json")
	@ApiImplicitParam(name = "ids", value = "房间ID列表", allowMultiple = true, dataType = "Integer", paramType = "body")
	public ResponseData<List<Integer>> deleteByIds(@RequestBody List<Integer> ids) {

		log.info("ids={}", ids);

		boolean rslt = service.deleteByIds(1, ids);

		if (rslt != true) {
			throw new AppEntityNotFoundException("删除失败！");
		}
		return new ResponseData<List<Integer>>(DmCode.NO_CONTENT, ids);
	}

	@ApiOperation(value = "开", notes = "修改房间状态", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "房间ID", dataType = "Integer", paramType = "path")
	@PutMapping("/room/{id}/state/on")
	public ResponseData<Room> updateStateToConfirmed(@PathVariable("id") Integer id) {

		log.info("id={}", id);

		Room rslt = service.updateStateToOn(id);

		return new ResponseData<Room>(DmCode.OK, rslt);
	}

	@ApiOperation(value = "关", notes = "修改房间状态", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "房间ID", dataType = "Integer", paramType = "path")
	@PutMapping("/room/{id}/state/off")
	public ResponseData<Room> updateStateToOff(@PathVariable("id") Integer id) {

		log.info("id={}", id);

		Room rslt = service.updateStateToOff(id);

		return new ResponseData<Room>(DmCode.OK, rslt);
	}
}
