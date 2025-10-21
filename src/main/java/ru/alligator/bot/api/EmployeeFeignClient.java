package ru.alligator.bot.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alligator.bot.dto.EmployeeTo;

import java.util.List;

@FeignClient(name = "employeeFeignClient", url = "${url.employee}")
public interface EmployeeFeignClient {

    @GetMapping("/employees")
    List<EmployeeTo> findEmployees(@RequestParam String query);
}
