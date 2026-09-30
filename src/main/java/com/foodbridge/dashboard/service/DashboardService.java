package com.foodbridge.dashboard.service;

import com.foodbridge.dashboard.dto.response.DashboardResponse;

public interface DashboardService {

    DashboardResponse getDashboard(String email);
}