package com.dindong.dingdong.network.bean.apply;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * 申请合作</>
 */

public class RequestInstitutionApply extends RequestApply {
  private String address;// 详细地址
  private String busLicenseId;// 营业执照图ID
  private String busLicenseUrl;// 营业执照图URL
  private String cityCode;// 市编码
  private String cityName;// 市名称
  private String districtCode;// 区编码
  private String districtName;// 区名称
  private String provinceCode;// 省编码
  private String provinceName;// 省名称
  private String contact;// 联系人
  private String fax;// 传真
  private String name;// 机构名称
  private String tel;// 联系电话

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getBusLicenseId() {
    return busLicenseId;
  }

  public void setBusLicenseId(String busLicenseId) {
    this.busLicenseId = busLicenseId;
  }

  public String getBusLicenseUrl() {
    return busLicenseUrl;
  }

  public void setBusLicenseUrl(String busLicenseUrl) {
    this.busLicenseUrl = busLicenseUrl;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getDistrictCode() {
    return districtCode;
  }

  public void setDistrictCode(String districtCode) {
    this.districtCode = districtCode;
  }

  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public String getProvinceName() {
    return provinceName;
  }

  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }
}
