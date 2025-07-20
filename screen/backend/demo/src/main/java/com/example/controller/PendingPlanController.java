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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
public class PendingPlanController {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public PendingPlanResponse getAllPlans() {
        System.out.println("收到 /api/plans 请求");

        // 查询所有待完成计划（taskStatus = 1）
        List<Plan> allPlans = planRepository.findByTaskStatus(1);
        System.out.println("从数据库获取到 " + allPlans.size() + " 条记录");

        // 转换为 PlanItem 格式
        List<PlanItem> plans = allPlans.stream().map(plan -> {
            PlanItem item = new PlanItem();
            item.setResponsiblePerson(plan.getResponsiblePerson());
            item.setActionPlan(plan.getActionPlan());
            item.setInspector(plan.getInspector());
            item.setCompletionDate(plan.getEndDate().toString());
            item.setAvatarUrl(getAvatarUrl(plan.getResponsiblePerson()));
            return item;
        }).collect(Collectors.toList());

        // 构建响应对象
        PendingPlanResponse response = new PendingPlanResponse();
        response.setTotalPending(plans.size());
        response.setPlans(plans);

        System.out.println("返回所有 " + plans.size() + " 条计划");
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
    public static class PendingPlanResponse {
        private int totalPending;
        private List<PlanItem> plans;

        public int getTotalPending() { return totalPending; }
        public void setTotalPending(int totalPending) { this.totalPending = totalPending; }
        public List<PlanItem> getPlans() { return plans; }
        public void setPlans(List<PlanItem> plans) { this.plans = plans; }
    }

    // 计划项对象
    public static class PlanItem {
        private String responsiblePerson;
        private String actionPlan;
        private String inspector;
        private String completionDate;
        private String avatarUrl;

        public String getResponsiblePerson() { return responsiblePerson; }
        public void setResponsiblePerson(String responsiblePerson) { this.responsiblePerson = responsiblePerson; }
        public String getActionPlan() { return actionPlan; }
        public void setActionPlan(String actionPlan) { this.actionPlan = actionPlan; }
        public String getInspector() { return inspector; }
        public void setInspector(String inspector) { this.inspector = inspector; }
        public String getCompletionDate() { return completionDate; }
        public void setCompletionDate(String completionDate) { this.completionDate = completionDate; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }
}