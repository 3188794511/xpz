package com.lj.log.api;

import com.lj.base.Result;
import com.lj.log.service.UpdateLogService;
import com.lj.model.log.UpdateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(("/xpz/api/log/update-log"))
public class UpdateLogApiController {
    @Autowired
    private UpdateLogService updateLogService;

    @GetMapping
    public Result updateLogList(){
        List<UpdateLog> updateLogs = updateLogService.list();
        return Result.ok(updateLogs);
    }

}
