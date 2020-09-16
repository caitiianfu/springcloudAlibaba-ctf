package com.unicom.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息投递日志
 *
 * @author ctf
 * @since 2020-05-27
 */
@ApiModel(value = "MsgLog对象", description = "消息投递日志")
public class MsgLog extends Model<MsgLog> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "消息唯一标识")
  private String msgId;

  @ApiModelProperty(value = "消息体, json格式化")
  private String msg;

  @ApiModelProperty(value = "交换机")
  private String exchange;

  @ApiModelProperty(value = "路由键")
  private String routingKey;

  @ApiModelProperty(value = "状态: 0投递中 1投递成功 2投递失败 3已消费")
  private Integer status;

  @ApiModelProperty(value = "重试次数")
  private Integer tryCount;

  @ApiModelProperty(value = "下一次重试时间")
  private LocalDateTime nextTryTime;

  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updateTime;

/*
  @TableField(exist = false)
  private DataScope dataScope = new DataScope();

  public DataScope getDataScope() {
    return dataScope;
  }

  public void setDataScope(DataScope dataScope) {
    this.dataScope = dataScope;
  }
*/

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  public String getRoutingKey() {
    return routingKey;
  }

  public void setRoutingKey(String routingKey) {
    this.routingKey = routingKey;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getTryCount() {
    return tryCount;
  }

  public void setTryCount(Integer tryCount) {
    this.tryCount = tryCount;
  }

  public LocalDateTime getNextTryTime() {
    return nextTryTime;
  }

  public void setNextTryTime(LocalDateTime nextTryTime) {
    this.nextTryTime = nextTryTime;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  protected Serializable pkVal() {
    return this.msgId;
  }

  @Override
  public String toString() {
    return "MsgLog{"
        + "msgId="
        + msgId
        + ", msg="
        + msg
        + ", exchange="
        + exchange
        + ", routingKey="
        + routingKey
        + ", status="
        + status
        + ", tryCount="
        + tryCount
        + ", nextTryTime="
        + nextTryTime
        + ", createTime="
        + createTime
        + ", updateTime="
        + updateTime
        + "}";
  }
}
