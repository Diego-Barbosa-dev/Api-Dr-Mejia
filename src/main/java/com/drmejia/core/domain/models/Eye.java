package com.drmejia.core.domain.models;

import com.drmejia.core.enums.EyeType;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Eye {
    private Long idEye;
    private @NonNull Long order;
    private EyeType type;

    private String esf;
    private String cil;
    private String axis;
    private String addition;
    private String dp;
    private String high;
    private String avl;
    private String avp;

}
