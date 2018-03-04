package com.example.dao;

import java.io.Serializable;

/**
 * Created by damei on 18/1/22.
 */

    public class VoiceBoxLocation implements Serializable {

        private static final long serialVersionUID = 1L;

        private String poiname;

        private String country;

        private String province;

        private String city;

        private String district;

        private String street;

        private String streetnum;

        private String address;

        private String lat;

        private String lng;

        private String addrname;

        public String getPoiname() {
            return poiname;
        }

        public void setPoiname(String poiname) {
            this.poiname = poiname;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getStreetnum() {
            return streetnum;
        }

        public void setStreetnum(String streetnum) {
            this.streetnum = streetnum;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getAddrname() {
            return addrname;
        }

        public void setAddrname(String addrname) {
            this.addrname = addrname;
        }

        @Override
        public String toString() {
            return "POI [poiname=" + poiname + ", country=" + country + ", province=" + province + ", city=" + city
                    + ", district=" + district + ", street=" + street + ", streetnum=" + streetnum + ", address=" + address
                    + ", lat=" + lat + ", lng=" + lng + ", addrname=" + addrname + "]";
        }

    }


