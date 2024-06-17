package com.dbt.platform.mn.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuda
 * @since 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("main_area_info_mn")
public class MainAreaInfoMn implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("id")
    private String id;

    @TableField("name")
    private String name;

    @TableField("pid")
    private String pid;

    @TableField("level")
    private String level;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String PID = "pid";

    public static final String LEVEL = "level";

}
