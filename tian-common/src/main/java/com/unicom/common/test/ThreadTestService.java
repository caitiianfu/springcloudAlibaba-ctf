/**
* Title: TranTestService.java
* Copyright: Copyright (c) 2018
* Company: Unicom
* @author ctf
* @date 2020年2月27日
* @version 1.0
*/
package com.unicom.common.test;

import com.unicom.common.domain.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
* <p>Title: TranTestService<／p>
* Copyright: Copyright (c) 2018
* Company: Unicom
* @author ctf
* @date 2020年2月27日
* @version 1.0
*/
@Service
public class ThreadTestService {
		Logger logger=LoggerFactory.getLogger(ThreadTestService.class);
		@Async("taskExecutor")
		public void sendMsg1() throws InterruptedException {
			logger.info("发送短信----------------1 执行开始");
			Thread.sleep(5000);
			logger.info("发送短信----------------1 执行结束");
		}
		@Async("taskExecutor")
		public void sendMsg2() throws InterruptedException {
			logger.info("发送短信----------------2 执行开始");
			Thread.sleep(2000);
			logger.info("发送短信----------------2 执行结束");
		}


}
