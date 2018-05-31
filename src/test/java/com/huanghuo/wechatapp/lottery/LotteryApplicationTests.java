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
		User user = new User();
		user.setNickname("AAA");
		user.setOpenid("1231231");
		user.setCtime(System.currentTimeMillis());
		userMapper.insert(user);
		User u = userMapper.findByNickName("AAA");
		Assert.assertEquals("1231231", u.getOpenid());
	}
}
