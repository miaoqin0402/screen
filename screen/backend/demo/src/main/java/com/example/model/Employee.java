package com.example.model;

import lombok.Getter;
import lombok.Setter;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.AttachmentType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@Erupt(name = "员工管理")
@Getter
@Setter
public class Employee extends BaseModel {

        @EruptField(
                views = @View(title = "工号", sortable = true),
                edit = @Edit(
                        title = "工号",
                        notNull = true,
                        search = @Search(vague = true))
        )
        @Column(unique = true, nullable = false)
        private String employeeId;

        @EruptField(
                views = @View(title = "姓名"),
                edit = @Edit(
                        title = "姓名",
                        notNull = true,
                        search = @Search(vague = true))
        )
        private String name;

        @EruptField(
                views = @View(title = "一级部门"),
                edit = @Edit(
                        title = "一级部门",
                        search = @Search(vague = true))
        )
        private String departmentLevel1;

        @EruptField(
                views = @View(title = "二级部门"),
                edit = @Edit(
                        title = "二级部门",
                        search = @Search(vague = true))
        )
        private String departmentLevel2;

        @EruptField(
                views = @View(title = "职位"),
                edit = @Edit(
                        title = "职位",
                        search = @Search(vague = true))
        )
        private String position;

        @EruptField(
                views = @View(title = "头像"),
                edit = @Edit(
                        title = "员工头像",
                        type = EditType.ATTACHMENT,
                        attachmentType = @AttachmentType(
                                type = AttachmentType.Type.IMAGE
                        )
                )
        )
        private String avatarUrl;

}