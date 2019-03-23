package com.faire.orders.faireorders.service;

import com.faire.orders.faireorders.service.entity.AnalyzesResult;
import com.faire.orders.faireorders.service.entity.ProcessOrderResult;

public interface AnalyzeService {

    AnalyzesResult getResultAnalytics(ProcessOrderResult result);

}
