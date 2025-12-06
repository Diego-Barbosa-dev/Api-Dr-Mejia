package com.drmejia.core.models;

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

    public boolean hasNullAttributes() {
        return this.order == null || 
               this.type == null ||
               this.esf == null || this.esf.isBlank() ||
               this.cil == null || this.cil.isBlank() ||
               this.axis == null || this.axis.isBlank() ||
               this.addition == null || this.addition.isBlank() ||
               this.dp == null || this.dp.isBlank() ||
               this.high == null || this.high.isBlank() ||
               this.avl == null || this.avl.isBlank() ||
               this.avp == null || this.avp.isBlank();
    }


}
