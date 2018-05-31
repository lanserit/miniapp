package com.huanghuo.wechatapp.lottery;

import com.huanghuo.wechatapp.domain.model.User;
import com.huanghuo.wechatapp.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@MapperScan("com.huanghuo.wechatapp.mapper")
@SpringBootTest
@Transactional
public class LotteryApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private UserMapper userMapper;

	@Test
	@Rollback
	public void findByName() throws Exception {
		userMapper.insert("AAA", "12344", System.currentTimeMillis());
		User u = userMapper.findByNickName("AAA");
		Assert.assertEquals("12344", u.getOpenid());
	}
}
