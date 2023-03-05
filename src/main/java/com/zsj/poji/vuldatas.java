package com.zsj.poji;

public class vuldatas {
    public Integer id;
    public String vul_host;
    public String vul_name;
    public String vul_payload;
    public String vul_describe;

    public String getVul_describe() {
        return vul_describe;
    }

    public void setVul_describe(String vul_describe) {
        this.vul_describe = vul_describe;
    }

    public Integer getId() {
        return id;
    }

    public String getVul_host() {
        return vul_host;
    }

    public String getVul_name() {
        return vul_name;
    }

    public String getVul_payload() {
        return vul_payload;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVul_host(String vul_host) {
        this.vul_host = vul_host;
    }

    public void setVul_name(String vul_name) {
        this.vul_name = vul_name;
    }

    public void setVul_payload(String vul_payload) {
        this.vul_payload = vul_payload;
    }

    @Override
    public String toString() {
        return "vuldatas{" +
                "id=" + id +
                ", vul_host='" + vul_host + '\'' +
                ", vul_name='" + vul_name + '\'' +
                ", vul_payload='" + vul_payload + '\'' +
                ", vul_describe='" + vul_describe + '\'' +
                '}';
    }
}