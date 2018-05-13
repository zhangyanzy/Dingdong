package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcong on 2018/3/13.
 */

public class QueryParam implements Serializable, Cloneable {
  private int start = 0;
  private int limit = 10;
  private int pageInde=0;
  private List<FilterParam> filters = new ArrayList();
  private List<SortParam> sorters = new ArrayList();

  public QueryParam() {
  }

  public QueryParam(int limit) {
    this.limit = limit;
  }

  public int getStart() {
    return this.start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getLimit() {
    return this.limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public List<FilterParam> getFilters() {
    return this.filters;
  }

  public void setFilters(List<FilterParam> filters) {
    this.filters = filters;
  }

  public List<SortParam> getSorters() {
    return this.sorters;
  }

  public void setSorters(List<SortParam> sorters) {
    this.sorters = sorters;
  }

  public int getPageInde() {
    return pageInde;
  }

  public void setPageInde(int pageInde) {
    this.pageInde = pageInde;
  }

  public QueryParam clone() {
    QueryParam queryParam = null;

    try {
      queryParam = (QueryParam) super.clone();
      queryParam.setFilters((List) ((ArrayList) this.getFilters()).clone());
      queryParam.setLimit(this.limit);
      queryParam.setSorters((List) ((ArrayList) this.getSorters()).clone());
      return queryParam;
    } catch (CloneNotSupportedException var3) {
      var3.printStackTrace();
      throw new RuntimeException();
    }
  }
}
