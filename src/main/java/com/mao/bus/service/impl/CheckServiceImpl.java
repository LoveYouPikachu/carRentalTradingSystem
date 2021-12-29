package com.mao.bus.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mao.bus.domain.Car;
import com.mao.bus.domain.Check;
import com.mao.bus.domain.Customer;
import com.mao.bus.domain.Rent;
import com.mao.bus.mapper.CarMapper;
import com.mao.bus.mapper.CheckMapper;
import com.mao.bus.mapper.CustomerMapper;
import com.mao.bus.mapper.RentMapper;
import com.mao.bus.service.ICheckService;
import com.mao.bus.vo.CheckVo;
import com.mao.sys.constast.SysConstast;
import com.mao.sys.utils.DataGridView;
import com.mao.sys.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckServiceImpl implements ICheckService {

    @Autowired
    private CheckMapper checkMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RentMapper rentMapper;

    @Autowired
    private CarMapper carMapper;

    @Override
    public Map<String, Object> initCheckFormData(String rentid) {
        //查询出租单
        Rent rent = this.rentMapper.selectByPrimaryKey(rentid);
        //查询客户
        Customer customer = this.customerMapper.selectByPrimaryKey(rent.getIdentity());
        //查询车辆
        Car car = this.carMapper.selectByPrimaryKey(rent.getCarnumber());
        //组装check
        Check check = new Check();
        check.setCheckid(RandomUtils.createRandomStringUseTime(SysConstast.CAR_ORDER_JC));
        check.setRentid(rentid);
        check.setCheckdate(new Date());
//        User user =(User) WebUtils.getHttpSession().getAttribute("user");
/*        System.out.println("======================================");
        System.out.println(user.getRealname());*/

//        check.setOpername(user.getRealname());

        check.setOpername(rent.getOpername());

        Map<String, Object> map = new HashMap<>();
        map.put("rent",rent);
        map.put("customer",customer);
        map.put("car",car);
        map.put("check",check);

        return map;
    }

    /**
     * 保存检查单数据
     * @param checkVo
     */
    @Override
    public void addCheck(CheckVo checkVo) {
        this.checkMapper.insertSelective(checkVo);
        //更改出租单的状态
        Rent rent = this.rentMapper.selectByPrimaryKey(checkVo.getRentid());
        //更改为已归还
        rent.setRentflag(SysConstast.RENT_BACK_TRUE);
        this.rentMapper.updateByPrimaryKeySelective(rent);
        //更改汽车的状态
        Car car = this.carMapper.selectByPrimaryKey(rent.getCarnumber());
        //更改汽车状态为未出租
        car.setIsrenting(SysConstast.RENT_CAR_FALSE);
        this.carMapper.updateByPrimaryKeySelective(car);
    }

    /**
     * 查询所有检查单
     * @param checkVo
     * @return
     */
    @Override
    public DataGridView queryAllCheck(CheckVo checkVo) {
        Page<Object> page = PageHelper.startPage(checkVo.getPage(), checkVo.getLimit());
        List<Check> data = this.checkMapper.queryAllCheck(checkVo);
        return new DataGridView(page.getTotal(),data);
    }

    /**
     * 批量删除检查单
     * @param ids
     */
    @Override
    public void deleteBatchCheck(String[] ids) {
        for (String id : ids) {
            this.checkMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 删除检查单
     * @param checkVo
     */
    @Override
    public void deleteCheck(CheckVo checkVo) {
        this.checkMapper.deleteByPrimaryKey(checkVo.getCheckid());
    }

    /**
     * 更新检查单
     * @param checkVo
     */
    @Override
    public void updateCheck(CheckVo checkVo) {
        this.checkMapper.updateByPrimaryKeySelective(checkVo);
    }
}