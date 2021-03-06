package com.mao.sys.service;


import com.mao.sys.utils.DataGridView;
import com.mao.sys.vo.LogInfoVo;

/**
 * 日志管理的服务接口
 * @author mao
 */
public interface ILogInfoService {

    /**
     * 查询所有日志
     * @param logInfoVo
     * @return
     */
    public DataGridView queryAllLogInfo(LogInfoVo logInfoVo);

    /**
     * 添加日志
     * @param logInfoVo
     */
    public void addLogInfo(LogInfoVo logInfoVo);

    /**
     * 根据id删除日志
     * @param logInfoId
     */
    public void deleteLogInfo(Integer logInfoId);

    /**
     * 批量删除日志
     * @param ids
     */
    public void deleteBatchLogInfo(Integer [] ids);

}
