package com.ximen.plan.entity.router;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Vue路由 Meta
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouterMeta implements Serializable {

    private static final long serialVersionUID = 5499925008927195914L;

    private String title;

    @JsonIgnore
    private Boolean closeable;

    @JsonIgnore
    private Boolean isShow;

    public RouterMeta(String title) {
        this.title = title;
    }

}
