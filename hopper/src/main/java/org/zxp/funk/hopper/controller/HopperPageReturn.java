package org.zxp.funk.hopper.controller;

public class HopperPageReturn extends HopperBaseReturn {
	
	private long pages;
	private long countPer;
	private long count;
	private long curPage;
	
	public long getPages() {
		return pages;
	}
	public void setPages(long pages) {
		this.pages = pages;
	}
	public long getCountPer() {
		return countPer;
	}
	public void setCountPer(long countPer) {
		this.countPer = countPer;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getCurPage() {
		return curPage;
	}
	public void setCurPage(long curPage) {
		this.curPage = curPage;
	}
	
	

}
