package com.siebre.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.siebre.service.UserService;

public class MyJobDetail extends QuartzJobBean {

	private static Log log = LogFactory.getLog(MyJobDetail.class);
	
	private static String jobResource = "MyJobDetail";
	
	private static final String APPLICATIONCONTEXT = "applicationContext";  

	private UserService userService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		long startTime = System.currentTimeMillis();
		
		logJobStart(context);
		Exception ex = null;
		try {
			doExecute();
		} catch (Exception e) {
			ex = e;
		} finally {
			logJobEnd(startTime, ex);
		}
	}

	private void logJobStart(JobExecutionContext context) {
		try {
			doInit(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (log.isInfoEnabled())
			log.info("Start quartz job: " + jobResource);
	}
	
	private void doInit(JobExecutionContext context) {
		// 一些@Autowired的初始化
		log.info("-----初始化start-----");
		ApplicationContext applicationContext = null;
		try {
			applicationContext = getApplicationContext(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		userService = (UserService) applicationContext.getBean("userService");
		System.err.println(userService);
	}

	private void doExecute() {
		log.info("-----定时器任务start-----");
		try {
			Thread.sleep(300000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("-----定时器任务end-----");
	}
	
	private void logJobEnd(long startTime, Exception ex) {
		if (ex != null) 
			ex.printStackTrace();
		log.info("用时:" + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	/**
	 * 根据定时任务上下文获取spring上下文
	 * @param JobExecutionContext
	 * @return ApplicationContext
	 * @throws Exception
	 */
	private ApplicationContext getApplicationContext(JobExecutionContext context) throws Exception {  
        ApplicationContext applicationContext = null;  
        applicationContext = (ApplicationContext) context.getScheduler().getContext().get(APPLICATIONCONTEXT);  
        if (applicationContext == null) {  
            throw new JobExecutionException("No application context available in scheduler context for key \"" + APPLICATIONCONTEXT + "\"");  
        }  
        return applicationContext;  
	}  
	
}
