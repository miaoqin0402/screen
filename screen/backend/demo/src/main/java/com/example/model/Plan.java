package com.example.model;

import lombok.Getter;
import lombok.Setter;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ChoiceType;
import xyz.erupt.annotation.sub_field.sub_edit.DateType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.annotation.sub_field.sub_edit.VL;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "plan")
@Erupt(name = "计划管理")
@Getter
@Setter
public class Plan extends BaseModel {

    @EruptField(
            views = @View(title = "计划编号", sortable = true),
            edit = @Edit(
                    title = "计划编号",
                    notNull = true,
                    search = @Search(vague = true)
            )
    )
    @Column(unique = true, nullable = false)
    private String serialNumber;

    @EruptField(
            views = @View(title = "责任人"),
            edit = @Edit(
                    title = "责任人",
                    notNull = true,
                    search = @Search(vague = true)
            )
    )
    private String responsiblePerson;

    @EruptField(
            views = @View(title = "行动计划"),
            edit = @Edit(
                    title = "行动计划",
                    notNull = true,
                    type = EditType.TEXTAREA,
                    search = @Search(vague = true)
            )
    )
    private String actionPlan;

    @EruptField(
            views = @View(title = "检查人"),
            edit = @Edit(
                    title = "检查人",
                    notNull = true,
                    search = @Search(vague = true)
            )
    )
    private String inspector;

    @EruptField(
            views = @View(title = "任务状态"),
            edit = @Edit(
                    title = "任务状态",
                    notNull = true,
                    type = EditType.CHOICE,
                    choiceType = @ChoiceType(vl = {
                            @VL(value = "1", label = "待完成"),
                            @VL(value = "2", label = "已完成"),
                            @VL(value = "3", label = "逾期")
                    }),
                    search = @Search
            )
    )
    private Integer taskStatus;

    @EruptField(
            views = @View(title = "开始日期"),
            edit = @Edit(
                    title = "开始日期",
                    notNull = true,
                    type = EditType.DATE,
                    dateType = @DateType(type = DateType.Type.DATE)
            )
    )
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @EruptField(
            views = @View(title = "截止日期"),
            edit = @Edit(
                    title = "截止日期",
                    notNull = true,
                    type = EditType.DATE,
                    dateType = @DateType(type = DateType.Type.DATE)
            )
    )
    @Temporal(TemporalType.DATE)
    private Date endDate;
}