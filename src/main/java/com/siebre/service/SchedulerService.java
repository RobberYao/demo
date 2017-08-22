package com.siebre.service;

import java.util.Date;

import org.quartz.CronExpression;

public interface SchedulerService {
	
	/** 
     * ���� Quartz Cron Expression �������� 
     *  
     * @param cronExpression 
     *            Quartz Cron ���ʽ���� "0/10 * * ? * * *"�� 
     */  
    void schedule(String cronExpression);  
  
    /** 
     * ���� Quartz Cron Expression �������� 
     *  
     * @param name 
     *            Quartz CronTrigger���� 
     * @param cronExpression 
     *            Quartz Cron ���ʽ���� "0/10 * * ? * * *"�� 
     */  
    void schedule(String name, String cronExpression);  
  
    /** 
     * ���� Quartz Cron Expression �������� 
     *  
     * @param name 
     *            Quartz CronTrigger���� 
     * @param group 
     *            Quartz CronTrigger�� 
     * @param cronExpression 
     *            Quartz Cron ���ʽ���� "0/10 * * ? * * *"�� 
     */  
    void schedule(String name, String group, String cronExpression);  
  
    /** 
     * ���� Quartz Cron Expression �������� 
     *  
     * @param cronExpression 
     *            Quartz CronExpression 
     */  
    void schedule(CronExpression cronExpression);  
  
    /** 
     * ���� Quartz Cron Expression �������� 
     *  
     * @param name 
     *            Quartz CronTrigger���� 
     * @param cronExpression 
     *            Quartz CronExpression 
     */  
    void schedule(String name, CronExpression cronExpression);  
  
    /** 
     * ���� Quartz Cron Expression �������� 
     *  
     * @param name 
     *            Quartz CronTrigger���� 
     * @param group 
     *            Quartz CronTrigger�� 
     * @param cronExpression 
     *            Quartz CronExpression 
     */  
    void schedule(String name, String group, CronExpression cronExpression);  
  
    /** 
     * ��startTimeʱִ�е���һ�� 
     *  
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     */  
    void schedule(Date startTime);  
  
    void schedule(Date startTime, String group);  
  
    /** 
     * ��startTimeʱִ�е���һ�� 
     *  
     * @param name 
     *            Quartz SimpleTrigger ���� 
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     */  
    void schedule(String name, Date startTime);  
  
    void schedule(String name, Date startTime, String group);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��� 
     *  
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     * @param endTime 
     *            ���Ƚ���ʱ�� 
     */  
    void schedule(Date startTime, Date endTime);  
  
    void schedule(Date startTime, Date endTime, String group);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��� 
     *  
     * @param name 
     *            Quartz SimpleTrigger ���� 
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     * @param endTime 
     *            ���Ƚ���ʱ�� 
     */  
    void schedule(String name, Date startTime, Date endTime);  
  
    void schedule(String name, Date startTime, Date endTime, String group);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��ȣ��ظ�ִ��repeatCount�� 
     *  
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     * @param repeatCount 
     *            �ظ�ִ�д��� 
     */  
    void schedule(Date startTime, int repeatCount);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��ȣ��ظ�ִ��repeatCount�� 
     *  
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     * @param endTime 
     *            ���Ƚ���ʱ�� 
     * @param repeatCount 
     *            �ظ�ִ�д��� 
     */  
    void schedule(Date startTime, Date endTime, int repeatCount);  
  
    void schedule(Date startTime, Date endTime, int repeatCount, String group);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��ȣ��ظ�ִ��repeatCount�� 
     *  
     * @param name 
     *            Quartz SimpleTrigger ���� 
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     * @param endTime 
     *            ���Ƚ���ʱ�� 
     * @param repeatCount 
     *            �ظ�ִ�д��� 
     */  
    void schedule(String name, Date startTime, Date endTime, int repeatCount);  
  
    void schedule(String name, Date startTime, Date endTime, int repeatCount, String group);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��ȣ��ظ�ִ��repeatCount�Σ�ÿ��repeatInterval��ִ��һ�� 
     *  
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     *  
     * @param repeatCount 
     *            �ظ�ִ�д��� 
     * @param repeatInterval 
     *            ִ��ʱ����� 
     */  
    void schedule(Date startTime, int repeatCount, long repeatInterval);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��ȣ��ظ�ִ��repeatCount�Σ�ÿ��repeatInterval��ִ��һ�� 
     *  
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     * @param endTime 
     *            ���Ƚ���ʱ�� 
     * @param repeatCount 
     *            �ظ�ִ�д��� 
     * @param repeatInterval 
     *            ִ��ʱ����� 
     */  
    void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval);  
  
    void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, String group);  
  
    /** 
     * ��startTimeʱִ�е��ԣ�endTime����ִ�е��ȣ��ظ�ִ��repeatCount�Σ�ÿ��repeatInterval��ִ��һ�� 
     *  
     * @param name 
     *            Quartz SimpleTrigger ���� 
     * @param startTime 
     *            ���ȿ�ʼʱ�� 
     * @param endTime 
     *            ���Ƚ���ʱ�� 
     * @param repeatCount 
     *            �ظ�ִ�д��� 
     * @param repeatInterval 
     *            ִ��ʱ����� 
     */  
    void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval);  
  
    void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval, String group);  
  
    /** 
     * ��ͣ������ 
     *  
     * @param triggerName 
     *            ���������� 
     */  
    void pauseTrigger(String triggerName);  
  
    /** 
     * ��ͣ������ 
     *  
     * @param triggerName 
     *            ���������� 
     * @param group 
     *            �������� 
     */  
    void pauseTrigger(String triggerName, String group);  
  
    /** 
     * �ָ������� 
     *  
     * @param triggerName 
     *            ���������� 
     */  
    void resumeTrigger(String triggerName);  
  
    /** 
     * �ָ������� 
     *  
     * @param triggerName 
     *            ���������� 
     * @param group 
     *            �������� 
     */  
    void resumeTrigger(String triggerName, String group);  
  
    /** 
     * ɾ�������� 
     *  
     * @param triggerName 
     *            ���������� 
     * @return 
     */  
    boolean removeTrigdger(String triggerName);  
  
    /** 
     * ɾ�������� 
     *  
     * @param triggerName 
     *            ���������� 
     * @param group 
     *            �������� 
     * @return 
     */  
    boolean removeTrigdger(String triggerName, String group); 
    
}  
