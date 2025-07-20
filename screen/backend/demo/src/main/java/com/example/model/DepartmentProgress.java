package com.example.model;

import lombok.Getter;
import lombok.Setter;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.NumberType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;

import javax.persistence.*;

@Entity
@Table(name = "department_progress")
@Erupt(name = "销售进度")
@Getter
@Setter
public class DepartmentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EruptField
    private Long id;
    @EruptField(
            views = @View(title = "部门名称", sortable = true),
            edit = @Edit(
                    title = "部门名称",
                    notNull = true,
                    search = @Search(vague = true),
                    inputType = @InputType
            )
    )
    @Column(name = "department_name", nullable = false, length = 50, unique = true)
    private String departmentName;
    @EruptField(
            views = @View(title = "达成率"),
            edit = @Edit(
                    title = "达成率",
                    notNull = true,
                    type = EditType.NUMBER,
                    numberType = @NumberType(max = 100, min = 0)
            )
    )
    @Column(name = "achievement_rate", nullable = false)
    private Double achievementRate;

}