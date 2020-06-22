package cn.mingzhu.iot.app.bas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.mingzhu.iot.app.bas.mapper.AutoMapper;
import tk.mybatis.mapper.additional.aggregation.AggregateCondition;
import tk.mybatis.mapper.entity.Example;

public class BaseService<T> {

    @Autowired
    protected AutoMapper<T> mapper;

    /**
     * 查询所有
     */
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    /**
     * 根据主键查询
     */
    public T selectByPrimaryKey(T key) {
        return mapper.selectByPrimaryKey(key);
    }

    /**
     * 根据条件实例查询
     */
    public List<T> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    /**
     * 根据条件实例查询一个对象
     */
    public T selectOne(T record) {
        return mapper.selectOne(record);
    }

    /**
     * 新增
     */
    public int insert(T record) {
        return mapper.insert(record);
    }

    /**
     * 选择新增
     */
    public int insertSelective(T record) {
        return mapper.insertSelective(record);
    }

    /**
     * 根据主键更新
     */
    public int updateByPrimaryKey(T record) {
        return mapper.updateByPrimaryKey(record);
    }

    /**
     * 根据主键选择更新
     */
    public int updateByPrimaryKeySelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据属性更新
     */
    public int updateByExample(T record, Example example) {
        return mapper.updateByExample(record, example);
    }

    /**
     * 根据属性选择更新
     */
    public int updateByExampleSelective(T record, Example example) {
        return mapper.updateByExampleSelective(record, example);
    }

    /**
     * 删除
     */
    public int delete(T record) {
        return mapper.delete(record);
    }

    /**
     * 根据主键删除
     */
    public int deleteByPrimaryKey(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

    /**
     * 批量删除
     */
    @Transactional
    public void deleteBatchByPrimaryKey(List<T> record) throws RuntimeException {
        record.forEach(it -> mapper.delete(it));
    }

    /**
     * 根据属性删除
     */
    public int deleteByExample(Example example) {
        return mapper.deleteByExample(example);
    }

    /**
     * 分页查询所有
     */
    public PageInfo<T> selectAllByList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<T>(mapper.selectAll());
    }

    /**
     * 根据属性分页查询
     */
    public PageInfo<T> selectByList(T record, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<T>(mapper.select(record));
    }

    /**
     * 根据example分页查询
     */
    public PageInfo<T>  selectByExampleList(Example example, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(mapper.selectByExample(example));
    }

    /**
     * 分页查询所有
     */
    public PageInfo<T>  selectAllByOffSetList(Integer offset, Integer limit) {
        PageHelper.offsetPage(offset, limit);
        return new PageInfo<>(mapper.selectAll());
    }

    /**
     * 根据属性分页查询
     */
    public PageInfo<T> selectByOffSetList(T record, Integer offset, Integer limit) {
        PageHelper.offsetPage(offset, limit);
        return new PageInfo<>(mapper.select(record));
    }

    /**
     * 根据example分页查询
     */
    public PageInfo<T> selectByExampleOffSetList(Example example, Integer offset, Integer limit) {
        PageHelper.offsetPage(offset, limit);
        return new PageInfo<>(mapper.selectByExample(example));
    }

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     * @param record
     * @return
     */
    public int count(T record) {
        return mapper.selectCount(record);
    }
    
    /**
     * 根据Example条件进行查询总数
     * @param example
     * @return
     */
    public int countByExample(Example example) {
        return mapper.selectCountByExample(example);
    }
    
    /**
     * 根据Example条件进行聚合查询
     * @param example
     * @param aggregateCondition 聚合查询
     * @return
     */
    public List<T> selectAggregationByExample(Example example, AggregateCondition aggregateCondition)
    {
        return mapper.selectAggregationByExample(example, aggregateCondition);
    }
    
    
}
