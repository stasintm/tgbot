package ru.alligator.bot.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alligator.bot.dto.VacationTo;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "vacationFeignClient", url = "${url.vacation}")
public interface VacationFeignClient {

    @GetMapping("/vacations")
    public List<VacationTo> findMyVacation(@RequestParam UUID employeeId);

    @PostMapping("/vacations")
    public VacationTo createVacation(@RequestBody VacationTo to);
}
