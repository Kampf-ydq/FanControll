package com.clover.entity;

/*
 * 有功功率
 */
public class Report {

	private String fanNumber;  //风机编号
	
    private String tradeTime;  //间隔时间10s
    
    private double powerVal;   //有功功率
    
    private double envirTemp;  //环境温度
    
    private double motorAngle;   //风机扭矩
    
    
	public String getFanNumber() {
		return fanNumber;
	}
	public void setFanNumber(String fanNumber) {
		this.fanNumber = fanNumber;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public double getPowerVal() {
		return powerVal;
	}
	public void setPowerVal(double powerVal) {
		this.powerVal = powerVal;
	}
	public double getEnvirTemp() {
		return envirTemp;
	}
	public void setEnvirTemp(double envirTemp) {
		this.envirTemp = envirTemp;
	}
	public double getMotorAngle() {
		return motorAngle;
	}
	public void setMotorAngle(double motorAngle) {
		this.motorAngle = motorAngle;
	}
    
    
}
