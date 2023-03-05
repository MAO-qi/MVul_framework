package com.zsj.mapper;

import com.zsj.poji.vuldatas;

public interface VulMapper {
    void add_Apssql(vuldatas vuldatas);
    void add_keda(vuldatas vul);
    void add_Dlink(vuldatas vuldatas);
    vuldatas select_Dlink();
    vuldatas select_keda();
    vuldatas select_Apssql();
    void truncate();
}
