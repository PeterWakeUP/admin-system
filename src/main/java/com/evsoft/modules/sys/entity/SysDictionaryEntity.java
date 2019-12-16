package com.evsoft.modules.sys.entity;

/**
    * sys_dictionary 实体类
    * 2018-04-11 09:49:48 Lw
    */ 

public class SysDictionaryEntity {
	/**0
	*铸件ID
	*/
	private int	id;
	/**
	*字典Key
	*/
	private String	dicKey;
	/**
	*具体值
	*/
	private String	dicValue;
	/**
	*字典名称
	*/
	private String	dicName;
	/**
	*备注
	*/
	private String	remarks;
	/**
	*是否固定
	*/
	private String	fixed;
	/**
	 * 排序
	 */
	private String	dicSort;
	/**
	*上级id
	*/
	private int	pid;

	   /**
		* 删除标志   0未删除  1已删除
		*/
	private Integer flag;

	   /**
		* 创建时间
		*/
	private String createAt;

	/**
	 * 更新时间
	 */
	private String updateAt;

	   /**
		* 用户id
		*/
	private Long userId;

	public String getDicSort() {
		return dicSort;
	}

	public void setDicSort(String dicSort) {
		this.dicSort = dicSort;
	}

	public int getId() {
		   return id;
	   }

	   public void setId(int id) {
		   this.id = id;
	   }

	   public String getDicKey() {
		   return dicKey;
	   }

	   public void setDicKey(String dicKey) {
		   this.dicKey = dicKey;
	   }

	   public String getDicValue() {
		   return dicValue;
	   }

	   public void setDicValue(String dicValue) {
		   this.dicValue = dicValue;
	   }

	   public String getDicName() {
		   return dicName;
	   }

	   public void setDicName(String dicName) {
		   this.dicName = dicName;
	   }

	   public String getRemarks() {
		   return remarks;
	   }

	   public void setRemarks(String remarks) {
		   this.remarks = remarks;
	   }

	   public String getFixed() {
		   return fixed;
	   }

	   public void setFixed(String fixed) {
		   this.fixed = fixed;
	   }

	   public int getPid() {
		   return pid;
	   }

	   public void setPid(int pid) {
		   this.pid = pid;
	   }

	   public Integer getFlag() {
		   return flag;
	   }

	   public void setFlag(Integer flag) {
		   this.flag = flag;
	   }

	   public String getCreateAt() {
		   return createAt;
	   }

	   public void setCreateAt(String createAt) {
		   this.createAt = createAt;
	   }

	   public String getUpdateAt() {
		   return updateAt;
	   }

	   public void setUpdateAt(String updateAt) {
		   this.updateAt = updateAt;
	   }

	   public Long getUserId() {
		   return userId;
	   }

	   public void setUserId(Long userId) {
		   this.userId = userId;
	   }

	@Override
	public String toString() {
		return "SysDictionaryEntity{" +
				"id=" + id +
				", dicKey='" + dicKey + '\'' +
				", dicValue='" + dicValue + '\'' +
				", dicName='" + dicName + '\'' +
				", remarks='" + remarks + '\'' +
				", fixed='" + fixed + '\'' +
				", pid=" + pid +
				", flag='" + flag + '\'' +
				", createAt='" + createAt + '\'' +
				", updateAt='" + updateAt + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}
}
