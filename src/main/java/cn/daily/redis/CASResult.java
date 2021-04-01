package cn.daily.redis;

/**
 * @author zhaibo
 * @date 2021/2/26 07:23
 * @description :
 */
public class CASResult<T> {
	public CASResult(boolean success, T finalResult) {
		this.success = success;
		this.finalResult = finalResult;
	}

	private boolean success;
	private T       finalResult;

	/**
	 * cas是否成功执行
	 *
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 最终结果
	 *
	 * @return
	 */
	public T getFinalResult() {
		return finalResult;
	}

}
