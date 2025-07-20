package com.example.controller;

import com.example.model.Employee;
import com.example.model.Plan;
import com.example.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/overdue-tasks")
public class OverdueTaskController {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public OverdueTaskResponse getAllOverdueTasks() {
        // 获取所有逾期任务（taskStatus = 3）
        List<Plan> plans = planRepository.findByTaskStatus(3);

        // 转换为 OverdueTaskResponse 格式并按逾期天数降序排序
        List<OverdueTaskResponse.TaskItem> tasks = plans.stream().map(plan -> {
                    OverdueTaskResponse.TaskItem item = new OverdueTaskResponse.TaskItem();
                    item.setSerialNumber(plan.getSerialNumber());
                    item.setResponsiblePerson(plan.getResponsiblePerson());
                    item.setActionPlan(plan.getActionPlan());
                    item.setInspector(plan.getInspector());
                    // 计算逾期天数
                    int overdueDays = 0;
                    if (plan.getEndDate() != null) {
                        try {
                            LocalDate endDate = new java.util.Date(plan.getEndDate().getTime())
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            LocalDate currentDate = LocalDate.now();
                            overdueDays = (int) Math.max(0, ChronoUnit.DAYS.between(endDate, currentDate));
                        } catch (Exception e) {
                            System.out.println("计算逾期天数失败 for plan " + plan.getSerialNumber() + ": " + e.getMessage());
                        }
                    } else {
                        System.out.println("endDate is null for plan " + plan.getSerialNumber());
                    }
                    item.setOverdueDays(overdueDays);
                    // 获取头像
                    item.setAvatarUrl(getAvatarUrl(plan.getResponsiblePerson()));
                    return item;
                })
                // 按逾期天数降序排序
                .sorted(Comparator.comparingInt(OverdueTaskResponse.TaskItem::getOverdueDays).reversed())
                .collect(Collectors.toList());

        // 构建响应对象
        OverdueTaskResponse response = new OverdueTaskResponse();
        response.setTotalOverdueTasks(tasks.size());
        response.setTasks(tasks);

        return response;
    }

    // 查询 Employee 表获取 avatarUrl
    private String getAvatarUrl(String responsiblePerson) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT e.avatarUrl FROM Employee e WHERE e.name = :name");
            query.setParameter("name", responsiblePerson);
            return (String) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("未找到员工 " + responsiblePerson + " 的头像: " + e.getMessage());
            return null; // 或返回默认头像路径
        }
    }

    // 响应对象
    public static class OverdueTaskResponse {
        private long totalOverdueTasks;
        private List<TaskItem> tasks;

        public long getTotalOverdueTasks() {
            return totalOverdueTasks;
        }

        public void setTotalOverdueTasks(long totalOverdueTasks) {
            this.totalOverdueTasks = totalOverdueTasks;
        }

        public List<TaskItem> getTasks() {
            return tasks;
        }

        public void setTasks(List<TaskItem> tasks) {
            this.tasks = tasks;
        }

        public static class TaskItem {
            private String serialNumber;
            private String responsiblePerson;
            private String actionPlan;
            private String inspector;
            private int overdueDays;
            private String avatarUrl;

            public String getSerialNumber() { return serialNumber; }
            public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
            public String getResponsiblePerson() { return responsiblePerson; }
            public void setResponsiblePerson(String responsiblePerson) { this.responsiblePerson = responsiblePerson; }
            public String getActionPlan() { return actionPlan; }
            public void setActionPlan(String actionPlan) { this.actionPlan = actionPlan; }
            public String getInspector() { return inspector; }
            public void setInspector(String inspector) { this.inspector = inspector; }
            public int getOverdueDays() { return overdueDays; }
            public void setOverdueDays(int overdueDays) { this.overdueDays = overdueDays; }
            public String getAvatarUrl() { return avatarUrl; }
            public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
        }
    }
}